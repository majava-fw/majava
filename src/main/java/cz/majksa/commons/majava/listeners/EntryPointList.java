/*
 *  majbot - cz.majksa.majbot.listeners.EntryPointList
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

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.listeners.EntryPointList}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class EntryPointList<T> {

    private final @NonNull List<EntryPoint<T>> entryPoints = new ArrayList<>();

    /**
     * Creates a new listener
     *
     * @param clazz     the class of the event
     * @param callback  the callback of the listener
     * @param predicate the condition of the listener
     * @return the created listener
     */
    public @NonNull EntryPoint<T> prepare(@NonNull Class<T> clazz, @NonNull Function<T, CompletableFuture<Void>> callback, @NonNull Predicate<T> predicate) {
        return new EntryPoint<>(clazz, callback, predicate, this);
    }

    /**
     * Adds a new listener
     *
     * @param entryPoint the listener
     * @return the listener
     */
    public @NonNull EntryPoint<T> register(@NonNull EntryPoint<T> entryPoint) {
        entryPoints.add(entryPoint);
        return entryPoint;
    }

    /**
     * Removes the listener
     *
     * @param entryPoint the listener
     */
    public void unregister(@NonNull EntryPoint<T> entryPoint) {
        entryPoints.remove(entryPoint);
    }

    /**
     * Runs the event in all registered listeners
     *
     * @param event the event to be run
     */
    public @NonNull List<CompletableFuture<Void>> run(@NonNull T event) {
        return entryPoints.stream()
                        .filter(entryPoint -> entryPoint.getFilter().test(event))
                        .map(entryPoint -> entryPoint.getCallback().apply(event))
                        .collect(Collectors.toList());
    }

}
