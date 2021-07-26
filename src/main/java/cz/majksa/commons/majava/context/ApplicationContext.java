/*
 *  majava - cz.majksa.commons.majava.context.ApplicationContext
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

package cz.majksa.commons.majava.context;

import cz.majksa.commons.majava.context.config.ApplicationConfig;
import cz.majksa.commons.majava.di.Container;
import cz.majksa.commons.majava.di.SimpleContainer;
import lombok.Data;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.context.ApplicationContext}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class ApplicationContext {

    @Nonnull
    private final String name;
    @Nonnull
    private final Container container;

    @SneakyThrows
    public static ApplicationContext from(@Nullable ApplicationConfig config) {
        if (config == null) {
            return new ApplicationContext("Majava", new SimpleContainer());
        }
        final Container container;
        if (config.getDi() == null) {
            container = new SimpleContainer();
        } else {
            container = (Container) config.getDi().invoke(null);
        }
        return new ApplicationContext(
                config.getName(),
                container
        );
    }

}
