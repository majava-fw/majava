/*
 *  majava - tech.majava.cli.ConsoleTextBuilder
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

package tech.majava.cli;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link ConsoleTextBuilder}</b></p>
 * A fancy way to print modified text to console
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ConsoleTextBuilder {

    public static final int WIDTH = 100;

    @Nonnull
    private final StringBuilder builder = new StringBuilder();

    private int width = 0;

    /**
     * Appends a text to the builder
     *
     * @param csq the text to be appended
     * @return {@link ConsoleModifiers}
     */
    @Nonnull
    public ConsoleTextBuilder append(CharSequence csq) {
        if (width + csq.length() > WIDTH) {
            final int diff = WIDTH - width;
            append(csq.subSequence(0, diff));
            builder.append("\n");
            if (diff < csq.length() - 1) {
                builder.append(csq, diff, csq.length());
            }
            width = csq.length() - diff;
        } else {
            builder.append(csq);
            width += csq.length();
        }
        return this;
    }

    /**
     * Apply a modification to the following text
     *
     * @param modification the modification
     * @return {@link ConsoleModifiers}
     */
    @Nonnull
    public ConsoleTextBuilder modify(@Nonnull ConsoleModifiers modification) {
        builder.append(modification);
        return this;
    }

    /**
     * Reset all modifications of the following text
     *
     * @return {@link ConsoleModifiers}
     */
    @Nonnull
    public ConsoleTextBuilder reset() {
        return modify(ConsoleModifiers.RESET);
    }

    /**
     * Adds a new line and resets
     *
     * @return {@link ConsoleModifiers}
     */
    @Nonnull
    public ConsoleTextBuilder newLine() {
        return newLine(true);
    }

    /**
     * Adds a new line and resets if <code>reset</code> is true
     *
     * @param reset if the modifications should be reset
     * @return {@link ConsoleModifiers}
     */
    @Nonnull
    public ConsoleTextBuilder newLine(boolean reset) {
        if (reset) {
            reset();
        }
        builder.append("\n");
        width = 0;
        return this;
    }

    /**
     * Builds a raw text
     *
     * @return the raw text
     */
    @Nonnull
    public String build() {
        newLine();
        return builder.toString();
    }

    /**
     * Prints the text to console
     */
    public void print() {
        System.out.print(build());
        clear();
    }

    /**
     * Clears the builder
     */
    public void clear() {
        builder.setLength(0);
        width = 0;
    }

}
