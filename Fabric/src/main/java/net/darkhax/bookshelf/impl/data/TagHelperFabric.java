package net.darkhax.bookshelf.impl.data;

import net.darkhax.bookshelf.api.data.ITagHelper;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagHelperFabric implements ITagHelper {

    @Override
    public Tag<Item> itemTag(ResourceLocation tag) {

        return TagFactory.ITEM.create(tag);
    }

    @Override
    public Tag<Block> blockTag(ResourceLocation tag) {

        return TagFactory.BLOCK.create(tag);
    }

    @Override
    public Tag<EntityType<?>> entityTag(ResourceLocation tag) {

        return TagFactory.ENTITY_TYPE.create(tag);
    }
}
