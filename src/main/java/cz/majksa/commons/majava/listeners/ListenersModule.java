/*
 *  majava - cz.majksa.commons.majava.listeners.ListenersModule
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

package cz.majksa.commons.majava.listeners;

import cz.majksa.commons.majava.context.ApplicationContext;
import cz.majksa.commons.majava.listeners.eventhandlers.EventsHandler;
import cz.majksa.commons.majava.modules.Module;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.listeners.ListenersModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ListenersModule extends Module<ListenersConfig> {

    private final Map<Class<?>, EventsHandler<?>> handlers = new HashMap<>();

    public ListenersModule(@Nonnull ListenersConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "listeners", "listeners modules");
        config.getHandlers().forEach(this::registerHandler);
    }

    private void registerHandler(@NonNull Class<?> clazz) {
        EventsHandler<?> handler;
        try {
            handler = (EventsHandler<?>) context.getContainer().get(clazz);
        } catch (NullPointerException e) {
            try {
                handler = (EventsHandler<?>) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new NullPointerException(clazz.getName());
            }
        }
        handlers.put(clazz, handler);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> handlers.values().forEach(EventsHandler::start));
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> handlers.values().forEach(EventsHandler::start));
    }

    @Nonnull
    public <T> EntryPoint<T> prepare(@Nonnull Class<T> clazz, @Nonnull Function<T, CompletableFuture<Void>> callback, @Nonnull Predicate<T> predicate) {
        return searchHandler(clazz).prepare(clazz, callback, predicate);
    }

    @SuppressWarnings("unchecked")
    public <T> @NonNull EventsHandler<T> getHandler(@NonNull Class<T> clazz) {
        return (EventsHandler<T>) handlers.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> @NonNull EventsHandler<T> searchHandler(@NonNull Class<?> clazz) {
        return handlers
                .values()
                .stream()
                .filter(eventsHandler -> eventsHandler.getRootEvent().isAssignableFrom(clazz))
                .map(eventsHandler -> (EventsHandler<T>) eventsHandler)
                .findAny()
                .orElseThrow(NullPointerException::new);
    }

    public <T> @NonNull ListenersModule registerHandler(@NonNull Class<T> clazz, @NonNull EventsHandler<T> handler) {
        handlers.put(clazz, handler);
        if (running) {
            handler.start();
        }
        return this;
    }

    public <T> @NonNull ListenersModule unregisterHandler(@NonNull Class<T> clazz) {
        final EventsHandler<?> handler = handlers.remove(clazz);
        if (running) {
            handler.stop();
        }
        return this;
    }

}
