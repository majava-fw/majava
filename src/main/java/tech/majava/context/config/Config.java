/*
 *  majava - tech.majava.context.config.Config
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

package tech.majava.context.config;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * <p><b>Interface {@link Config}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Config extends Serializable {

//    void merge(@Nonnull Config config);

    @SuppressWarnings("unchecked")
    default <C extends Config> C convert(@Nonnull Config config) {
        return (C) config;
    }

}
