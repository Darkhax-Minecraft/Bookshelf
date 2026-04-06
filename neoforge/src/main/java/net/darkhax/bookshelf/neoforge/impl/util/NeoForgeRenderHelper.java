package net.darkhax.bookshelf.neoforge.impl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.common.api.util.IRenderHelper;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public class NeoForgeRenderHelper implements IRenderHelper {
    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {
        BookshelfMod.LOG.error("Rendering fluids is not supported yet!", new Throwable());
    }
}