/*
 *  majbot - cz.majksa.majbot.utils.AsyncUtils
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

import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link cz.majksa.commons.majava.utils.AsyncUtils}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class AsyncUtils {

    @SafeVarargs
    public static <T> @NonNull CompletableFuture<List<T>> allOf(@NonNull CompletableFuture<T>... futuresList) {
        return allOf(Arrays.stream(futuresList).collect(Collectors.toList()));
    }

    public static <T> @NonNull CompletableFuture<List<T>> allOf(@NonNull Collection<CompletableFuture<T>> futuresList) {
        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
        return allFuturesResult.thenApply(v ->
                futuresList
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.<T>toList())
        );
    }

    public static <T> CompletableFuture<T> apply(@NonNull CompletableFuture<T> action, @NonNull Consumer<T> consumer) {
        return action.thenApply(t -> {
            consumer.accept(t);
            return t;
        });
    }

}
