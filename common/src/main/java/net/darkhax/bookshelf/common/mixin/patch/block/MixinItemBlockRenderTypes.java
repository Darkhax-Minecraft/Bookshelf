package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = ItemBlockRenderTypes.class, priority = 900)
public class MixinItemBlockRenderTypes {

    @Shadow
    @Final
    @Mutable
    private static Map<Block, RenderType> TYPE_BY_BLOCK;

    static {
        TYPE_BY_BLOCK = new HashMap<>(TYPE_BY_BLOCK);
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.bindRenderLayers(TYPE_BY_BLOCK::put));
        TYPE_BY_BLOCK = Collections.unmodifiableMap(TYPE_BY_BLOCK);
    }
}
