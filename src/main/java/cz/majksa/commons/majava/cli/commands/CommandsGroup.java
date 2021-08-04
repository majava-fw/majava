/*
 *  majava - cz.majksa.commons.majava.cli.commands.CommandsGroup
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p><b>Class {@link CommandsGroup}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public final class CommandsGroup {

    @Nonnull
    private final String name;

    @Nonnull
    private final String description;

    @Nonnull
    private final Map<String, CliCommand> commands = new LinkedHashMap<>();

    private boolean empty = true;

    private int maxLength = 0;


    public int getMaxLength() {
        return maxLength + name.length() + 1;
    }

    /**
     * Registers a command to this group
     *
     * @param command the command to be registered
     * @return {@link cz.majksa.commons.majava.cli.commands.CommandsGroup}
     */
    @Nonnull
    public CommandsGroup register(@Nonnull CliCommand command) {
        if (commands.containsKey(command.getName())) {
            throw new IllegalArgumentException("Command with name " + name + " has already been registered!");
        }
        empty = false;
        commands.put(command.getName(), command);
        if (maxLength < command.getName().length()) {
            maxLength = command.getName().length();
        }
        return this;
    }

}
