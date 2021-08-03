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

import com.fasterxml.jackson.databind.JsonNode;
import cz.majksa.commons.majava.context.config.ApplicationConfig;
import cz.majksa.commons.majava.di.Container;
import cz.majksa.commons.majava.di.SimpleContainer;
import cz.majksa.commons.majava.modules.Modules;
import cz.majksa.commons.majava.modules.ModulesStarter;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Map;

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
    private final boolean debug;
    @Nonnull
    private final Container container;
    @Nonnull
    private final Modules modules = new Modules();
    @Nonnull
    private final ModulesStarter modulesStarter;
    @Nullable
    private final URI tmp;

    public ApplicationContext(@Nonnull String name, boolean debug, @Nonnull Container container, @Nullable URI tmp) {
        this.name = name;
        this.debug = debug;
        this.container = container;
        this.modulesStarter = new ModulesStarter(modules);
        this.tmp = tmp;
    }

    /**
     * Creates {@link cz.majksa.commons.majava.context.ApplicationContext} from {@link cz.majksa.commons.majava.context.config.ApplicationConfig}
     *
     * @param config the application config
     * @return {@link cz.majksa.commons.majava.context.ApplicationContext}
     */
    public static ApplicationContext from(@Nonnull ApplicationConfig config) {
        return new ApplicationContext(
                config.getName(),
                config.isDebug(),
                createContainer(config),
                config.getTmp()
        ).loadModules(config);
    }

    /**
     * Loads or creates a simple container from the config
     *
     * @param config the application config
     * @return the container
     */
    @Nonnull
    private static Container createContainer(@Nonnull ApplicationConfig config) {
        if (config.getDi() == null) {
            return new SimpleContainer();
        } else {
            try {
                return (Container) config.getDi().invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * Loads the modules from the config
     *
     * @param config the application config
     * @return {@link cz.majksa.commons.majava.context.ApplicationContext}
     */
    @Nonnull
    private ApplicationContext loadModules(@Nonnull ApplicationConfig config) {
        final Map<String, JsonNode> configs = config.getModuleConfigs();
        config.getModules().forEach((name, clazz) -> modules.add(modules.create(this, clazz, configs.get(name))));
        return this;
    }

}
