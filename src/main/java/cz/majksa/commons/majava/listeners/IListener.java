/*
 *  majbot - cz.majksa.majbot.listeners.AbstractListener
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

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.listeners.IListener}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IListener<T> {

    @Nonnull Class<T> getEventClass();

    /**
     * Runs the event.
     *
     * @param event the actual event {@link T}
     */
    @Nonnull
    CompletableFuture<Void> run(@Nonnull T event);

    /**
     * Checks if the event is the one we want to run.
     *
     * @param event the actual event {@link T}
     * @return <code>true</code> if this event is the one we want
     */
    default boolean check(@Nonnull T event) {
        return true;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    default Class<? extends IListener<T>> getGenericClass() {
        return (Class<? extends IListener<T>>) getClass();
    }

}
