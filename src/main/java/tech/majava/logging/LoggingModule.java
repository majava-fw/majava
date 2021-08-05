/*
 *  majava - tech.majava.logging.LoggingModule
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package tech.majava.logging;

import tech.majava.cli.ConsoleModifiers;
import tech.majava.cli.ConsoleTextBuilder;
import tech.majava.context.ApplicationContext;
import tech.majava.listeners.ListenersModule;
import tech.majava.logging.events.LogEvent;
import tech.majava.modules.Module;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link LoggingModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class LoggingModule extends Module<LoggingConfig> {

    /**
     * @see Logger
     */
    @Nonnull
    private final Logger logger;

    private final Function<Throwable, Void> logFunction = throwable -> {
        log(throwable);
        return null;
    };

    private final LogEventHandler handler;

    /**
     * If application has been started
     */
    private boolean debug = false;

    /**
     * Constructor
     *
     * @param config  the module config
     * @param context the application context
     */
    public LoggingModule(@Nonnull LoggingConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "logging", "the module that takes care of logs");
        dependencies.add(ListenersModule.class);
        handler = new LogEventHandler(logFunction);
        logger = new Logger(LogManager.getLogger(config.getName()), config.getErrors(), handler);
        setDebug(config.isDebug());
        context.getContainer()
                .register(logFunction)
                .register(handler)
                .register(logger);
    }

    /**
     * Executed on module start
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> context.getModules().get(ListenersModule.class).registerHandler(LogEvent.class, handler));
    }

    /**
     * Executed on module shutdown
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> context.getModules().get(ListenersModule.class).unregisterHandler(LogEvent.class));
    }

    /**
     * Sets log level
     *
     * @param level the level to set logger to
     */
    public void setLevel(@Nonnull Level level) {
        debug = Level.DEBUG.isMoreSpecificThan(level);
        logger.setLevel(level);
    }

    /**
     * Sets debug
     *
     * @param debug if debug messages should be printed
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        logger.setLevel(debug ? Level.DEBUG : Level.ERROR);
    }

    /**
     * Toggles if debug messages should be printed
     */
    public void toggleDebug() {
        setDebug(!debug);
    }

    /**
     * Logs the runnable using {@link Logger}
     *
     * @param runnable the runnable to be logged
     */
    public void run(@Nonnull SafeRunnable<Throwable> runnable) {
        final Throwable throwable = runnable.apply(null);
        if (throwable != null) {
            log(throwable);
        }
    }

    /**
     * Logs the consumer using {@link Logger}
     *
     * @param consumer the consumer to be logged
     * @param subject  the subject to be consumed
     * @param <T>      the type of subject
     */
    public <T> void run(@Nonnull SafeConsumer<T, Throwable> consumer, @Nonnull T subject) {
        final Throwable throwable = consumer.apply(subject);
        if (throwable != null) {
            log(throwable);
        }
    }

    /**
     * Logs the throwable using {@link Logger}
     *
     * @param throwable the throwable to be logged
     */
    public void log(@Nonnull Throwable throwable) {
        final ConsoleTextBuilder textBuilder = new ConsoleTextBuilder();
        textBuilder
                .modify(ConsoleModifiers.BLACK)
                .modify(ConsoleModifiers.RED_BACKGROUND)
                .append("An error internal error occurred!")
                .newLine()
                .modify(ConsoleModifiers.RED)
                .append(throwable.getMessage())
                .print();
        logger.atError()
                .withLocation()
                .withThrowable(throwable)
                .log(throwable);
    }

}
