
/*
 *  majava - cz.majksa.commons.majava.listeners.ListenersConfig
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

package cz.majksa.commons.majava.logging;

import cz.majksa.commons.majava.context.config.ConfigNode;
import cz.majksa.commons.majava.modules.ModuleConfig;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.logging.LoggingConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class LoggingConfig extends ModuleConfig {

    @Nullable
    private final URI errors;
    @Nonnull
    private final String name;
    private final boolean debug;

    /**
     * Constructor
     *
     * @param node the raw node
     */
    public LoggingConfig(@Nonnull ConfigNode node) {
        super(node);
        name = node.getOrDefault("name", "Majava");
        errors = node.get("errors");
        debug = node.getOrDefault("debug", false);
    }

}
