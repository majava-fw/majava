/*
 *  majava - tech.majava.context.config.deserialization.URIDeserializer
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

package tech.majava.context.config.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * <p><b>Class {@link URIDeserializer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class URIDeserializer extends StdDeserializer<URI> {

    private static final long serialVersionUID = 4528364264041556640L;

    public URIDeserializer() {
        super(URI.class);
    }

    /**
     * Deserializes the provided json to {@link java.net.URI}
     *
     * @param p    the parser
     * @param ctxt the context
     * @return the parse {@link java.net.URI}
     * @throws IOException if an error occurs while parsing
     */
    @Override
    public URI deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new File(p.getText()).toURI();
    }

}
