/*
 *  majava - cz.majksa.commons.majava.MainTest
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

package cz.majksa.commons.majava;

import cz.majksa.commons.majava.di.Container;
import cz.majksa.commons.majava.di.SimpleContainer;
import cz.majksa.commons.majava.logging.Logger;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void mainTest() {
        assertDoesNotThrow(() -> Application.start(SimpleApplication.class, new String[0]));
    }

    public static Container di() {
        final SimpleContainer container = new SimpleContainer();
        container.register(new SimpleApplication());
        container.register(new Logger(LogManager.getLogger()));
        return container;
    }

    public static final class SimpleApplication extends Application {
        @Nonnull
        @Override
        protected CompletableFuture<Void> onStart(@Nonnull String[] args) {
            logger.always().log("HELLO");
            return CompletableFuture.runAsync(() -> System.out.println("start"));
        }

        @Nonnull
        @Override
        protected CompletableFuture<Void> onShutdown() {
            return CompletableFuture.runAsync(() -> System.out.println("stop"));
        }
    }

}