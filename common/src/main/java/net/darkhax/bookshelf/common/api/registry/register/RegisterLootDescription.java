package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.loot.LootPoolEntryDescriber;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

import java.util.function.BiConsumer;

public record RegisterLootDescription(BiConsumer<LootPoolEntryType, LootPoolEntryDescriber> registryFunc) {
}