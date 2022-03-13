package net.darkhax.bookshelf.api.function;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<P1, P2, P3, P4> {

    void accept(P1 p1, P2 p2, P3 p3, P4 p4);

    default QuadConsumer<P1, P2, P3, P4> andThen(QuadConsumer<P1, P2, P3, P4> after) {

        Objects.requireNonNull(after);

        return (p1, p2, p3, p4) -> {
            accept(p1, p2, p3, p4);
            after.accept(p1, p2, p3, p4);
        };
    }
}