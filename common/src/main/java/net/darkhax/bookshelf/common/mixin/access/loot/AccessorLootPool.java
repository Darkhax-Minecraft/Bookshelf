package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface AccessorLootPool {

    @Accessor("entries")
    List<LootPoolEntryContainer> bookshelf$entries();

    @Accessor("entries")
    @Mutable
    void bookshelf$setEntries(List<LootPoolEntryContainer> entries);

    @Accessor("conditions")
    List<LootItemCondition> bookshelf$conditions();

    @Accessor("functions")
    List<LootItemFunction> functions();

    @Accessor("rolls")
    NumberProvider bookshelf$rolls();

    @Accessor("bonusRolls")
    NumberProvider bookshelf$bonusRolls();
}