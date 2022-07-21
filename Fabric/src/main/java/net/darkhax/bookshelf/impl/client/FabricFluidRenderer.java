package net.darkhax.bookshelf.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.api.client.FluidRenderer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class FabricFluidRenderer implements FluidRenderer {

    @Override
    public void render(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {

        final Fluid fluid = fluidState.getType();

        if (fluid instanceof FlowingFluid flowing) {

            final FluidRenderHandler renderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

            if (renderHandler != null) {

                final int[] color = argb(renderHandler.getFluidColor(level, pos, fluidState));

                // Fabric's API does not support alpha and decided to use 0 as default.
                if (color[0] == 0) {
                    color[0] = 255;
                }

                final TextureAtlasSprite sprite = renderHandler.getFluidSprites(level, pos, fluidState)[0];
                renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, sprite, light, overlay, color);
            }
        }
    }
}