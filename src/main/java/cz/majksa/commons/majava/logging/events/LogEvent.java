/*
 *  majava - cz.majksa.commons.majava.logging.events.LogEvent
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation;
either version 3 of the License;
or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not;
see <https://www.gnu.org/licenses/>.
 */

package cz.majksa.commons.majava.logging.events;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.logging.events.LogEvent}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public interface LogEvent {

    @Nonnull
    Level getLevel();

    Marker getMarker();

    StackTraceElement getLocation();

    Message getMessage();

    Throwable getThrowable();

    static LogEvent from(@Nonnull Level level, Marker marker, StackTraceElement location, Message message, Throwable throwable) {
        if (Level.OFF.equals(level)) {
            return null;
        }
        if (Level.DEBUG.equals(level)) {
            return new DebugLogEvent(marker, location, message, throwable);
        }
        if (Level.TRACE.equals(level)) {
            return new TraceLogEvent(marker, location, message, throwable);
        }
        if (Level.INFO.equals(level)) {
            return new InfoLogEvent(marker, location, message, throwable);
        }
        if (Level.WARN.equals(level)) {
            return new WarnLogEvent(marker, location, message, throwable);
        }
        if (Level.ERROR.equals(level)) {
            return new ErrorLogEvent(marker, location, message, throwable);
        }
        if (Level.FATAL.equals(level)) {
            return new FatalLogEvent(marker, location, message, throwable);
        }
        if (Level.ALL.equals(level)) {
            return new AlwaysLogEvent(marker, location, message, throwable);
        }
        return new CustomLogEvent(level, marker, location, message, throwable);
    }

}
