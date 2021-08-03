/*
 *  majava - cz.majksa.commons.majava.application.commands.Debug
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

package cz.majksa.commons.majava.application.commands;

import cz.majksa.commons.majava.application.Application;
import cz.majksa.commons.majava.cli.commands.CliCommand;
import cz.majksa.commons.majava.cli.commands.CommandsGroup;
import cz.majksa.commons.majava.cli.ConsoleModifiers;
import cz.majksa.commons.majava.cli.commands.ConsoleRuntimeException;
import org.apache.commons.cli.CommandLine;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link Debug}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Debug extends CliCommand {

    @Nonnull
    private final Application application;

    public Debug(@Nonnull CommandsGroup group, @Nonnull Application application) {
        super(group,"debug", "toggles debug");
        this.application = application;
        options.addOption("s", "show", false, "shows the current value");
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        if (!commandLine.hasOption('s')) {
            application.toggleDebug();
            consoleMessenger
                    .append("Debug has been toggled.")
                    .print();
        }
        if (application.isDebug()) {
            consoleMessenger
                    .modify(ConsoleModifiers.GREEN)
                    .append("Debug is enabled.")
                    .print();
        } else {
            consoleMessenger
                    .modify(ConsoleModifiers.RED)
                    .append("Debug is disabled.")
                    .print();
        }
    }

}