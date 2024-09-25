package net.darkhax.bookshelf.common.mixin.access.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseContainerBlockEntity.class)
public interface AccessorBaseContainerBlockEntity {

    @Accessor("name")
    void bookshelf$name(Component name);
}