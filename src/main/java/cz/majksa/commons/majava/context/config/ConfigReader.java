/*
 *  majava - cz.majksa.commons.majava.context.config.ConfigReader
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import cz.majksa.commons.majava.context.config.deserialization.ApplicationConfigDeserializer;
import cz.majksa.commons.majava.context.config.deserialization.MethodsDeserializer;
import cz.majksa.commons.majava.context.config.deserialization.URIDeserializer;
import cz.majksa.commons.majava.listeners.ListenersModule;
import cz.majksa.commons.majava.modules.Module;
import cz.majksa.commons.majava.modules.ModuleConfig;
import cz.majksa.commons.majava.utils.CollectionUtils;
import cz.majksa.commons.majava.utils.LambaUtils;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.context.config.ConfigReader}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigReader {

    public static final HashMap<String, Class<? extends Module<? extends ModuleConfig>>> defaultModules = new HashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Nonnull
    private final List<File> usedFiles = new ArrayList<>();
    @Getter
    @Nonnull
    private final ApplicationConfig config;
    @Nonnull
    private final ObjectReader reader;

    static {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(Methods.class, new MethodsDeserializer());
        module.addDeserializer(URI.class, new URIDeserializer());
        module.addDeserializer(ApplicationConfig.class, new ApplicationConfigDeserializer());
        mapper.registerModule(module);
        defaultModules.put("listeners", ListenersModule.class);
    }

    public ConfigReader(@Nonnull File file) throws IOException {
        config = mapper.readValue(file, ApplicationConfig.class);
        config.setModules(CollectionUtils.mergeMaps(defaultModules, config.getModules()));
        reader = mapper.readerForUpdating(config);
        usedFiles.add(file);
        read(file, new ArrayList<>(config.getInclude()));
        config.setInclude(
                usedFiles
                        .stream()
                        .filter(LambaUtils.negate(file::equals))
                        .map(File::getAbsolutePath)
                        .collect(Collectors.toList())
        );
        checkConfigOptions();
    }

    private void checkConfigOptions() {
        final Set<String> moduleKeys = new HashSet<>(config.getModuleConfigs().keySet());
        moduleKeys.removeAll(config.getModules().keySet());
        if (moduleKeys.size() > 0) {
            throw new IllegalArgumentException("Unexpected options inside config: " + String.join(", ", moduleKeys));
        }
    }

    private void read(@Nonnull File current, @Nonnull List<String> files) throws IOException {
        for (String file : files) {
            final File fileToRead = file.startsWith("/") ? new File(file.substring(1)) : new File(current.getParent(), file);
            if (usedFiles.contains(fileToRead)) {
                continue;
            }
            usedFiles.add(fileToRead);
            reader.readValue(fileToRead);
            final ApplicationConfig applicationConfig = mapper.readValue(fileToRead, ApplicationConfig.class);
            read(fileToRead, new ArrayList<>(applicationConfig.getInclude()));
        }
    }

    @Nonnull
    public static ApplicationConfig read(@Nonnull File file) throws IOException {
        return new ConfigReader(file).config;
    }

    @Nonnull
    public static ApplicationConfig read(@Nonnull String file) throws IOException {
        return read(new File(file));
    }

    @Nonnull
    public static ApplicationConfig read(@Nonnull URL url) throws IOException {
        return read(url.getFile());
    }

}
