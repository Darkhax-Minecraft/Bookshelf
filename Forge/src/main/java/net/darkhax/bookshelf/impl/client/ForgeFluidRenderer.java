package net.darkhax.bookshelf.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.api.client.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class ForgeFluidRenderer implements FluidRenderer {

    @Override
    public void render(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {

        final Fluid fluid = fluidState.getType();
        final ResourceLocation texture = fluid.getAttributes().getStillTexture();
        final int[] color = argb(fluid.getAttributes().getColor(level, pos));
        final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, sprite, light, overlay, color);
    }
}