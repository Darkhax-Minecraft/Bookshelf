package net.darkhax.bookshelf.common.api.function;

import java.util.Objects;

/**
 * A consumer that accepts four parameters.
 *
 * @param <P1> The first parameter.
 * @param <P2> The second parameter.
 * @param <P3> The third parameter.
 * @param <P4> The fourth parameter.
 */
@FunctionalInterface
public interface QuadConsumer<P1, P2, P3, P4> {

    /**
     * Consumes the parameters.
     *
     * @param p1 The first parameter.
     * @param p2 The second parameter.
     * @param p3 The third parameter.
     * @param p4 The fourth parameter.
     */
    void accept(P1 p1, P2 p2, P3 p3, P4 p4);

    /**
     * Chains another consumer on to this one.
     *
     * @param after The consumer to run after this one.
     * @return A new consumer that chains both.
     */
    default QuadConsumer<P1, P2, P3, P4> andThen(QuadConsumer<P1, P2, P3, P4> after) {
        Objects.requireNonNull(after);
        return (p1, p2, p3, p4) -> {
            accept(p1, p2, p3, p4);
            after.accept(p1, p2, p3, p4);
        };
    }
}