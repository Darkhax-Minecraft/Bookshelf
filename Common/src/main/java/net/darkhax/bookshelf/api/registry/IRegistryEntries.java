package net.darkhax.bookshelf.api.registry;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This interface defines a collection of registry entries that are owned by a specific mod. The added entries will be
 * propagated to the core game registries at an appropriate time.
 *
 * @param <V> The type of entry being held.
 */
public interface IRegistryEntries<V> extends Iterable<V> {

    /**
     * Adds a new value to the registry. The value will be registered with the game at the appropriate time.
     *
     * @param value The value to add to the registry.
     * @param id    The ID to assign to the value. This registry use namespace IDs that have a namespace and path
     *              component. The namespace for this parameter will be taken from {@link #getOwner()}.
     * @param <VT>  The type of the value being added to the registry.
     * @return The value that was added to the registry.
     */
    default <VT extends V> VT add(VT value, String id) {

        return this.add(value, new ResourceLocation(this.getOwner(), id));
    }

    /**
     * Adds a new value to the registry. The value will be registered with the game at the appropriate time.
     *
     * @param value The value to add to the registry.
     * @param id    The ID to assign the value.
     * @param <VT>  The type of the value being added to the registry.
     * @return The value that was added to the registry.
     */
    <VT extends V> VT add(VT value, ResourceLocation id);

    /**
     * Looks up a value by its ID. Only values added through this object can be retrieved through this method.
     *
     * @param id The ID of the value to look up.
     * @return The value that was found, or null when no value is found.
     */
    @Nullable
    default V getEntry(ResourceLocation id) {

        return this.getEntries().get(id);
    }

    /**
     * Gets an immutable map containing all added values. The key is the ID used when registering a value and the value
     * is the value being registered.
     *
     * @return An immutable map containing all added values.
     */
    Map<ResourceLocation, V> getEntries();

    /**
     * Gets the ID of the mod that owns these entries.
     *
     * @return The ID of the mod that owns these entries.
     */
    String getOwner();

    /**
     * Checks if no entries have been added.
     *
     * @return Returns true when no entries have been added.
     */
    boolean isEmpty();

    /**
     * Registers a listener that will be invoked when a new value is inserted into the entries.
     *
     * @param listener The listener to invoke.
     */
    void addInsertListener(BiConsumer<ResourceLocation, V> listener);

    @Override
    default Iterator<V> iterator() {

        return this.getEntries().values().iterator();
    }
}