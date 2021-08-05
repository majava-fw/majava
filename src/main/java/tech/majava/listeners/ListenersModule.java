/*
 *  majava - tech.majava.listeners.ListenersModule
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

package tech.majava.listeners;

import tech.majava.context.ApplicationContext;
import tech.majava.listeners.eventhandlers.EventsHandler;
import tech.majava.modules.Module;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p><b>Class {@link ListenersModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ListenersModule extends Module<ListenersConfig> {

    private final Map<Class<?>, EventsHandler<?>> handlers = new HashMap<>();

    /**
     * Constructor
     *
     * @param config  the listeners config
     * @param context the application context
     */
    public ListenersModule(@Nonnull ListenersConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "listeners", "listeners modules");
        config.getHandlers().forEach(this::registerHandler);
    }

    /**
     * Creates and registers a handler
     *
     * @param clazz the class of the handler
     */
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
        handlers.put(handler.getRootEvent(), handler);
    }

    /**
     * Starts all handlers
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> handlers.values().forEach(EventsHandler::start));
    }

    /**
     * Shuts down all handlers
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> handlers.values().forEach(EventsHandler::start));
    }

    /**
     * Creates a new listener
     *
     * @param clazz     the class of the event
     * @param callback  the callback of the listener
     * @param predicate the condition of the listener
     * @return {@link EntryPoint}
     */
    @Nonnull
    public <T> EntryPoint<T> prepare(@Nonnull Class<T> clazz, @Nonnull Function<T, CompletableFuture<Void>> callback, @Nonnull Predicate<T> predicate) {
        return searchHandler(clazz).prepare(clazz, callback, predicate);
    }

    /**
     * Gets the event handler by the root event class
     *
     * @param clazz the root event class
     * @param <T>   the root event type
     * @return {@link tech.majava.listeners.eventhandlers.EventsHandler}
     */
    @SuppressWarnings("unchecked")
    public <T> @NonNull EventsHandler<T> getHandler(@NonNull Class<T> clazz) {
        return (EventsHandler<T>) handlers.get(clazz);
    }

    /**
     * Searches for the event handler by an event class
     *
     * @param clazz the event class
     * @param <T>   the root event type
     * @return {@link tech.majava.listeners.eventhandlers.EventsHandler}
     */
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


    /**
     * Registers the event handler
     *
     * @param clazz   the root event class
     * @param handler the handler itself
     * @param <T>     the root event type
     * @return {@link ListenersModule}
     */
    public <T> @NonNull ListenersModule registerHandler(@NonNull Class<T> clazz, @NonNull EventsHandler<T> handler) {
        handlers.put(clazz, handler);
        if (running) {
            handler.start();
        }
        return this;
    }

    /**
     * Unregisters the event handler
     *
     * @param clazz the root event class
     * @param <T>   the root event type
     * @return {@link ListenersModule}
     */
    public <T> @NonNull ListenersModule unregisterHandler(@NonNull Class<T> clazz) {
        final EventsHandler<?> handler = handlers.remove(clazz);
        if (running) {
            handler.stop();
        }
        return this;
    }

    /**
     * Loads a listener class and converts it into an {@link EntryPoint}
     *
     * @param listener the listener to be loaded
     * @param <T>      the event type the listener listens to
     * @return the created {@link EntryPoint}
     */
    @Nonnull
    public <T> EntryPoint<T> loadListener(@Nonnull IListener<T> listener) {
        return prepare(listener.getEventClass(), listener::run, listener::check);
    }

}
