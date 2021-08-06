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
import org.apache.commons.cli.OptionBuilder;
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
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link tech.majava.logging.commands.Open}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Open extends CliCommand {

    @Nonnull
    private final ErrorsSaver errors;

    public Open(@Nonnull ErrorsSaver errors, @Nullable CommandsGroup group) {
        super(group, "open", "opens the given error by ID");
        this.errors = errors;
        options.addOption(
                Option.builder("i")
                        .longOpt("id")
                        .desc("the ID of the file to open")
                        .hasArg()
                        .argName("id")
                        .build()
        );
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        if (!commandLine.hasOption('i')) {
            throw new ConsoleRuntimeException("Please select an ID!");
        }
        final String id = commandLine.getOptionValue('i');
        try {
            print(errors.get(id));
        } catch (ThrowableNotFoundException e) {
            throw new ConsoleRuntimeException(e.getMessage());
        }
    }

    public static void print(@Nonnull SerializableThrowable throwable) {
        final ConsoleTextBuilder builder = new ConsoleTextBuilder()
                .modify(ConsoleModifiers.BOLD)
                .append("Class: ")
                .reset()
                .append(throwable.getClassName())
                .newLine()
                .modify(ConsoleModifiers.BOLD)
                .append("Message: ")
                .reset()
                .append(throwable.getMessage() == null ? "No message" : throwable.getMessage())
                .newLine()
                .modify(ConsoleModifiers.BOLD)
                .append("Stack trace: ")
                .newLine();
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder
                    .append(String.format("%s.%s(%s:%d)", element.getClassName(), element.getFileName(), element.getMethodName(), element.getLineNumber()))
                    .newLine();
        }
        builder.print();
    }

}
