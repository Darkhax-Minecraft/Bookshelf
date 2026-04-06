package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin(LootTable.class)
public interface AccessorLootTable {

    @Accessor("randomSequence")
    Optional<Identifier> bookshelf$randomSequence();

    @Accessor("pools")
    List<LootPool> bookshelf$pools();

    @Accessor("functions")
    List<LootItemFunction> bookshelf$functions();
}