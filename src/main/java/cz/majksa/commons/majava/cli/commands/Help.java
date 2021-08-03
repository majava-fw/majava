/*
 *  majava - cz.majksa.commons.majava.cli.commands.Help
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

package cz.majksa.commons.majava.cli.commands;

import cz.majksa.commons.majava.cli.ConsoleModifiers;
import cz.majksa.commons.majava.cli.ConsoleTextBuilder;
import org.apache.commons.cli.CommandLine;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * <p><b>Class {@link Help}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Help extends CliCommand {

    private final CliRunner cliRunner;

    public Help(CliRunner cliRunner) {
        super(null, "help", "shows this message");
        this.cliRunner = cliRunner;
    }

    @Override
    protected void onCommand(@Nonnull CommandLine commandLine) throws ConsoleRuntimeException {
        final ConsoleTextBuilder builder = new ConsoleTextBuilder()
                .append("Showing help for Majava application")
                .newLine()
                .newLine();
        final int length = Stream.concat(
                        cliRunner.getCommands()
                                .values()
                                .stream()
                                .map(CommandsGroup::getMaxLength),
                        cliRunner.getRootCommands()
                                .values()
                                .stream()
                                .map(command -> command.getName().length())
                )
                .max(Integer::compareTo)
                .orElse(4);
        cliRunner.getRootCommands().values().forEach(command -> addToBuilder(builder, command.getName(), command.getDescription(), length));
        cliRunner.getCommands().values().forEach(group -> addToBuilder(builder, group, length));
        builder.print();
    }

    private void addToBuilder(@Nonnull ConsoleTextBuilder builder, @Nonnull CommandsGroup group, int length) {
        group.getCommands().forEach((name, context) -> addToBuilder(builder, context.getRoute(), context.getDescription(), length));
    }

    private void addToBuilder(@Nonnull ConsoleTextBuilder builder, @Nonnull String name, @Nonnull String description, int length) {
        builder
                .modify(ConsoleModifiers.GREEN)
                .append(name)
                .reset();
        for (int i = 0; i < length + 3 - name.length(); i++) {
            builder.append(" ");
        }
        final int descriptionLength = ConsoleTextBuilder.WIDTH - length + 3;
        builder.append(description.substring(0, Math.min(descriptionLength, description.length())));
        for (int i = 0; i < description.length() / descriptionLength; i++) {
            builder.newLine();
            for (int j = 0; j < length + 3; j++) {
                builder.append(" ");
            }
            builder.append(description.substring((i + 1) * descriptionLength, Math.min((i + 2) * descriptionLength, description.length())));
        }
        builder.newLine();
    }

}
