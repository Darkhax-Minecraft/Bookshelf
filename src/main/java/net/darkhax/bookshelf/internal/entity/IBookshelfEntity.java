package net.darkhax.bookshelf.internal.entity;

import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * An interface that is applied to all entities via Mixin. This interface expands entities with
 * new internal APIs used by the rest of Bookshelf.
 */
public interface IBookshelfEntity {
    
    /**
     * Provides access to the internal API applied by Bookshelf. This should only be used
     * internally.
     * 
     * @param obj The entity to access.
     * @return The internal API.
     */
    public static IBookshelfEntity from (Object obj) {
        
        return (IBookshelfEntity) obj;
    }
    
    public static final String PERSISTENT_TAG = "BookshelfData";
    
    public Map<Identifier, CompoundTag> bookshelf$getPersistentData ();
    
    public void bookshelf$setPersistentData (Map<Identifier, CompoundTag> data);
}