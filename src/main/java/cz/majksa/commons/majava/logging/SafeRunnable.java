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

import cz.majksa.commons.majava.Application;

import javax.annotation.Nullable;

/**
 * <p><b>Interface {@link cz.majksa.commons.majava.logging.SafeRunnable}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface SafeRunnable extends Runnable {

    /**
     * The method for running {@link #execute()} safely, which instead of throwing exceptions, it just logs it
     */
    @Override
    default void run() {
        final Throwable throwable = runAndReturn();
        if (throwable != null) {
            Application.log(throwable);
        }
    }

    /**
     * The main method that may throw a throwable
     *
     * @throws Throwable the throwable thrown
     */
    void execute() throws Throwable;

    /**
     * The method for running {@link #run()} safely, which instead of throwing exceptions, it just returns it
     *
     * @return the throwable thrown
     */
    @Nullable
    default Throwable runAndReturn() {
        try {
            run();
            return null;
        } catch (Throwable throwable) {
            return throwable;
        }
    }

}
