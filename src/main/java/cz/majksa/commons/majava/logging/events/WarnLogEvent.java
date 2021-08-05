/*
 *  majava - cz.majksa.commons.majava.logging.events.DebugLogEvent
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

package cz.majksa.commons.majava.logging.events;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.logging.events.WarnLogEvent}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class WarnLogEvent extends AbstractLogEvent {

    public WarnLogEvent(Marker marker, StackTraceElement location, Message message, Throwable throwable) {
        super(Level.WARN, marker, location, message, throwable);
    }

}
