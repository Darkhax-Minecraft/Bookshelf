package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface AccessorTagEntry {

    @Accessor("tag")
    TagKey<Item> bookshelf$tag();

    @Accessor("expand")
    boolean bookshelf$expand();
}