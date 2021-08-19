/*
 *  majava - tech.majava.context.config.deserialization.ApplicationConfigDeserializer
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.majava.context.config.ApplicationConfig;
import tech.majava.context.config.Config;
import tech.majava.modules.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p><b>Class {@link ApplicationConfigDeserializer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ApplicationConfigDeserializer extends StdDeserializer<ApplicationConfig> {

    private static final long serialVersionUID = 7661000716783379354L;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApplicationConfigDeserializer() {
        super(ApplicationConfig.class);
    }

    /**
     * Deserializes the provided json to {@link tech.majava.context.config.ApplicationConfig}
     *
     * @param p    the parser
     * @param ctxt the context
     * @return the parse {@link tech.majava.context.config.ApplicationConfig}
     * @throws IOException if an error occurs while parsing
     */
    @Override
    public ApplicationConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final ApplicationConfig config = new ApplicationConfig();
        final Map<String, JsonNode> nodes = convert(p.readValueAsTree(), new TypeReference<Map<String, JsonNode>>() {});
        set(nodes.remove("di"), config::setDi);
        set(nodes.remove("name"), config::setName);
        set(nodes.remove("include"), config::setInclude);
        set(nodes.remove("tmp"), config::setTmp);
        set(nodes.remove("modules"), config::setModules, new TypeReference<Map<String, Class<? extends Module<? extends Config>>>>() {});
        final HashMap<String, String> moduleConfigs = new HashMap<>();
        nodes.forEach((s, jsonNode) -> moduleConfigs.put(s, jsonNode.toString()));
        config.setModuleConfigs(moduleConfigs);
        return config;
    }

    private <T> void set(@Nullable JsonNode node, @Nonnull Consumer<T> setter) {
        set(node, setter, new TypeReference<T>() {});
    }

    private <T> void set(@Nullable JsonNode node, @Nonnull Consumer<T> setter, @Nonnull TypeReference<T> typeReference) {
        if (node != null) {
            setter.accept(convert(node, typeReference));
        }
    }

    private <T> T convert(@Nonnull JsonNode node, @Nonnull TypeReference<T> typeReference) {
        return objectMapper.convertValue(node, typeReference);
    }

}
