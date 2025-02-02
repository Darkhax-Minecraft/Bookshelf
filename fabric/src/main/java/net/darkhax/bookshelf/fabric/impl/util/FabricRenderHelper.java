package net.darkhax.bookshelf.fabric.impl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.common.api.util.IRenderHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public class FabricRenderHelper implements IRenderHelper {

    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {
        final FluidRenderHandler renderer = FluidRenderHandlerRegistry.INSTANCE.get(fluidState.getType());
        if (renderer != null) {
            final int[] color = unpackARGB(renderer.getFluidColor(level, pos, fluidState));
            // Correct Fabric API not supporting alpha.
            if (color[0] == 0) {
                color[0] = 255;
            }
            final TextureAtlasSprite sprite = renderer.getFluidSprites(level, pos, fluidState)[0];
            renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, sprite, light, overlay, color);
        }
    }
}