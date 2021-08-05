/*
 *  majava - tech.majava.di.Container
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

import javax.annotation.Nonnull;

/**
 * <p><b>Interface {@link Container}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Container {

    /**
     * Gets an object from the container
     *
     * @param key the object key
     * @param <T> the object type
     * @return the object
     * @throws NullPointerException if object does not exist
     */
    @Nonnull
    <T> T get(@Nonnull String key) throws NullPointerException;

    /**
     * Gets an object from the container
     *
     * @param clazz the class so be used as a key
     * @param <T>   the object type
     * @return the object
     * @throws NullPointerException if object does not exist
     */
    @Nonnull
    default <T> T get(@Nonnull Class<T> clazz) throws NullPointerException {
        return get(clazz.getName());
    }

    /**
     * Register an object to the container
     *
     * @param key    the object key
     * @param object the object
     */
    @Nonnull
    Container register(@Nonnull String key, @Nonnull Object object);

    /**
     * Register an object to the container
     *
     * @param object the object
     */
    default Container register(@Nonnull Object object) {
        return register(object.getClass().getName(), object);
    }

}
