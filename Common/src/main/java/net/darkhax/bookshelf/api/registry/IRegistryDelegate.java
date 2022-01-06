package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

/**
 * This interface defines a neutral delegate for accessing game data registries.
 *
 * @param <T> The type of value held in the registry.
 */
public interface IRegistryDelegate<T> {

    /**
     * Gets the value registered to a given ID.
     *
     * @param id The ID to lookup.
     * @return The value associated with the ID. A null value means the ID has not been registered.
     */
    T getValue(ResourceLocation id);

    /**
     * Gets the registry ID for a given value. This is not a suitable way to check if a value has been registered. Use
     * {@link #isRegistered(Object)} instead.
     *
     * @param value The value to reverse lookup.
     * @return The registry ID associated with value.
     */
    ResourceLocation getId(T value);

    /**
     * Checks if a given value has been registered with the game.
     *
     * @param value the value to lookup.
     * @return True when the value is present in the game registry.
     */
    boolean isRegistered(T value);

    /**
     * Gets all values held within the registry.
     *
     * @return All values held within the registry.
     */
    Collection<T> getAllValues();

    /**
     * Gets all registry keys used within the registry.
     *
     * @return All registry keys used within the registry.
     */
    Collection<ResourceLocation> getAllKeys();
}