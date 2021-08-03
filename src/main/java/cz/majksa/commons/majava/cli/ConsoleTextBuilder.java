/*
 *  majava - cz.majksa.commons.majava.cli.ConsoleTextBuilder
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

package cz.majksa.commons.majava.cli;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.cli.ConsoleTextBuilder}</b></p>
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

    @Nonnull
    public ConsoleTextBuilder modify(@Nonnull ConsoleModifiers color) {
        builder.append(color);
        return this;
    }

    @Nonnull
    public ConsoleTextBuilder reset() {
        return modify(ConsoleModifiers.RESET);
    }

    @Nonnull
    public ConsoleTextBuilder newLine() {
        return newLine(true);
    }

    @Nonnull
    public ConsoleTextBuilder newLine(boolean reset) {
        if (reset) {
            reset();
        }
        builder.append("\n");
        width = 0;
        return this;
    }

    @Nonnull
    public String build() {
        newLine();
        return builder.toString();
    }

    public void print() {
        System.out.print(build());
        clear();
    }

    public void clear() {
        builder.setLength(0);
        width = 0;
    }

}
