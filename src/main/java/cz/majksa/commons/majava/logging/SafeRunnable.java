/*
 *  majava - cz.majksa.commons.majava.logging.SafeRunnable
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

package cz.majksa.commons.majava.logging;

/**
 * <p><b>Interface {@link cz.majksa.commons.majava.logging.SafeRunnable}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface SafeRunnable<T extends Throwable> extends SafeConsumer<Void, T> {

    /**
     * The wrapper method for {@link #execute()}
     *
     * @param param unused param
     * @throws T the exception thrown
     */
    @Override
    default void execute(Void param) throws T {
        execute();
    }

    /**
     * The main method that may throw a throwable
     *
     * @throws T the exception thrown
     */
    void execute() throws T;

}
