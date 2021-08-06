/*
 *  majava - tech.majava.Bootstrap
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

package tech.majava;

import tech.majava.application.Application;
import tech.majava.cli.CliApplication;
import tech.majava.context.ApplicationContext;
import tech.majava.context.config.ApplicationConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link Bootstrap}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Bootstrap {

    /**
     * Boots the application with default config file
     *
     * @return {@link tech.majava.cli.CliApplication}
     */
    public static CliApplication boot() {
        return boot(null);
    }

    /**
     * Boots the application with provided config file
     *
     * @param configFile the config file
     * @return {@link tech.majava.cli.CliApplication}
     */
    public static CliApplication boot(@Nullable String configFile) {
        final ApplicationConfig config = ApplicationConfig.load(configFile);
        final ApplicationContext context = new ApplicationContext(config);
        final Application application = new Application(context.getName(), context, context.getModules());
        return new CliApplication(application);
    }

    /**
     * Boots and starts the application with default config file
     *
     * @param args application arguments
     */
    public static void run(@Nonnull String[] args) {
        boot().run(args);
    }

}
