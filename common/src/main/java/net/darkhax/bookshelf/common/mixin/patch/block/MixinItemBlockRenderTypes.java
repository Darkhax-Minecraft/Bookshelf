package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = ItemBlockRenderTypes.class)
public class MixinItemBlockRenderTypes {

    @Shadow
    @Final
    @Mutable
    private static Map<Block, RenderType> TYPE_BY_BLOCK;

    static {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.bindRenderLayers(TYPE_BY_BLOCK::put));
    }
}
