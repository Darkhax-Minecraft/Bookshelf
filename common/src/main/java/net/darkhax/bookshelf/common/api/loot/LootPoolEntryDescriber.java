package net.darkhax.bookshelf.common.api.loot;

import net.minecraft.server.MinecraftServer;
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
     * @param server    The server instance.
     * @param entry     The loot pool entry to be processed.
     * @param collector Collects entries from the entry into the desired format.
     */
    void getPotentialDrops(@NotNull MinecraftServer server, @NotNull LootPoolEntryContainer entry, Consumer<ItemStack> collector);
}