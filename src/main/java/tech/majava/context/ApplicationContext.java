/*
 *  majava - tech.majava.context.ApplicationContext
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

package tech.majava.context;

import com.fasterxml.jackson.databind.JsonNode;
import tech.majava.context.config.ApplicationConfig;
import tech.majava.di.Container;
import tech.majava.di.SimpleContainer;
import tech.majava.modules.Modules;
import tech.majava.modules.ModulesStarter;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Map;

/**
 * <p><b>Class {@link ApplicationContext}</b></p>
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
    @Nonnull
    private final Modules modules;
    @Nonnull
    private final ModulesStarter modulesStarter;
    @Nullable
    private final URI tmp;

    /**
     * Constructor
     *
     * @param config the config to be parsed
     */
    public ApplicationContext(@Nonnull ApplicationConfig config) {
        this.name = config.getName();
        this.container = createContainer(config);
        this.modules = loadModules(config);
        this.modulesStarter = new ModulesStarter(modules);
        this.tmp = config.getTmp();
    }

    /**
     * Loads or creates a simple container from the config
     *
     * @param config the application config
     * @return the container
     */
    @Nonnull
    private Container createContainer(@Nonnull ApplicationConfig config) {
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
     * @return {@link ApplicationContext}
     */
    @Nonnull
    private Modules loadModules(@Nonnull ApplicationConfig config) {
        final Map<String, JsonNode> configs = config.getModuleConfigs();
        final Modules modules = new Modules();
        config.getModules().forEach((name, clazz) -> modules.add(modules.create(this, clazz, configs.get(name))));
        return modules;
    }

}
