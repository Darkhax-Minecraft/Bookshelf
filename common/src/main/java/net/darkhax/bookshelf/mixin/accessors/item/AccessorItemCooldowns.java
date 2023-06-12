package net.darkhax.bookshelf.mixin.accessors.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemCooldowns.class)
public interface AccessorItemCooldowns {

    @Accessor("cooldowns")
    Map<Item, ?> bookshelf$getCooldowns();

    @Accessor("tickCount")
    int bookshelf$getTickCount();
}
