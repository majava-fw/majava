/*
 *  majava - cz.majksa.commons.majava.Application
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

package cz.majksa.commons.majava;

import cz.majksa.commons.majava.context.ApplicationContext;
import cz.majksa.commons.majava.context.config.ApplicationConfig;
import cz.majksa.commons.majava.di.Container;
import cz.majksa.commons.majava.logging.Logger;
import cz.majksa.commons.majava.logging.SafeRunnable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.Application}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class Application {

    /**
     * @see cz.majksa.commons.majava.logging.Logger
     */
    @Getter
    @Nonnull
    protected static final Logger logger = new Logger(LogManager.getLogger("Majava"));
    /**
     * If Listeners are listening for discord events
     */
    protected boolean running = false;
    protected ApplicationContext context;

    @Nonnull
    protected String name = getClass().getName();

    /**
     * Stars the application
     */
    public void start(@Nonnull String[] args) {
        logger.atDebug().log("Trying to start {}", name);
        if (running) {
            logger.atWarn().log("Application {} has already been started", name);
        } else {
            onStart(args)
                    .exceptionally(Application::log)
                    .join();
            running = true;
        }
    }

    @Nonnull
    protected abstract CompletableFuture<Void> onStart(@Nonnull String[] args);

    /**
     * Stops the application
     */
    public void shutdown() {
        logger.atDebug().log("Trying to stop {}", name);
        if (running) {
            onShutdown()
                    .exceptionally(Application::log)
                    .join();
            running = false;
        } else {
            logger.atWarn().log("Application {} has not been started yet", name);
        }
    }

    @Nonnull
    protected abstract CompletableFuture<Void> onShutdown();

    public static <T extends Application> void start(@Nonnull Class<T> appClass, @Nonnull String[] args) {
        final ApplicationContext context = ApplicationContext.from(ApplicationConfig.load());
        final T application = context
                .getContainer()
                .get(appClass);
        context.getContainer().register(context);
        application.context = context;
        application.start(args);
    }

    /**
     * Logs the throwable using {@link cz.majksa.commons.majava.logging.Logger}
     *
     * @param throwable the throwable to be logged
     * @return void
     */
    public static Void log(@Nonnull Throwable throwable) {
        logger.atError()
                .withLocation()
                .withThrowable(throwable)
                .log(throwable);
        return null;
    }

}
