/*
 *  majava - cz.majksa.commons.majava.utils.StringUtils
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

package cz.majksa.commons.majava.utils;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.utils.StringUtils}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringUtils {

    /**
     * Repeats the string provided amount of times
     *
     * @param text   the text to be repeated
     * @param amount the amount of repetitions
     * @return the repeated string
     */
    @Nonnull
    public static String repeat(@Nonnull String text, int amount) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(text);
        }
        return builder.toString();
    }

}
