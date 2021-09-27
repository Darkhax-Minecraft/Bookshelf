package net.darkhax.bookshelf.lib.function;

import java.util.function.Supplier;

/**
 * An implementation of Supplier that wraps another supplier with a null check. If the wrapped
 * supplier returns a null value a NullReturnedException will be raised.
 *
 * @param <T> The type of value returned by the supplier.
 */
public class NonNullSupplier<T> implements Supplier<T> {
    
    /**
     * The internal supplier being wrapped. This is the source of returned values.
     */
    private final Supplier<T> internal;
    
    /**
     * A supplier that returns the error message used when a null value is encountered.
     */
    private final Supplier<String> errorSupplier;
    
    private NonNullSupplier(Supplier<T> internal, Supplier<String> errorSupplier) {
        
        this.internal = internal;
        this.errorSupplier = errorSupplier;
    }
    
    @Override
    public T get () {
        
        final T value = this.internal.get();
        
        if (value == null) {
            
            throw new NullReturnedException(this.errorSupplier.get());
        }
        
        return value;
    }
    
    /**
     * Wraps the provided supplier with a null check. If the wrapped supplier returns null a
     * NullReturnedException will be raised.
     * 
     * @param <T> The type of value returned by the supplier.
     * @param toWrap The supplier to wrap.
     * @return A supplier that has been wrapped with a null check.
     */
    public static <T> Supplier<T> from (Supplier<T> toWrap) {
        
        return NonNullSupplier.from(toWrap, () -> "The returned value was null.");
    }
    
    /**
     * Wraps the provided supplier with a null check. If the wrapped supplier returns null a
     * NullReturnedException will be raised.
     * 
     * @param <T> The type of value returned by the supplier.
     * @param toWrap The supplier to wrap.
     * @param errorSupplier A supplier that generates the exception message when a null value
     *        is returned from the wrapped supplier.
     * @return A supplier that has been wrapped with a null check.
     */
    public static <T> Supplier<T> from (Supplier<T> toWrap, Supplier<String> errorSupplier) {
        
        return new NonNullSupplier<>(toWrap, errorSupplier);
    }
    
    /**
     * An exception that is thrown when a NonNullSupplier would return a null value.
     */
    public static class NullReturnedException extends RuntimeException {
        
        private static final long serialVersionUID = -1848463236953236103L;
        
        private NullReturnedException(String message) {
            
            super(message);
        }
    }
}