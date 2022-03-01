package net.darkhax.bookshelf.impl.data;

import net.darkhax.bookshelf.api.data.ITagHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagHelperForge implements ITagHelper {

    @Override
    public Tag<Item> itemTag(ResourceLocation tag) {

        return ItemTags.createOptional(tag);
    }

    @Override
    public Tag<Block> blockTag(ResourceLocation tag) {

        return BlockTags.createOptional(tag);
    }

    @Override
    public Tag<EntityType<?>> entityTag(ResourceLocation tag) {

        return EntityTypeTags.createOptional(tag);
    }
}
