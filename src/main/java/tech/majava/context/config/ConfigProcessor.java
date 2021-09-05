/*
 *  majava - tech.majava.context.config.ConfigProcessor
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

import jodd.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tech.majava.utils.JsonUtils;

import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p><b>Class {@link ConfigProcessor}</b></p>
 *
 * @author majksa
 * @version 1.2.0
 * @since 1.2.0
 */
@RequiredArgsConstructor
public class ConfigProcessor {

    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String CONFIG_FOLDER_PREFIX = "config";
    @Nonnull
    private final Filer filer;

    public void write(@Nonnull String name, @Nonnull ApplicationConfig config) {
        write(name, JsonUtils.toString(config));
    }

    public void write(@Nonnull String name, @Nonnull String config) {
        try (final Writer out = getNewUniqueConfig(name).openWriter()) {
            out.write(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileObject getNewUniqueConfig(@Nonnull String name) {
        int UID = 0;
        while (true) {
            final String filename = getRelativeName(name + (UID == 0 ? "" : UID) + ".json");
            try {
                return filer.createResource(StandardLocation.CLASS_OUTPUT, "", filename);
            } catch (FilerException e) {
                UID++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Nonnull
    @SneakyThrows
    public static List<File> listAll() {
        List<File> filenames = new ArrayList<>();
        try (
                final InputStream in = loader.getResourceAsStream(CONFIG_FOLDER_PREFIX);
                final BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))
        ) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(getConfig(resource));
            }
        }

        return filenames;
    }

    @SneakyThrows
    public static File getConfig(@Nonnull String name) {
        final URL url = Objects.requireNonNull(loader.getResource(getRelativeName(name)));
        return new File(url.toURI());
    }

    public static String getRelativeName(@Nonnull String name) {
        return CONFIG_FOLDER_PREFIX + "/" + name;
    }

    @SneakyThrows
    public static JsonObject read() {
        final JsonObject object = new JsonObject();
        for (File file : listAll()) {
            object.mergeInDeep(JsonUtils.readJson(file));
        }
        return object;
    }

}
