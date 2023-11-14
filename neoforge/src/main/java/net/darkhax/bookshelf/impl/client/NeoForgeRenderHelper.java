package net.darkhax.bookshelf.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.api.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class NeoForgeRenderHelper implements RenderHelper {

    @Override
    public void renderFluidBox(PoseStack pose, FluidState fluidState, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay) {

        final IClientFluidTypeExtensions fluid = IClientFluidTypeExtensions.of(fluidState);

        if (fluid != null) {

            final ResourceLocation texture = fluid.getStillTexture();

            if (texture != null) {

                final int[] color = argb(fluid.getTintColor(fluidState, level, pos));
                final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
                renderBox(bufferSource.getBuffer(RenderType.translucent()), pose, sprite, light, overlay, color);
            }
        }
    }
}