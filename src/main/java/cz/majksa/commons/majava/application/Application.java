/*
 *  majava - cz.majksa.commons.majava.application.Application
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

package cz.majksa.commons.majava.application;

import cz.majksa.commons.majava.cli.ConsoleModifiers;
import cz.majksa.commons.majava.cli.ConsoleTextBuilder;
import cz.majksa.commons.majava.context.ApplicationContext;
import cz.majksa.commons.majava.logging.Logger;
import cz.majksa.commons.majava.logging.SafeRunnable;
import cz.majksa.commons.majava.modules.Modules;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link Application}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public final class Application {

    /**
     * @see cz.majksa.commons.majava.logging.Logger
     */
    @Nonnull
    private final Logger logger = new Logger(LogManager.getLogger("Majava"));

    @Nonnull
    private final String name;

    @Nonnull
    private final ApplicationContext context;
    /**
     * If application has been started
     */
    private boolean running = false;

    /**
     * If application has been started
     */
    private boolean debug = false;

    public Application(@Nonnull String name, @Nonnull ApplicationContext context, Modules modules) {
        this.name = name;
        this.context = context;

    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        logger.setLevel(debug ? Level.DEBUG : Level.ERROR);
    }

    public void toggleDebug() {
        setDebug(!debug);
    }

    /**
     * Stars the application
     */
    public void start() {
        if (running) {
            throw new IllegalStateException("Application has already been started.");
        }
        running = true;
        logger.atDebug().log("Trying to start the application!");
        context
                .getModulesStarter()
                .start()
                .exceptionally(this::log);
    }

    public void restart() {
        if (running) {
            exit();
        }
        start();
    }

    public void exit() {
        if (!running) {
            throw new IllegalStateException("Application has not been started yet.");
        }
        running = false;
        logger
                .atDebug()
                .log("Trying to exit the application!");
        context
                .getModulesStarter()
                .shutdown()
                .exceptionally(this::log);
    }

    /**
     * Logs the throwable using {@link cz.majksa.commons.majava.logging.Logger}
     *
     * @param throwable the throwable to be logged
     */
    public Void log(@Nonnull Throwable throwable) {
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
        return null;
    }

    /**
     * Logs the runnable using {@link cz.majksa.commons.majava.logging.Logger}
     *
     * @param runnable the runnable to be logged
     */
    public void run(@Nonnull SafeRunnable<Throwable> runnable) {
        final Throwable throwable = runnable.apply(null);
        if (throwable != null) {
            log(throwable);
        }
    }

}
