package net.darkhax.bookshelf.api.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<P1, P2, P3, R> {

    R apply(P1 p1, P2 p2, P3 p3);

    default TriFunction<P1, P2, P3, R> andThen(Function<? super R, ? extends R> after) {
        Objects.requireNonNull(after);
        return (p1, p2, p3) -> after.apply(apply(p1, p2, p3));
    }
}