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
import tech.majava.context.config.Methods;
import tech.majava.modules.Module;
import tech.majava.modules.ModuleConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * <p><b>Class {@link ApplicationConfigDeserializer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ApplicationConfigDeserializer extends StdDeserializer<ApplicationConfig> {

    private static final long serialVersionUID = 7661000716783379354L;

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
        final JsonNode jsonNode = p.readValueAsTree();
        config.setDi(jsonNode.get("di").traverse(p.getCodec()).readValueAs(Methods.class));
        config.setName(jsonNode.get("name").asText(config.getName()));
        config.setInclude(jsonNode.findValuesAsText("include"));
        final JsonNode tmp = jsonNode.get("tmp");
        if (tmp != null) {
            config.setTmp(tmp.traverse(p.getCodec()).readValueAs(URI.class));
        }
        final JsonNode modules = jsonNode.get("modules");
        if (modules != null) {
            config.setModules(new ObjectMapper().convertValue(modules, new TypeReference<Map<String, Class<? extends Module<? extends ModuleConfig>>>>(){}));
        }
        final Map<String, JsonNode> modulesConfig = new ObjectMapper().convertValue(jsonNode, new TypeReference<Map<String, JsonNode>>(){});
        modulesConfig.remove("di");
        modulesConfig.remove("name");
        modulesConfig.remove("include");
        modulesConfig.remove("modules");
        modulesConfig.remove("tmp");
        config.setModuleConfigs(modulesConfig);
        return config;
    }

}
