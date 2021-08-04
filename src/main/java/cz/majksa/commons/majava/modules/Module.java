/*
 *  majava - cz.majksa.commons.majava.modules.Module
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

package cz.majksa.commons.majava.modules;

import cz.majksa.commons.majava.cli.commands.CommandsGroup;
import cz.majksa.commons.majava.context.ApplicationContext;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.modules.Module}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public abstract class Module<C extends ModuleConfig> {

    protected final List<Class<? extends Module<? extends ModuleConfig>>> dependencies = new ArrayList<>();

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

    public Module(@Nonnull C config, @Nonnull ApplicationContext context, @Nonnull String name, @Nonnull String description) {
        this.config = config;
        this.context = context;
        this.name = name;
        this.description = description;
        cliCommands = new CommandsGroup(name, description);
    }

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

    @Nonnull
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {});
    }

    @Nonnull
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> {});
    }

}
