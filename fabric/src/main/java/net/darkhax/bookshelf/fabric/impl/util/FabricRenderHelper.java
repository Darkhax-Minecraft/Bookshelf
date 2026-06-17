package net.darkhax.bookshelf.fabric.impl.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.common.api.util.IRenderHelper;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public class FabricRenderHelper implements IRenderHelper {

    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, int light, int overlay) {
        BookshelfMod.LOG.error("Fluid rendering not supported yet.");
    }
}