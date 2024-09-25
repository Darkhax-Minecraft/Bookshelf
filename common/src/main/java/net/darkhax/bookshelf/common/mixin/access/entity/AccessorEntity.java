package net.darkhax.bookshelf.common.mixin.access.entity;

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface AccessorEntity {

    @Invoker("createHoverEvent")
    HoverEvent bookshelf$createHoverEvent();
}