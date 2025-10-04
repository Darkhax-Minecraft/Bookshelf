package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriber;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

import java.util.function.BiConsumer;

public record LootDescriptionAdapter(BiConsumer<LootPoolEntryType, LootPoolEntryDescriber> registryFunc) {
}