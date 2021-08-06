/*
 *  majava - tech.majava.listeners.ListenersConfigDeserializer
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

/**
 * <p><b>Class {@link tech.majava.listeners.ListenersConfigDeserializer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ListenersConfigDeserializer extends StdDeserializer<ListenersConfig> {

    private static final long serialVersionUID = 7661000716783379354L;

    public ListenersConfigDeserializer() {
        super(ListenersConfig.class);
    }

    /**
     * Deserializes the provided json to {@link tech.majava.listeners.ListenersConfig}
     *
     * @param p    the parser
     * @param ctxt the context
     * @return the parse {@link tech.majava.listeners.ListenersConfig}
     * @throws java.io.IOException if an error occurs while parsing
     */
    @Override
    public ListenersConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final ListenersConfig config = new ListenersConfig();
        final JsonNode jsonNode = p.readValueAsTree();
        final List<Class<?>> handlers = new ObjectMapper().convertValue(jsonNode, new TypeReference<List<Class<?>>>(){});
        config.setHandlers(handlers);
        return config;
    }

}
