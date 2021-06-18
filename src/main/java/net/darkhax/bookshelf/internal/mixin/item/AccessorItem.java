package net.darkhax.bookshelf.internal.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

@Mixin(Item.class)
public interface AccessorItem {
    
    @Accessor("group")
    public void setItemGroup (ItemGroup group);
    
    @Accessor("rarity")
    public void setRarity (Rarity rarity);
    
    @Accessor("fireproof")
    public void setFireproof (boolean fireproof);
    
    @Accessor("foodComponent")
    public void setFood (FoodComponent rarity);
}