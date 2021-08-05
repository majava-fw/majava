/*
 *  majava - tech.majava.utils.CollectionUtils
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

package tech.majava.utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>Class {@link CollectionUtils}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class CollectionUtils {

    /**
     * Merges the provided maps
     *
     * @param maps the provided maps
     * @param <K>  the type of keys
     * @param <V>  the type of values
     * @return the merged map
     */
    @Nonnull
    @SafeVarargs
    public static <K, V> Map<K, V> mergeMaps(@Nonnull Map<K, V>... maps) {
        final HashMap<K, V> finalMap = new HashMap<>();
        for (Map<K, V> map : maps) {
            finalMap.putAll(map);
        }
        return finalMap;
    }

}
