/*
 *  majava - cz.majksa.commons.majava.application.commands.Restart
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
import cz.majksa.commons.majava.cli.CliApplication;
import cz.majksa.commons.majava.cli.commands.CliCommand;
import cz.majksa.commons.majava.cli.commands.CommandsGroup;
import cz.majksa.commons.majava.cli.ConsoleModifiers;
import cz.majksa.commons.majava.cli.commands.ConsoleRuntimeException;
import org.apache.commons.cli.CommandLine;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.application.commands.Start}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Start extends CliCommand {

    @Nonnull
    private final Application application;
    @Nonnull
    private final CliApplication cliApplication;

    public Start(@Nonnull CommandsGroup group, @Nonnull Application application, @Nonnull CliApplication cliApplication) {
        super(group,"start", "starts the application");
        options.addOption("l", "listen", false, "attaches a listener for commands");
        this.application = application;
        this.cliApplication = cliApplication;
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        try {
            application.start();
            consoleMessenger
                    .modify(ConsoleModifiers.GREEN)
                    .append("Successfully started the application.")
                    .print();
        } catch (IllegalStateException e) {
            throw new ConsoleRuntimeException(e.getMessage());
        }
        if (commandLine.hasOption('l')) {
            cliApplication.listen();
        }
    }

}
