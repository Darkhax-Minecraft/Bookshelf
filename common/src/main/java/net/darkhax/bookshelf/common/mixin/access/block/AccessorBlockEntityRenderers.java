package net.darkhax.bookshelf.common.mixin.access.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntityRenderers.class)
public interface AccessorBlockEntityRenderers {

    @Invoker("register")
    @SuppressWarnings("rawtypes")
    static void bookshelf$register(BlockEntityType type, BlockEntityRendererProvider renderProvider) {
    }
}