/*
 *  majbot - cz.majksa.majbot.utils.ConfigUtils
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

package cz.majksa.commons.majava.utils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * <p><b>Class {@link ClassUtils}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClassUtils {

    @Nonnull
    public static Class<?> toClass(@Nonnull String raw) throws ClassNotFoundException {
        return Class.forName(raw);
    }

    @Nonnull
    public static Method toClassMethod(@Nonnull String raw, Class<?>... parameterTypes) throws ClassNotFoundException, NoSuchMethodException {
        String[] parts = raw.split("::");
        Class<?> clazz = toClass(parts[0]);
        return clazz.getDeclaredMethod(parts[1], parameterTypes);
    }

    @Nonnull
    public static List<Class<?>> getParents(@Nonnull Class<?> clazz) {
        return getParents(clazz, Object.class);
    }

    @Nonnull
    public static List<Class<?>> getParents(@Nonnull Class<?> clazz, @Nonnull Class<?> to) {
        List<Class<?>> parents = new ArrayList<>();

        do {
            parents.add(clazz);

            // First, add all the interfaces implemented by this class
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                parents.addAll(Arrays.asList(interfaces));

                for (Class<?> interfaze : interfaces) {
                    parents.addAll(getParents(interfaze, to));
                }
            }

            // Add the super class
            Class<?> superClass = clazz.getSuperclass();

            // Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
            if (superClass == null) {
                break;
            }

            // Now inspect the superclass
            clazz = superClass;
        } while (!to.equals(clazz));

        return parents;
    }


}
