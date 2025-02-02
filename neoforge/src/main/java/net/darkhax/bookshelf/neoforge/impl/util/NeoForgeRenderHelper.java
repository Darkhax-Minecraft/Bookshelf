package net.darkhax.bookshelf.neoforge.impl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.common.api.util.IRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class NeoForgeRenderHelper implements IRenderHelper {
    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {
        final IClientFluidTypeExtensions fluidType = IClientFluidTypeExtensions.of(fluidState);
        final ResourceLocation texturePath = fluidType.getStillTexture();
        final int[] color = unpackARGB(fluidType.getTintColor(fluidState, level, pos));
        renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, blockSprite(texturePath), light, overlay, color);
    }
}