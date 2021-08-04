/*
 *  majava - cz.majksa.commons.majava.listeners.eventhandlers.AbstractEventsHandler
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

package cz.majksa.commons.majava.listeners.eventhandlers;

import cz.majksa.commons.majava.listeners.EntryPointList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * The entry points are registered here.
     *
     * @see cz.majksa.commons.majava.listeners.EntryPointList
     */
    @Nonnull
    protected final Map<Class<? extends E>, EntryPointList<? extends E>> entryPointsMap = new HashMap<>();

}
