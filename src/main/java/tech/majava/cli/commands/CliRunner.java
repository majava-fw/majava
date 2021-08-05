/*
 *  majava - tech.majava.cli.commands.CliRunner
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

package tech.majava.cli.commands;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p><b>Class {@link CliRunner}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public final class CliRunner {

    @Nonnull
    private final Map<String, CommandsGroup> commands = new HashMap<>();
    private final Map<String, CliCommand> rootCommands = new HashMap<>();

    public CliRunner() {
        rootCommands.put("help", new Help(this));
        rootCommands.put("exit", new Exit());
    }

    /**
     * Registers and creates a new commands group
     *
     * @param name        the name(space) of the new group
     * @param description the description of the new group
     * @return the created group
     */
    @Nonnull
    public CommandsGroup register(@Nonnull String name, @Nonnull String description) {
        return register(new CommandsGroup(name, description));
    }

    /**
     * Registers the provided group
     *
     * @param group the group to register
     * @return the group
     */
    @Nonnull
    public CommandsGroup register(@Nonnull CommandsGroup group) {
        if (commands.containsKey(group.getName())) {
            throw new IllegalArgumentException("Command with name " + group.getName() + " has already been registered!");
        }
        commands.put(group.getName(), group);
        return group;
    }

    /**
     * Run the input
     *
     * @param arg the input that will be split into args
     * @throws ConsoleRuntimeException the runtime exception
     */
    public void run(@Nonnull String arg) throws ConsoleRuntimeException {
        run(arg.split(" "));
    }

    /**
     * Run the arguments
     *
     * @param args the arguments to be run
     * @throws ConsoleRuntimeException the runtime exception
     */
    public void run(@Nonnull String[] args) throws ConsoleRuntimeException {
        assert args.length > 0;
        final String cmd = args[0];
        final String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
        final String[] cmdParts = cmd.split(":", 2);
        final Optional<CliCommand> consumer = getCommand(cmdParts[0], cmdParts.length > 1 ? cmdParts[1] : null);
        if (!consumer.isPresent()) {
            throw new ConsoleRuntimeException("Command \"" + cmd + "\" was not found!");
        }
        consumer.get().run(cmdArgs);
    }

    /**
     * Get the command object by the provided route
     *
     * @param group the group part
     * @param command the command part
     * @return the {@link java.util.Optional} command
     */
    @Nonnull
    private Optional<CliCommand> getCommand(@Nonnull String group, @Nullable String command) {
        if (command == null) {
            if (rootCommands.containsKey(group)) {
                return Optional.of(rootCommands.get(group));
            }
            return Optional.empty();
        }
        final Map<String, CliCommand> commands = this.commands.get(group).getCommands();
        if (!commands.containsKey(command)) {
            return Optional.empty();
        }
        return Optional.of(commands.get(command));
    }

}
