package net.darkhax.bookshelf.api.function;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A Supplier implementation that will cache the result of an internal delegate supplier.
 *
 * @param <T> The type that is cached by the supplier.
 */
public class CachedSupplier<T> implements Supplier<T> {

    /**
     * The internal Supplier responsible for producing the cached value.
     */
    private final Supplier<T> delegate;

    /**
     * Tracks if the delegate supplier has been cached.
     */
    private boolean cached = false;

    /**
     * The most recently cached value.
     */
    @Nullable
    private T cachedValue;

    protected CachedSupplier(Supplier<T> delegate) {

        this.delegate = delegate;
    }

    @Override
    public T get() {

        if (!this.isCached()) {

            this.cachedValue = this.delegate.get();
            this.cached = true;
        }

        return cachedValue;
    }

    /**
     * Invalidates the cached value. This will result in a new value being cached the next type {@link #get()} is used.
     */
    public void invalidate() {

        this.cached = false;
        this.cachedValue = null;
    }

    /**
     * Checks if this supplier has a cached value. This is not a substitute for null checking.
     *
     * @return Has the supplier cached a value.
     */
    public boolean isCached() {

        return this.cached;
    }

    /**
     * Safely attempts to invoke a consumer with the cached value. If a value has not been cached the consumer will not
     * be invoked and a new value will not be cached. The consumer will still be invoked if the cached value is null.
     *
     * @param consumer The consumer to invoke if a value has been cached.
     */
    public void ifCached(Consumer<T> consumer) {

        if (this.isCached()) {

            consumer.accept(this.get());
        }
    }

    /**
     * Safely attempts to invoke a consumer with the cached value. If a value has not been cached, or the cached value
     * is null the consumer will not be invoked and a new value will not be cached.
     *
     * @param consumer The consumer to invoke if a cached value is present.
     */
    public void ifPresent(Consumer<T> consumer) {

        if (this.cachedValue != null) {

            consumer.accept(this.get());
        }
    }

    /**
     * Invokes the consumer with the cached value. This will cause a value to be cached if one has not been cached
     * already.
     *
     * @param consumer The consumer to invoke.
     */
    public void apply(Consumer<T> consumer) {

        consumer.accept(this.get());
    }

    public <X> CachedSupplier<X> cast() {

        return (CachedSupplier<X>)this;
    }

    /**
     * Creates a cached supplier that can only produce a single value.
     *
     * @param singleton The only value for the cache to use.
     * @param <T>       The type of value held by the cache.
     * @return A cached supplier that will only produce a single cached value.
     */
    public static <T> CachedSupplier<T> singleton(T singleton) {

        return cache(() -> singleton);
    }

    /**
     * Creates a cached supplier that will cache a value from the supplied delegate when queried.
     *
     * @param delegate The delegate supplier responsible for producing the cached value. This will only be accessed when
     *                 the cached supplier is being accessed and has not been cached already.
     * @param <T>      The type of value held by the cache.
     * @return A supplier that will cache a value from the supplied delegate supplier.
     */
    public static <T> CachedSupplier<T> cache(Supplier<T> delegate) {

        return new CachedSupplier<>(delegate);
    }
}