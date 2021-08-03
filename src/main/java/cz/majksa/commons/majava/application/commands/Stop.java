/*
 *  majava - cz.majksa.commons.majava.application.commands.Exit
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
 * <p><b>Class {@link cz.majksa.commons.majava.application.commands.Stop}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Stop extends CliCommand {

    @Nonnull
    private final Application application;

    public Stop(@Nonnull CommandsGroup group, @Nonnull Application application) {
        super(group,"stop", "stops the application");
        this.application = application;
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        try {
            application.exit();
            consoleMessenger
                    .modify(ConsoleModifiers.RED)
                    .modify(ConsoleModifiers.BOLD)
                    .append("Successfully stopped the application")
                    .print();
        } catch (IllegalStateException e) {
            throw new ConsoleRuntimeException(e.getMessage());
        }
    }
}
