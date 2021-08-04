/*
 *  majava - cz.majksa.commons.majava.context.config.ApplicationConfig
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

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.databind.JsonNode;
import cz.majksa.commons.majava.modules.Module;
import cz.majksa.commons.majava.modules.ModuleConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Class {@link ApplicationConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig implements Config {

    private static final long serialVersionUID = -6561001459587273380L;
    @Nonnull
    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    @Nonnull
    @JsonMerge
    private String name = "Majava";

    @Nullable
    @JsonMerge
    private URI tmp = null;

    @Nonnull
    @JsonMerge
    private List<String> include = new ArrayList<>();

    @Nonnull
    @JsonMerge
    private Map<String, Class<? extends Module<? extends ModuleConfig>>> modules = new HashMap<>();

    @Nonnull
    @JsonMerge
    private Map<String, JsonNode> moduleConfigs = new HashMap<>();

    @Nullable
    @JsonMerge
    private Methods di = null;

    /**
     * Get the method to create {@link cz.majksa.commons.majava.di.Container}
     *
     * @return the method
     */
    @Nullable
    public Method getDi() {
        if (di == null) {
            return null;
        }
        return di.get();
    }

    /**
     * Load the config from default location
     *
     * @return the loaded config
     */
    @Nonnull
    public static ApplicationConfig load() {
        return load(null);
    }

    /**
     * Load the config from provided location
     *
     * @param config the location
     * @return the loaded config
     */
    @Nonnull
    @SneakyThrows
    public static ApplicationConfig load(@Nullable String config) {
        final URL url = loader.getResource(config == null ? "majava.yml" : config);
        if (url == null) {
            return new ApplicationConfig();
        }
        return ConfigReader.read(url);
    }

}
