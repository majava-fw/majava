/*
 *  majava - tech.majava.cli.commands.ConsoleRuntimeException
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

package tech.majava.cli.commands;

/**
 * <p><b>Exception {@link ConsoleRuntimeException}</b></p>
 * Thrown inside console commands and the message is then printed to the user.
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConsoleRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5744716065601064960L;

    public ConsoleRuntimeException(String message) {
        super(message);
    }

    public ConsoleRuntimeException(Throwable cause) {
        super(cause);
    }

}
