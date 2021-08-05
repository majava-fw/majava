/*
 *  majava - tech.majava.logging.SafeConsumer
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

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * <p><b>Interface {@link SafeConsumer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface SafeConsumer<P, T extends Throwable> extends Function<P, T> {

    /**
     * The main method that may throw a throwable
     *
     * @throws T the exception thrown
     */
    void execute(P param) throws T;

    /**
     * Executes the function
     *
     * @param p the argument
     * @return the throwable, null if ended without throwing a throwable
     */
    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    default T apply(P p) {
        try {
            execute(p);
            return null;
        } catch (Throwable t) {
            return (T) t;
        }
    }

}
