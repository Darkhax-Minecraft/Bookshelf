package net.darkhax.bookshelf.common.api.util;

import com.mojang.datafixers.util.Either;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionHelper {

    /**
     * Tests an optional value. If the value is empty or the test fails it will return false.
     *
     * @param input The input value to test.
     * @param test  The test to perform.
     * @param <T>   The type of value to test.
     * @return If the test was successful.
     */
    public static <T> boolean test(Optional<T> input, Predicate<T> test) {
        return input.isEmpty() || test.test(input.get());
    }

    /**
     * Unpacks an Either into its value using the first possible match.
     *
     * @param either The Either to resolve.
     * @param <T>    The type of value held by the Either.
     * @return The first value that was unpacked.
     */
    public static <T> T unpack(Either<T, T> either) {
        return either.map(Function.identity(), Function.identity());
    }
}