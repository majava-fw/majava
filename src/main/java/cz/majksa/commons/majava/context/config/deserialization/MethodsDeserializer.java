/*
 *  majava - cz.majksa.commons.majava.context.config.deserialization.MethodDeserializer
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

package cz.majksa.commons.majava.context.config.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import cz.majksa.commons.majava.context.config.Methods;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p><b>Class {@link MethodsDeserializer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class MethodsDeserializer extends StdDeserializer<Methods> {

    private static final long serialVersionUID = -851658448089524227L;

    public MethodsDeserializer() {
        super(Methods.class);
    }

    /**
     * Deserializes the provided json to {@link cz.majksa.commons.majava.context.config.Methods}
     *
     * @param p    the parser
     * @param ctxt the context
     * @return the parse {@link cz.majksa.commons.majava.context.config.Methods}
     * @throws IOException if an error occurs while parsing
     */
    @Override
    public Methods deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String raw = p.getText();
        try {
            final String[] parts = raw.split("::");
            final Class<?> clazz = Class.forName(parts[0]);
            final String name = parts[1];
            final Methods methods = new Methods();
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().equals(name))
                    .forEach(methods::put);
            return methods;
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException(p, e.getMessage(), raw, Methods.class);
        }
    }

}
