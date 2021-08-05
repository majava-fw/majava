/*
 *  majava - tech.majava.application.Application
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

package tech.majava.application;

import tech.majava.context.ApplicationContext;
import tech.majava.logging.Logger;
import tech.majava.logging.LoggingModule;
import tech.majava.modules.Modules;
import lombok.Getter;

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
     * @see tech.majava.logging.Logger
     */
    @Nonnull
    private final Logger logger;

    @Nonnull
    private final String name;

    @Nonnull
    private final ApplicationContext context;

    @Nonnull
    private final Modules modules;

    /**
     * If application has been started
     */
    private boolean running = false;

    public Application(@Nonnull String name, @Nonnull ApplicationContext context, @Nonnull Modules modules) {
        this.name = name;
        this.context = context;
        this.modules = modules;
        logger = modules.get(LoggingModule.class).getLogger();
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
                .exceptionally(modules.get(LoggingModule.class).getLogFunction())
                .join();
    }

    /**
     * Restarts the application
     */
    public void restart() {
        if (running) {
            stop();
        }
        start();
    }

    /**
     * Stops the application
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Application has not been started yet.");
        }
        running = false;
        logger
                .atDebug()
                .log("Trying to stop the application!");
        context
                .getModulesStarter()
                .shutdown()
                .join();
    }

}
