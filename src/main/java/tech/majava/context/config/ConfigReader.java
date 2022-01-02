/*
 *  majava - tech.majava.context.config.ConfigReader
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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jodd.json.JsonArray;
import jodd.json.JsonObject;
import jodd.json.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import tech.majava.context.config.deserialization.ApplicationConfigDeserializer;
import tech.majava.context.config.deserialization.MethodsDeserializer;
import tech.majava.context.config.deserialization.URIDeserializer;
import tech.majava.listeners.ListenersConfig;
import tech.majava.listeners.ListenersConfigDeserializer;
import tech.majava.listeners.ListenersModule;
import tech.majava.logging.LoggingModule;
import tech.majava.modules.Module;
import tech.majava.utils.CollectionUtils;
import tech.majava.utils.LambaUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link ConfigReader}</b></p>
 *
 * @author majksa
 * @version 1.1.5
 * @since 1.0.0
 */
public class ConfigReader {

    public static final HashMap<String, Class<? extends Module<? extends Config>>> defaultModules = new HashMap<>();
    public static final SimpleModule mapperModule = new SimpleModule();
    public static final ObjectMapper mapper = new ObjectMapper();

    @Nonnull
    private final List<File> usedFiles = new ArrayList<>();
    private final ObjectMapper yamlToJson = new ObjectMapper(new YAMLFactory());
    @Getter
    @Nonnull
    private final ApplicationConfig config;

    static {
        mapperModule.addDeserializer(Methods.class, new MethodsDeserializer());
        mapperModule.addDeserializer(URI.class, new URIDeserializer());
        mapperModule.addDeserializer(ApplicationConfig.class, new ApplicationConfigDeserializer());
        mapper.registerModule(mapperModule);
        defaultModules.put("listeners", ListenersModule.class);
        mapperModule.addDeserializer(ListenersConfig.class, new ListenersConfigDeserializer());
        defaultModules.put("logging", LoggingModule.class);
    }

    /**
     * Constructor
     *
     * @param file the file to be read
     * @throws IOException if an exception occurs when reading
     */
    public ConfigReader(@Nonnull File file) throws IOException {
        try {
            final JsonObject fullRawConfig = readAll(file);
            fullRawConfig.put("include", usedFiles.stream().map(File::toString).collect(Collectors.toList()));
            config = mapper.readValue(fullRawConfig.toString(), ApplicationConfig.class);
        } catch (Throwable throwable) {
            if (throwable.getCause() instanceof IOException) {
                throw (IOException) throwable.getCause();
            }
            throw throwable;
        }
    }

    @Nonnull
    @SneakyThrows
    protected JsonObject readAll(@Nonnull File main) {
        final JsonNode content = yamlToJson.readValue(main, JsonNode.class);
        final JsonObject parsed = JsonParser.create().parseAsJsonObject(content.toString());
        final JsonArray include = parsed.getJsonArray("include");
        if (include != null) {
            include
                    .stream()
                    .map(String::valueOf)
                    .map(query -> getPath(main, query))
                    .filter(LambaUtils.negate(usedFiles::contains))
                    .peek(usedFiles::add)
                    .map(this::readAll)
                    .forEach(parsed::mergeInDeep);
        }
        return parsed;
    }

    protected static File getPath(@Nonnull File main, @Nonnull String query) {
        return query.startsWith("/") ? new File(query.substring(1)) : new File(main.getParent(), query);
    }

    /**
     * Makes sure that there are no unexpected options inside the config
     */
    public void checkConfigOptions() {
        final Set<String> moduleKeys = new HashSet<>(config.getModuleConfigs().keySet());
        moduleKeys.removeAll(config.getModules().keySet());
        if (moduleKeys.size() > 0) {
            throw new IllegalArgumentException("Unexpected options inside config: " + String.join(", ", moduleKeys));
        }
    }

    /**
     * Read the provided file
     *
     * @param file the file
     * @return the generated config
     * @throws IOException if an exception occurs when reading
     */
    @Nonnull
    public static ApplicationConfig read(@Nonnull File file) throws IOException {
        final ConfigReader reader = new ConfigReader(file);
        reader.checkConfigOptions();
        return reader.config;
    }

    /**
     * Read the provided file
     *
     * @param file the file
     * @return the generated config
     * @throws IOException if an exception occurs when reading
     */
    @Nonnull
    public static ApplicationConfig read(@Nonnull String file) throws IOException {
        return read(new File(file));
    }

    /**
     * Read the provided file
     *
     * @param url the file url
     * @return the generated config
     * @throws IOException if an exception occurs when reading
     */
    @Nonnull
    public static ApplicationConfig read(@Nonnull URL url) throws IOException {
        return read(url.getFile());
    }

}
