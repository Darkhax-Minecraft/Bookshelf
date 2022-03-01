package net.darkhax.bookshelf.api.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ITagHelper {

    Tag<Item> itemTag(ResourceLocation tag);

    Tag<Block> blockTag(ResourceLocation tag);

    Tag<EntityType<?>> entityTag(ResourceLocation tag);
}
