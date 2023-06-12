package net.darkhax.bookshelf.api.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface IItemBlockProvider {

    IItemBlockProvider DEFAULT = new IItemBlockProvider() {
    };

    default BlockItem createItemBlock(Block block) {

        return new BlockItem(block, new Item.Properties());
    }

    default boolean hasItemBlock(Block block) {

        return true;
    }
}