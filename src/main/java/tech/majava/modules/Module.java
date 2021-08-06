/*
 *  majava - tech.majava.modules.Module
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

import tech.majava.cli.commands.CommandsGroup;
import tech.majava.context.ApplicationContext;
import lombok.Getter;
import tech.majava.context.config.Config;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link Module}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public abstract class Module<C extends Config> {

    protected final List<Class<? extends Module<? extends Config>>> dependencies = new ArrayList<>();

    @Nonnull
    protected final C config;

    @Nonnull
    protected final String name;

    protected final String description;

    protected final CommandsGroup cliCommands;

    @Nonnull
    protected final ApplicationContext context;

    /**
     * If module has been started
     */
    protected boolean running;

    protected CompletableFuture<Void> future;

    /**
     * Constructor
     *
     * @param config      module config
     * @param context     application context
     * @param name        module name
     * @param description module description
     */
    public Module(@Nonnull C config, @Nonnull ApplicationContext context, @Nonnull String name, @Nonnull String description) {
        this.config = config;
        this.context = context;
        this.name = name;
        this.description = description;
        cliCommands = new CommandsGroup(name, description);
    }

    /**
     * Get the cast class of the module
     *
     * @return the class
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Module<C>> getModuleClass() {
        return (Class<? extends Module<C>>) getClass();
    }

    /**
     * Stars the module
     */
    @Nonnull
    public final CompletableFuture<Void> start() {
        if (running) {
            return future;
        }
        running = true;
        future = onStart();
        return future;
    }

    /**
     * Stops the module
     */
    @Nonnull
    public final CompletableFuture<Void> shutdown() {
        if (!running) {
            return future;
        }
        running = false;
        future = onShutdown();
        return future;
    }

    /**
     * Method executed on module start
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {});
    }

    /**
     * Method executed on module shutdown
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> {});
    }

}
