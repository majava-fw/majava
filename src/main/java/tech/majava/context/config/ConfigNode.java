/*
 *  majava - tech.majava.context.config.ConfigNode
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link ConfigNode}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigNode {

    @Nonnull
    private final JsonNode node;

    /**
     * Constructor
     *
     * @param jsonNode the node to be wrapped as a config node
     */
    public ConfigNode(@Nullable JsonNode jsonNode) {
        if (jsonNode == null) {
            node = NullNode.instance;
        } else {
            node = jsonNode;
        }
    }

    /**
     * Convert the node itself to a required value
     *
     * @param <T> the required value
     * @return the converted node
     */
    public <T> T convert() {
        return new ObjectMapper().convertValue(node, new TypeReference<T>() {});
    }

    /**
     * Get the child and convert it.
     * <code>null</code> if child does not exist
     *
     * @param key the key of the child
     * @param <T> the required value
     * @return the converted child
     */
    @Nullable
    public <T> T get(@Nonnull String key) {
        final JsonNode jsonNode = node.get(key);
        if (jsonNode == null) {
            return null;
        }
        return new ObjectMapper().convertValue(jsonNode, new TypeReference<T>() {});
    }

    /**
     * Get the child and convert it.
     * <code>defaultValue</code> if child does not exist
     *
     * @param key          the key of the child
     * @param <T>          the required value
     * @param defaultValue the value if child does not exist
     * @return the converted child
     */
    @Nonnull
    public <T> T getOrDefault(@Nonnull String key, @Nonnull T defaultValue) {
        final T value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

}
