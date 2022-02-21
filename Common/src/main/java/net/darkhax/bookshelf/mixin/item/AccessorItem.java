package net.darkhax.bookshelf.mixin.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface AccessorItem {

    @Mutable
    @Accessor("category")
    void bookshelf$setCreativeTab(CreativeModeTab tab);

    @Mutable
    @Accessor("craftingRemainingItem")
    void bookshelf$setCraftingRemainder(Item item);
}
