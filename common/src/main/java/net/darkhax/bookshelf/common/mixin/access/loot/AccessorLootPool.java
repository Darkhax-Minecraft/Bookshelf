package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin(LootPool.class)
public interface AccessorLootPool {

    @Accessor("entries")
    List<LootPoolEntryContainer> bookshelf$entries();

    @Accessor("entries")
    @Mutable
    void bookshelf$setEntries(List<LootPoolEntryContainer> entries);

    @Accessor("condition")
    Optional<Holder<LootItemCondition>> bookshelf$condition();

    @Accessor("modifier")
    Optional<Holder<LootItemFunction>> bookshelf$modifier();

    @Accessor("rolls")
    Holder<ContextIntProvider> bookshelf$rolls();

    @Accessor("bonusRolls")
    Holder<ContextIntProvider> bookshelf$bonusRolls();
}