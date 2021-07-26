/*
 *  majava - cz.majksa.commons.majava.di.SimpleContainer
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

package cz.majksa.commons.majava.di;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.di.SimpleContainer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class SimpleContainer implements Container {

    private final Map<String, Object> database = new HashMap<>();

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@Nonnull String alias, @Nonnull Class<T> clazz) {
        return (T) database.get(alias);
    }

    @Override
    public void register(@Nonnull String alias, @Nonnull Object object) {
        database.put(alias, object);
    }

}
