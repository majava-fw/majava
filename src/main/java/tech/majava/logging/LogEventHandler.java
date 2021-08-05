/*
 *  majava - tech.majava.logging.LogEventHandler
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

package tech.majava.logging;

import tech.majava.listeners.eventhandlers.AbstractEventsHandler;
import tech.majava.logging.events.LogEvent;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * <p><b>Class {@link LogEventHandler}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class LogEventHandler extends AbstractEventsHandler<LogEvent> {

    public LogEventHandler(@Nonnull Function<Throwable, Void> loggingFunction) {
        super(LogEvent.class, loggingFunction);
    }

}
