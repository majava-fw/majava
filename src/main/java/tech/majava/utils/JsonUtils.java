/*
 *  majava - tech.majava.utils.JsonUtils
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jodd.json.JsonObject;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p><b>Class {@link tech.majava.utils.JsonUtils}</b></p>
 *
 * @author majksa
 * @version 1.2.0
 * @since 1.2.0
 */
public class JsonUtils {

    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private static final JsonParser parser = JsonParser.create();

    public static JsonObject readYaml(@Nonnull File file) throws IOException {
        final JsonNode node = yamlMapper.readValue(file, JsonNode.class);
        return readJson(node.toString());
    }

    public static JsonObject readJson(@Nonnull File file) throws IOException {
        final String content = String.join("\n", Files.readAllLines(Paths.get(file.toURI()), StandardCharsets.UTF_8));
        return readJson(content);
    }

    public static JsonObject readJson(@Nonnull String json) {
        return parser.parseAsJsonObject(json);
    }

    @Nonnull
    public static String toString(@Nonnull Object object) {
        return JsonSerializer.create().deep(true).serialize(object);
    }

}
