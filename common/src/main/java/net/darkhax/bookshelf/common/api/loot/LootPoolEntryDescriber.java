package net.darkhax.bookshelf.common.api.loot;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Describes the potential items that a loot pool entry can generate. See {@link LootPoolEntryDescriptions} for usage.
 */
@FunctionalInterface
public interface LootPoolEntryDescriber {

    /**
     * Generates a list of potential items that can be produced by the loot pool entry.
     *
     * @param registries The current reloadable game registries.
     * @param entry      The loot pool entry to be analyzed.
     * @return An optional list of items that can be produced by the entry. If the describer can not handle the provided
     * entry type it will be empty.
     */
    Optional<List<ItemStack>> getPotentialDrops(@NotNull RegistryAccess registries, @NotNull LootPoolEntryContainer entry);
}