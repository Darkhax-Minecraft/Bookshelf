package net.darkhax.bookshelf.block;

import javax.annotation.Nullable;

import net.minecraft.item.Item;

public interface IBookshelfBlock {
    
    @Nullable
    default Item.Properties getItemBlockProperties() {
        
        return new Item.Properties();
    }
}