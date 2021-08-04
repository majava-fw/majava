/*
 *  majava - cz.majksa.commons.majava.modules.ModuleConfig
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

package cz.majksa.commons.majava.modules;

import cz.majksa.commons.majava.context.config.ConfigNode;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * <p><b>Interface {@link cz.majksa.commons.majava.modules.ModuleConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class ModuleConfig {

    /**
     * The config node
     */
    @Nonnull
    protected final ConfigNode node;

}