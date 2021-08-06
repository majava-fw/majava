/*
 *  majava - tech.majava.modules.ModulesStarter
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

import tech.majava.context.config.Config;
import tech.majava.logging.LoggingModule;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link ModulesStarter}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ModulesStarter {

    @Nonnull
    private final Map<Class<? extends Module<? extends Config>>, Module<? extends Config>> modules;
    private final Map<Class<? extends Module<? extends Config>>, List<Class<? extends Module<? extends Config>>>> modulesDependencies = new HashMap<>();
    private final Function<Throwable, Void> logFunction;

    /**
     * Constructor
     *
     * @param modules the modules to be started
     */
    public ModulesStarter(@Nonnull Modules modules) {
        this.modules = modules.getMap();
        logFunction = modules.get(LoggingModule.class).getLogFunction();
    }

    /**
     * Starts all modules
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    public CompletableFuture<Void> start() {
        return CompletableFuture.allOf(
                modules.values()
                        .stream()
                        .map(this::start)
                        .toArray(CompletableFuture[]::new)
        );
    }

    /**
     * Shuts down all modules
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.allOf(
                modules.values()
                        .stream()
                        .map(this::shutdown)
                        .toArray(CompletableFuture[]::new)
        );
    }

    /**
     * Starts the provided module
     *
     * @param module the module class
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    public CompletableFuture<Void> start(Class<? extends Module<? extends Config>> module) {
        return start(modules.get(module));
    }


    /**
     * Shuts down the provided module
     *
     * @param module the module class
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    public CompletableFuture<Void> shutdown(Class<? extends Module<? extends Config>> module) {
        return shutdown(modules.get(module));
    }

    /**
     * Starts the module and it's dependencies
     *
     * @param module the module to be started
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    private CompletableFuture<Void> start(@Nonnull Module<? extends Config> module) {
        if (module.isRunning()) {
            return module.getFuture();
        }
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        final List<Class<? extends Module<? extends Config>>> dependencies = module.getDependencies();
        for (Class<? extends Module<? extends Config>> dependencyClass : dependencies) {
            modulesDependencies.computeIfAbsent(dependencyClass, clazz -> new ArrayList<>());
            modulesDependencies.get(dependencyClass).add(module.getModuleClass());
            final Module<? extends Config> dependency = modules.get(dependencyClass);
            if (dependency == null) {
                throw new IllegalArgumentException("Module " + module.name + " depends on " + dependencyClass.getName() + ", which has not been registered!");
            }
            futures.add(start(dependency));
        }
        futures.forEach(CompletableFuture::join);
        return module.start().exceptionally(logFunction);
    }

    /**
     * Shuts down the module and modules it depends on
     *
     * @param module the module to be started
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    private CompletableFuture<Void> shutdown(@Nonnull Module<? extends Config> module) {
        if (!module.isRunning()) {
            return module.getFuture();
        }
        modulesDependencies.getOrDefault(module.getModuleClass(), Collections.emptyList())
                .stream()
                .map(modules::get)
                .map(this::shutdown)
                .forEach(CompletableFuture::join);
        return module.shutdown().exceptionally(logFunction);
    }

}
