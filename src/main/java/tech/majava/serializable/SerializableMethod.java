/*
 *  majava - tech.majava.serializable.SerializableMethod
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

package tech.majava.serializable;

import lombok.Getter;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <p><b>Class {@link SerializableMethod}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class SerializableMethod implements Serializable {

    private static final long serialVersionUID = -5139356899699681078L;

    private Class<?> clazz;
    private String name;
    private Class<?>[] args;

    private transient Method method;

    /**
     * Constructor
     *
     * @param method the method to be serialized
     */
    public SerializableMethod(@Nonnull Method method) {
        this.method = method;
        clazz = method.getDeclaringClass();
        name = method.getName();
        args = method.getParameterTypes();
    }

    @SneakyThrows(NoSuchMethodException.class)
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        clazz = (Class<?>) stream.readObject();
        name = stream.readUTF();
        args = (Class<?>[]) stream.readObject();
        method = clazz.getMethod(name, args);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(clazz);
        stream.writeUTF(name);
        stream.writeObject(args);
    }

}
