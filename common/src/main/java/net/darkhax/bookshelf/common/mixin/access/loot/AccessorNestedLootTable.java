package net.darkhax.bookshelf.common.mixin.access.loot;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NestedLootTable.class)
public interface AccessorNestedLootTable {

    @Accessor("value")
    HolderSet<LootTable> bookshelf$value();
}
