/*
 *  majava - tech.majava.modules.Modules
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

package tech.majava.modules;

import com.fasterxml.jackson.databind.JsonNode;
import tech.majava.context.ApplicationContext;
import tech.majava.context.config.ConfigNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link Modules}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
public final class Modules {

    @Nonnull
    private final Map<Class<? extends Module<? extends ModuleConfig>>, Module<? extends ModuleConfig>> map = new HashMap<>();

    /**
     * Constructor
     *
     * @param modules the initial modules
     */
    public Modules(@Nonnull List<Module<? extends ModuleConfig>> modules) {
        modules.forEach(this::add);
    }

    /**
     * Add a modules to the module list
     *
     * @param module the module to be added
     */
    public void add(@Nonnull Module<? extends ModuleConfig> module) {
        map.put(module.getModuleClass(), module);
    }

    /**
     * Create a new module and add it
     *
     * @param context the application context
     * @param clazz   the module class
     * @param node    the module config
     * @return the created module
     */
    @Nonnull
    public Module<? extends ModuleConfig> create(@Nonnull ApplicationContext context, @Nonnull Class<? extends Module<? extends ModuleConfig>> clazz, @Nullable JsonNode node) {
        final List<Constructor<?>> constructors = Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 2)
                .filter(constructor -> ModuleConfig.class.isAssignableFrom(constructor.getParameterTypes()[0]))
                .filter(constructor -> ApplicationContext.class.equals(constructor.getParameterTypes()[1]))
                .collect(Collectors.toList());
        if (constructors.isEmpty()) {
            throw new IllegalArgumentException(String.format("There must be a public constructor with 2 arguments: one that extends %s and second one being %s in the module class: %s", ModuleConfig.class, ApplicationContext.class, clazz));
        }
        final Constructor<?> constructor = constructors.get(0);
        final Class<?> configClass = constructor.getParameterTypes()[0];
        try {
            final Object config = configClass.getConstructor(ConfigNode.class).newInstance(new ConfigNode(node));
            final Module<? extends ModuleConfig> module = (Module<? extends ModuleConfig>) constructor.newInstance(config, context);
            context.getContainer().register(module);
            return module;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("There must be a public constructor with a single argument %s in the config class: %s", ConfigNode.class, configClass));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Get a module from the list
     *
     * @param module the module class
     * @param <M>    the module type
     * @return the module
     */
    @SuppressWarnings("unchecked")
    public <M extends Module<? extends ModuleConfig>> M get(@Nonnull Class<M> module) {
        return (M) map.get(module);
    }

}
