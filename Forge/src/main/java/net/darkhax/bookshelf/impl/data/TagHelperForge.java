package net.darkhax.bookshelf.impl.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagHelperForge extends TagHelperVanilla {

    @Override
    public TagKey<Item> itemTag(ResourceLocation tag) {

        return ItemTags.create(tag);
    }

    @Override
    public TagKey<Block> blockTag(ResourceLocation tag) {

        return BlockTags.create(tag);
    }
}
