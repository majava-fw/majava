/*
 *  majava - tech.majava.listeners.ListenersConfig
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

package tech.majava.listeners;

import tech.majava.context.config.ConfigNode;
import tech.majava.modules.ModuleConfig;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Class {@link ListenersConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class ListenersConfig extends ModuleConfig {

    private final List<Class<?>> handlers;

    /**
     * Constructor
     *
     * @param node the raw node
     */
    public ListenersConfig(@Nonnull ConfigNode node) {
        super(node);
        handlers = node.getOrDefault("handlers", new ArrayList<>());
    }

}
