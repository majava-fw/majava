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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tech.majava.context.ApplicationContext;
import tech.majava.context.config.Config;
import tech.majava.context.config.ConfigReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
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
@RequiredArgsConstructor
public final class Modules {

    @Nonnull
    private final Map<Class<? extends Module<? extends Config>>, Module<? extends Config>> map = new HashMap<>();
    private final ApplicationContext context;

    /**
     * Constructor
     *
     * @param modules the initial modules
     */
    public Modules(@Nonnull List<Module<? extends Config>> modules, @Nonnull ApplicationContext context) {
        this(context);
        modules.forEach(this::add);
    }

    /**
     * Add a modules to the module list
     *
     * @param module the module to be added
     */
    public void add(@Nonnull Module<? extends Config> module) {
        map.put(module.getModuleClass(), module);
    }

    /**
     * Create a new module and add it
     *
     * @param clazz   the module class
     * @param rawConfig    the module config
     * @return the created module
     */
    @Nonnull
    public Module<?> create(@Nonnull Class<?> clazz, @Nullable String rawConfig) {
        final Constructor<?> constructor = getConstructor(clazz);
        final Object config = createConfig(constructor.getParameterTypes()[0], rawConfig);
        final Module<?> module = createModule(constructor, config);
        context.getContainer().register(module);
        return module;
    }

    @SneakyThrows
    private Object createConfig(@Nonnull Class<?> configClass, @Nullable String raw) {
        if (raw == null) {
            return configClass.newInstance();
        }
        return ConfigReader.mapper.readValue(raw, configClass);
    }

    private Constructor<?> getConstructor(@Nonnull Class<?> moduleClass) {
        final List<Constructor<?>> constructors = Arrays.stream(moduleClass.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 2)
                .filter(constructor -> Config.class.isAssignableFrom(constructor.getParameterTypes()[0]))
                .filter(constructor -> ApplicationContext.class.equals(constructor.getParameterTypes()[1]))
                .collect(Collectors.toList());
        if (constructors.isEmpty()) {
            throw new IllegalArgumentException(String.format("There must be a public constructor with 2 arguments: one that extends %s and second one being %s in the module class: %s", Config.class, ApplicationContext.class, moduleClass));
        }
        return constructors.get(0);
    }

    @SneakyThrows
    private Module<?> createModule(@Nonnull Constructor<?> constructor, @Nonnull Object config) {
        return (Module<?>) constructor.newInstance(config, context);
    }

    /**
     * Get a module from the list
     *
     * @param module the module class
     * @param <M>    the module type
     * @return the module
     */
    @SuppressWarnings("unchecked")
    public <M extends Module<? extends Config>> M get(@Nonnull Class<M> module) {
        return (M) map.get(module);
    }

}
