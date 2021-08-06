/*
 *  majava - tech.majava.logging.commands.Open
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

package tech.majava.logging.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import tech.majava.cli.ConsoleModifiers;
import tech.majava.cli.ConsoleTextBuilder;
import tech.majava.cli.commands.CliCommand;
import tech.majava.cli.commands.CommandsGroup;
import tech.majava.cli.commands.ConsoleRuntimeException;
import tech.majava.logging.errors.ErrorsSaver;
import tech.majava.logging.errors.ThrowableNotFoundException;
import tech.majava.serializable.SerializableThrowable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link tech.majava.logging.commands.Delete}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Delete extends CliCommand {

    @Nonnull
    private final ErrorsSaver errors;

    public Delete(@Nonnull ErrorsSaver errors, @Nullable CommandsGroup group) {
        super(group, "delete", "deletes the given error by ID");
        this.errors = errors;
        options
                .addOption(
                        Option.builder("i")
                                .longOpt("id")
                                .desc("the ID of the file to delete")
                                .hasArg()
                                .argName("id")
                                .build()
                )
                .addOption("a", "all", false, "delete all errors");
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        if (commandLine.hasOption('a')) {
            final int size = errors.ids().size();
            errors.clear();
            consoleMessenger
                    .modify(ConsoleModifiers.GREEN)
                    .append("Successfully deleted ")
                    .modify(ConsoleModifiers.BOLD)
                    .modify(ConsoleModifiers.UNDERLINE)
                    .append(Integer.toString(size))
                    .reset()
                    .modify(ConsoleModifiers.GREEN)
                    .append(" all errors!")
                    .print();
            return;
        }
        if (!commandLine.hasOption('i')) {
            throw new ConsoleRuntimeException("Please select an ID!");
        }
        final String id = commandLine.getOptionValue('i');
        try {
            errors.delete(id);
            consoleMessenger.modify(ConsoleModifiers.GREEN).append("Successfully deleted error with ID").append(id).print();
        } catch (ThrowableNotFoundException e) {
            throw new ConsoleRuntimeException(e.getMessage());
        }
    }

}
