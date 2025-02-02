package net.darkhax.bookshelf.forge.impl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.common.api.util.IRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class ForgeRenderHelper implements IRenderHelper {
    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {
        IClientFluidTypeExtensions fluidType = IClientFluidTypeExtensions.of(fluidState);
        if (fluidType != null) {
            final ResourceLocation texturePath = fluidType.getStillTexture();
            if (texturePath != null) {
                final int[] color = unpackARGB(fluidType.getTintColor(fluidState, level, pos));
                renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, blockSprite(texturePath), light, overlay, color);
            }
        }
    }
}