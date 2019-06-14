/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.HashMap;
import java.util.Map;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This class combines the weighted random selector class with some of the features of a forge
 * registry. This hybrid enforces that all entries have a forge registry name, and allows
 * entries to be retrieved accurately using it. It does not fire forge registry events though.
 */
public class WeightedSelectorRegistry<T extends IForgeRegistryEntry<T>> extends WeightedSelector<T> {

    /**
     * This map is used to hold an ID to value reference for all entries in the weighted
     * selector.
     */
    private final Map<ResourceLocation, T> REGISTRY = new HashMap<>();

    /**
     * Adds an entry and sets it's registry id/name at the same time.
     *
     * @param value The entry to register.
     * @param weight The weight of the entry.
     * @param id The id of the entry.
     * @return Whether or not the entry was added successfully.
     */
    public boolean addEntry (T value, int weight, String id) {

        value.setRegistryName(new ResourceLocation(id));
        return this.addEntry(value, weight);
    }

    /**
     * Gets an entry using it's registry id/name.
     *
     * @param id The registry id/name for the entry you want.
     * @return If the entry was found, you will get it. Otherwise you get null.
     */
    public T getValue (ResourceLocation id) {

        return this.REGISTRY.get(id);
    }

    @Override
    public boolean addEntry (T value, int weight) {

        // Prevent invalid registry attempts.
        if (value.getRegistryName() == null) {

            Bookshelf.LOG.warn("Attempted to register an item without setting it's ID! This is not allowed.");
            return false;
        }

        final boolean added = super.addEntry(value, weight);

        // If added successfully, store in backup map.
        if (added) {

            this.REGISTRY.put(value.getRegistryName(), value);
        }

        return added;
    }

    @Override
    public boolean removeEntry (WeightedEntry<T> entry) {

        final boolean removed = super.removeEntry(entry);

        // If removed successfully remove from backup map.
        if (removed) {

            this.REGISTRY.remove(entry.getEntry().getRegistryName());
        }

        return removed;
    }
}
