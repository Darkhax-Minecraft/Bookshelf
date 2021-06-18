package net.darkhax.bookshelf.utils;

import java.util.function.Function;

import net.darkhax.bookshelf.internal.entity.IBookshelfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * This class contains various helper functions for working with entities and their data.
 */
public final class EntityHelper {
    
    /**
     * Checks if an entity has a persistent data tag.
     * 
     * @param entity The entity to check.
     * @param identifier The ID of the tag to search for.
     * @return Whether or not the entity has the persistent data tag.
     */
    public static boolean hasPersistentData (Entity entity, Identifier identifier) {
        
        return IBookshelfEntity.from(entity).bookshelf$getPersistentData().containsKey(identifier);
    }
    
    /**
     * Gets a persistent compound NBT tag from an entity. This tag will be kept within the NBT
     * of that entity and will persist world reloads and dimension changes. If the data tag
     * does not exist an empty tag will be created for your.
     * 
     * @param entity The entity to access data from.
     * @param identifier The ID of the data you want to access.
     * @return The persistent compound NBT tag.
     */
    public static CompoundTag getPersistentData (Entity entity, Identifier identifier) {
        
        return getPersistentData(entity, identifier, e -> new CompoundTag());
    }
    
    /**
     * Gets a persistent compound NBT tag from an entity. This tag will be kept within the NBT
     * of that entity and will persist world reloads and dimension changes. If the data tag
     * does not exist the fallback function will be used to create one.
     * 
     * @param entity The entity to access data from.
     * @param identifier The ID of the data you want to access.
     * @param fallback A fallback function to create a compound tag if one does not already
     *        exist.
     * @return The persistent compound NBT tag.
     */
    public static <T extends Entity> CompoundTag getPersistentData (T entity, Identifier identifier, Function<T, CompoundTag> fallback) {
        
        return IBookshelfEntity.from(entity).bookshelf$getPersistentData().computeIfAbsent(identifier, k -> fallback.apply(entity));
    }
}