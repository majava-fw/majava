/*
 *  majava - tech.majava.di.SimpleContainer
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

package tech.majava.di;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>Class {@link SimpleContainer}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class SimpleContainer implements Container {

    private final Map<String, Object> database = new HashMap<>();

    /**
     * {@inheritDoc}
     *
     * @param key the object key
     * @param <T> the object type
     * @return the object
     * @throws NullPointerException if object does not exist
     */
    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@Nonnull String key) throws NullPointerException {
        final T t = (T) database.get(key);
        if (t == null) {
            throw new NullPointerException(key);
        }
        return t;
    }

    /**
     * {@inheritDoc}
     *
     * @param key    the object key
     * @param object the object
     */
    @Nonnull
    @Override
    public Container register(@Nonnull String key, @Nonnull Object object) {
        database.put(key, object);
        return this;
    }

}
