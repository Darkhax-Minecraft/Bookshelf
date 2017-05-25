/**
 * This class was created by <SanAndreasP>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 22:33:44 (GMT)]
 *
 * Changes
 * - Added support for the ChestBuilder system
 * - Repackaged and formated for Bookshelf standards.
 * - Added javadocs.
 */
package net.darkhax.bookshelf.client.render.tileentity;

import org.lwjgl.opengl.GL11;

import net.darkhax.bookshelf.block.tileentity.TileEntityBasicChest;
import net.darkhax.bookshelf.builder.ChestBuilder.IRenderHook;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * This class is part of the ChestBuilder system. Please see
 * {@link net.darkhax.bookshelf.builder.ChestBuilder} for more info.
 */
public class TileEntityBasicChestRenderer extends TileEntitySpecialRenderer<TileEntityBasicChest> {

    /**
     * The single chest model.
     */
    private final ModelChest modelSingle = new ModelChest();

    /**
     * The large chest model.
     */
    private final ModelChest modelDouble = new ModelLargeChest();

    public TileEntityBasicChestRenderer () {

    }

    @Override
    public void renderTileEntityAt (TileEntityBasicChest te, double x, double y, double z, float partialTicks, int destroyStage) {

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        int meta;

        if (te.hasWorld()) {
            meta = te.getBlockMetadata();
            te.checkForAdjacentChests();
        }
        else {
            meta = 0;
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {

            ModelChest model;

            // Single Chest
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                model = this.modelSingle;

                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(GL11.GL_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                }

                else {
                    this.bindTexture(te.getType().getNormalTexture());
                }
            }

            // Double Chest
            else {
                model = this.modelDouble;

                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(GL11.GL_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                }
                else {
                    this.bindTexture(te.getType().getDoubleTexture());
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            if (destroyStage < 0) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.translate(x, y + 1.0F, z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int angle = 0;

            if (meta == 2) {
                angle = 180;
            }

            if (meta == 3) {
                angle = 0;
            }

            if (meta == 4) {
                angle = 90;
            }

            if (meta == 5) {
                angle = -90;
            }

            if (meta == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0F, 0.0F, 0.0F);
            }

            if (meta == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0F, 0.0F, -1.0F);
            }

            GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            float lidAngle = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null) {
                final float adjLidAngle = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (adjLidAngle > lidAngle) {
                    lidAngle = adjLidAngle;
                }
            }

            if (te.adjacentChestXNeg != null) {
                final float adjLidAngle = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (adjLidAngle > lidAngle) {
                    lidAngle = adjLidAngle;
                }
            }

            lidAngle = 1.0F - lidAngle;
            lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
            model.chestLid.rotateAngleX = -(lidAngle * ((float) Math.PI / 2F));

            for (final IRenderHook renderHook : te.getType().renderHooks()) {

                renderHook.setup(te, x, y, z, partialTicks, destroyStage, this, model, model == this.modelSingle);
            }

            model.renderAll();

            for (final IRenderHook renderHook : te.getType().renderHooks()) {

                renderHook.render(te, x, y, z, partialTicks, destroyStage, this, model, model == this.modelSingle);
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0) {
                GlStateManager.matrixMode(GL11.GL_TEXTURE);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            }
        }
    }

    @Override
    public void bindTexture (ResourceLocation location) {

        super.bindTexture(location);
    }
}