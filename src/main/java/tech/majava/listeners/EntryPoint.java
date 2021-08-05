/*
 *  majava - tech.majava.listeners.EntryPoint
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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p><b>Class {@link EntryPoint}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public class EntryPoint<T> {

    /**
     * The {@link Class} of the event
     */
    private final @NonNull Class<T> type;
    /**
     * The {@link java.util.function.Consumer} for the listener
     */
    private final @NonNull Function<T, CompletableFuture<Void>> callback;
    /**
     * The {@link java.util.function.Predicate} for the listener
     */
    private final @NonNull Predicate<T> filter;
    /**
     * The {@link EntryPointList} to {@link #register()} in and {@link #unregister()} from.
     */
    private final @NonNull EntryPointList<T> list;

    /**
     * Register the listener in {@link #list}.
     *
     * @see EntryPointList#register(EntryPoint)
     */
    public void register() {
        list.register(this);
    }

    /**
     * Unregister the listener from {@link #list}.
     *
     * @see EntryPointList#unregister(EntryPoint)
     */
    public void unregister() {
        list.unregister(this);
    }


}
