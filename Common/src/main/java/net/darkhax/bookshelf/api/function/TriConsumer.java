package net.darkhax.bookshelf.api.function;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<P1, P2, P3> {

    void accept(P1 p1, P2 p2, P3 p3);

    default TriConsumer<P1, P2, P3> andThen(TriConsumer<P1, P2, P3> after) {

        Objects.requireNonNull(after);

        return (p1, p2, p3) -> {
            accept(p1, p2, p3);
            after.accept(p1, p2, p3);
        };
    }
}