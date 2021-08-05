/*
 *  majava - tech.majava.application.commands.Debug
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

package tech.majava.application.commands;

import tech.majava.application.Application;
import tech.majava.cli.commands.CliCommand;
import tech.majava.cli.commands.CommandsGroup;
import tech.majava.cli.ConsoleModifiers;
import tech.majava.cli.commands.ConsoleRuntimeException;
import tech.majava.logging.LoggingModule;
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
    private final LoggingModule logging;

    public Debug(@Nonnull CommandsGroup group, @Nonnull Application application) {
        super(group,"debug", "toggles debug");
        logging = application.getModules().get(LoggingModule.class);
        options.addOption("s", "show", false, "shows the current value");
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        if (!commandLine.hasOption('s')) {
            logging.toggleDebug();
            consoleMessenger
                    .append("Debug has been toggled.")
                    .print();
        }
        if (logging.isDebug()) {
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