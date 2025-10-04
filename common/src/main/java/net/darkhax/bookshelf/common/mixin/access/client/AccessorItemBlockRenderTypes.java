package net.darkhax.bookshelf.common.mixin.access.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemBlockRenderTypes.class)
public interface AccessorItemBlockRenderTypes {

    @Accessor("TYPE_BY_BLOCK")
    static Map<Block, RenderType> bookshelf$getBlockTypes() {
        throw new IllegalStateException("Accessor code not reachable.");
    }
}