package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;

@Mixin(LootItem.class)
public interface AccessorLootItem {

    @Invoker("<init>")
    static LootItem bookshelf$create(Holder<Item> item, int weight, int quality, Optional<Holder<LootItemCondition>> condition, Optional<Holder<LootItemFunction>> modifier) {
        return null;
    }

    @Accessor("item")
    Holder<Item> bookshelf$item();
}