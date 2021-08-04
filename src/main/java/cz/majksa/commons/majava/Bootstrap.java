/*
 *  majava - cz.majksa.commons.majava.Bootstrap
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

import cz.majksa.commons.majava.application.Application;
import cz.majksa.commons.majava.cli.CliApplication;
import cz.majksa.commons.majava.context.ApplicationContext;
import cz.majksa.commons.majava.context.config.ApplicationConfig;

import javax.annotation.Nullable;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.Bootstrap}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Bootstrap {

    public static CliApplication boot() {
        return boot(null);
    }

    public static CliApplication boot(@Nullable String configFile) {
        final ApplicationConfig config = ApplicationConfig.load(configFile);
        final ApplicationContext context = new ApplicationContext(config);
        final Application application = new Application(context.getName(), context, context.getModules());
        return new CliApplication(application);
    }

}
