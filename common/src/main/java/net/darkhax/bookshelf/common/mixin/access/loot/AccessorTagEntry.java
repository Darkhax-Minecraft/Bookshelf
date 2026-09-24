package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface AccessorTagEntry {

    @Accessor("tag")
    HolderSet<Item> bookshelf$tag();
}