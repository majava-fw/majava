/*
 *  majava - tech.majava.listeners.eventhandlers.AbstractEventsHandler
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

package tech.majava.listeners.eventhandlers;

import tech.majava.listeners.EntryPointList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link AbstractEventsHandler}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventsHandler<E> implements EventsHandler<E> {

    @Nonnull
    protected final Class<E> rootEvent;

    @Nonnull
    protected final Function<Throwable, Void> loggingFunction;

    private boolean running = false;

    /**
     * The entry points are registered here.
     *
     * @see tech.majava.listeners.EntryPointList
     */
    @Nonnull
    protected final Map<Class<? extends E>, EntryPointList<? extends E>> entryPointsMap = new HashMap<>();

    @Override
    public void start() {
        if (running) {
            throw new IllegalStateException("Event handler " + getClass().getName() + " is already running!");
        }
        running = true;
        onStart().join();
    }

    @Override
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Event handler " + getClass().getName() + " is not running yet!");
        }
        running = false;
        onStop().join();
    }

    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.completedFuture(null);
    }

    protected CompletableFuture<Void> onStop() {
        return CompletableFuture.completedFuture(null);
    }

}
