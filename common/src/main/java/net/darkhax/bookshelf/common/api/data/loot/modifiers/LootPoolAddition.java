package net.darkhax.bookshelf.common.api.data.loot.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

/**
 * Represents a loot pool entry that should be added to a loot pool.
 *
 * @param id    A unique ID for the individual entry. This should be unique to each entry and is used to help identify
 *              and debug entry additions.
 * @param entry The entry to add to the pool.
 */
public record LootPoolAddition(ResourceLocation id, LootPoolEntryContainer entry) {

}