package net.darkhax.bookshelf.mixin.accessors.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.item.ItemCooldowns$CooldownInstance")
public interface AccessorCooldownInstance {

    @Accessor("startTime")
    int bookshelf$getStartTime();

    @Accessor("endTime")
    int bookshelf$getEndTime();
}