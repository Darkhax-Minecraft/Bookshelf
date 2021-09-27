package net.darkhax.bookshelf.lib.function;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

import javax.annotation.Nullable;

/**
 * A proxy for an object that is calculated as needed at a later point. The calculated object
 * will be held with a weak reference that will be regenerated as needed when the reference
 * expires.
 * 
 * @param <T> The type of object held by the lazy weak reference.
 */
public class LazyWeakReference<T> implements Supplier<T> {
    
    /**
     * A supplier used to generate the value held by the weak reference. This is used when the
     * weak reference does not exist or the value it's referencing has been discarded.
     */
    private final Supplier<T> supplier;
    
    /**
     * A weak reference to the held value. This will be null until the value is first
     * requested.
     */
    @Nullable
    private WeakReference<T> reference;
    
    public LazyWeakReference(Supplier<T> supplier) {
        
        this.supplier = supplier;
    }
    
    @Override
    public T get () {
        
        // Get the existing reference value.
        @Nullable
        final T existing = this.reference != null ? this.reference.get() : null;
        
        // If the existing reference does not exist, create a new value and update the
        // reference.
        if (existing == null) {
            
            final T fresh = this.supplier.get();
            this.reference = new WeakReference<>(fresh);
            return fresh;
        }
        
        return existing;
    }
}