/*
 *  majava - cz.majksa.commons.majava.context.config.Methods
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

import cz.majksa.commons.majava.serializable.SerializableMethod;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link Methods}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Methods implements Map<String, Method>, Serializable {

    private static final long serialVersionUID = 4910753560277361047L;
    private final HashMap<String, Method> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Method get(Class<?>... key) {
        return get(
                Arrays.stream(key)
                        .map(Class::getName)
                        .collect(Collectors.joining(","))
        );
    }

    public Method get(String key) {
        return map.get(key);
    }

    @Override
    public Method get(Object key) {
        return map.get(key);
    }

    public Method put(Method method) {
        return put(
                Arrays.stream(method.getParameterTypes())
                        .map(Class::getName)
                        .collect(Collectors.joining(",")),
                method
        );
    }

    @Override
    public Method put(String key, Method value) {
        return map.put(key, value);
    }

    @Override
    public Method remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends String, ? extends Method> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Method> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Method>> entrySet() {
        return map.entrySet();
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        ((HashMap<String, SerializableMethod>) stream.readObject())
                .forEach((key, method) -> map.put(key, method.getMethod()));
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        final HashMap<String, SerializableMethod> serializableMap = new HashMap<>();
        map.forEach((key, method) -> serializableMap.put(key, new SerializableMethod(method)));
        stream.writeObject(serializableMap);
    }

}
