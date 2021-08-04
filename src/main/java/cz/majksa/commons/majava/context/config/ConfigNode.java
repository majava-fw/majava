/*
 *  majava - cz.majksa.commons.majava.context.config.ConfigNode
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

package cz.majksa.commons.majava.context.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.context.config.ConfigNode}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigNode {

    @Nonnull
    private final JsonNode node;

    public ConfigNode(@Nullable JsonNode jsonNode) {
        if (jsonNode == null) {
            node = NullNode.instance;
        } else {
            node = jsonNode;
        }
    }

    public <T> T convert() {
        return new ObjectMapper().convertValue(node, new TypeReference<T>() {});
    }

    @Nullable
    public <T> T get(@Nonnull String key) {
        final JsonNode jsonNode = node.get(key);
        if (jsonNode == null) {
            return null;
        }
        return new ObjectMapper().convertValue(jsonNode, new TypeReference<T>() {});
    }

    @Nonnull
    public <T> T getOrDefault(@Nonnull String key, @Nonnull T defaultValue) {
        final JsonNode jsonNode = node.get(key);
        if (jsonNode == null) {
            return defaultValue;
        }
        return new ObjectMapper().convertValue(jsonNode, new TypeReference<T>() {});
    }

}
