package net.darkhax.bookshelf.common.api.loot;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Describes the potential items that a loot pool entry can generate. See {@link LootPoolEntryDescriptions} for usage.
 */
@FunctionalInterface
public interface LootPoolEntryDescriber {

    /**
     * Describes items that may potentially be dropped by a loot pool entry.
     *
     * @param registries Access to the current game registries.
     * @param entry      The loot pool entry to be processed.
     * @param collector  Collects entries from the entry into the desired format.
     */
    void getPotentialDrops(@NotNull RegistryAccess registries, @NotNull LootPoolEntryContainer entry, Consumer<ItemStack> collector);
}