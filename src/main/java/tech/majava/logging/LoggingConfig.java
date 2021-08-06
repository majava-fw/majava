
/*
 *  majava - tech.majava.logging.LoggingConfig
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

package tech.majava.logging;

import com.fasterxml.jackson.annotation.JsonMerge;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.majava.context.config.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;

/**
 * <p><b>Class {@link LoggingConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class LoggingConfig implements Config {

    private static final long serialVersionUID = 5644051024948255779L;

    @Nullable
    @JsonMerge
    private URI errors = null;

    @Nonnull
    @JsonMerge
    private String name = "Majava";

    @JsonMerge
    private boolean debug = false;

}
