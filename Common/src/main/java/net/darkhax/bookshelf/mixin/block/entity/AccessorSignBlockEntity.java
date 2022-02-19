package net.darkhax.bookshelf.mixin.block.entity;

import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SignBlockEntity.class)
public interface AccessorSignBlockEntity {

    @Invoker("markUpdated")
    void bookshelf$markUpdated();
}