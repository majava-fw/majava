/*
 *  majava - tech.majava.cli.ConsoleModifiers
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

import lombok.Getter;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * <p><b>Class {@link ConsoleModifiers}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class ConsoleModifiers {

    // Basic
    public static final ConsoleModifiers RESET = new ConsoleModifiers(0);
    public static final ConsoleModifiers BOLD = new ConsoleModifiers(1);
    public static final ConsoleModifiers FAINT = new ConsoleModifiers(2);
    public static final ConsoleModifiers ITALIC = new ConsoleModifiers(3);
    public static final ConsoleModifiers UNDERLINE = new ConsoleModifiers(4);
    public static final ConsoleModifiers SLOW_BLINK = new ConsoleModifiers(5);
    public static final ConsoleModifiers RAPID_BLINK = new ConsoleModifiers(6);
    public static final ConsoleModifiers REVERSE_VIDEO = new ConsoleModifiers(7);
    public static final ConsoleModifiers CONCEAL = new ConsoleModifiers(8);
    public static final ConsoleModifiers CROSSED_OUT = new ConsoleModifiers(9);

    // Fonts
    public static final ConsoleModifiers PRIMARY_FONT = new ConsoleModifiers(10);

    // Colors
    public static final ConsoleModifiers BLACK = new ConsoleModifiers(30);
    public static final ConsoleModifiers RED = new ConsoleModifiers(31);
    public static final ConsoleModifiers GREEN = new ConsoleModifiers(32);
    public static final ConsoleModifiers YELLOW = new ConsoleModifiers(33);
    public static final ConsoleModifiers BLUE = new ConsoleModifiers(34);
    public static final ConsoleModifiers PURPLE = new ConsoleModifiers(35);
    public static final ConsoleModifiers CYAN = new ConsoleModifiers(36);
    public static final ConsoleModifiers WHITE = new ConsoleModifiers(37);

    // Background Color
    public static final ConsoleModifiers RED_BACKGROUND = new ConsoleModifiers(41);
    public static final ConsoleModifiers GREEN_BACKGROUND = new ConsoleModifiers(42);
    public static final ConsoleModifiers YELLOW_BACKGROUND = new ConsoleModifiers(43);
    public static final ConsoleModifiers BLUE_BACKGROUND = new ConsoleModifiers(44);
    public static final ConsoleModifiers PURPLE_BACKGROUND = new ConsoleModifiers(45);
    public static final ConsoleModifiers CYAN_BACKGROUND = new ConsoleModifiers(46);

    private final String ansi;

    public ConsoleModifiers(int code) {
        this.ansi = "\u001B[" + code + "m";
    }

    public ConsoleModifiers(int a, int b) {
        this.ansi = "\u001B[" + a + ";" + b + "m";
    }

    public ConsoleModifiers(int a, int b, int c) {
        this.ansi = "\u001B[" + a + ";" + b + ";" + c + "m";
    }

    public ConsoleModifiers(Color rgb) {
        this.ansi = "\u001B[38;2;" + rgb.getRed() + ";" + rgb.getGreen() + ";" + rgb.getBlue() + "m";
    }

    /**
     * Adds an effect to the text
     *
     * @param text the text to be modified
     * @return the modified text
     */
    public String modify(@Nonnull String text) {
        return modify(text, true);
    }

    /**
     * Adds an effect to the text
     *
     * @param text  the text to be modified
     * @param reset if the effect should be reset after
     * @return the modified text
     */
    public String modify(@Nonnull String text, boolean reset) {
        return ansi + text + (reset ? RESET.ansi : "");
    }

    @Override
    public String toString() {
        return ansi;
    }

}
