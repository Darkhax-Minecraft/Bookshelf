/*******************************************************************************************************************
 * Copyright: covers1624
 *
 * License: Lesser General Public License 2.1
 *          https://github.com/TheCBProject/CodeChickenLib/blob/397135f68774e0661d23cb608c893274635d6d6d/LICENSEw
 *
 * Original: https://github.com/TheCBProject/CodeChickenLib/blob/6d2202b3328e564509371db283a40d4b4d752287/src/main/java/codechicken/lib/render/item/CCRenderItem.java
 *
 * Changes: - Reformatted to Bookshelf's code style and formatting.
 *          - Wrote complete Javadocs.
 *          - Renamed class to RenderItemWrapper.
 *          - Renamed/Removed several fields and methods to better fit this project.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.client.render.item;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public class RenderItemWrapper extends RenderItem {

    /**
     * Reference to the pre existing RenderItem instance. Allows for compatibility to be
     * maintained with similar solutions.
     */
    private final RenderItem parent;

    /**
     * Whether or not the wrapper has been initialized.
     */
    private static boolean initialized;

    /**
     * Forge's implementation is private. This one does the same thing.
     */
    private static final Matrix4f flipX;

    static {

        flipX = new Matrix4f();
        flipX.setIdentity();
        flipX.m00 = -1;
    }

    /**
     * Use {@link RenderItemWrapper#getInstance()} to get an instance of the wrapper.
     *
     * @param parentRenderer An instance of the renderer that existed before this one was
     *        constructed.
     */
    private RenderItemWrapper (RenderItem parentRenderer) {

        super(parentRenderer.textureManager, parentRenderer.itemModelMesher.getModelManager(), parentRenderer.itemColors);
        this.itemModelMesher = parentRenderer.itemModelMesher;
        this.parent = parentRenderer;
    }

    /**
     * Initializes the render wrapper. The wrapper can not be initialized twice, so it is safe
     * to use this as a getter.
     *
     * @return The custom render item instance.
     */
    public static RenderItem instance () {

        if (!initialized) {

            Minecraft.getMinecraft().renderItem = new RenderItemWrapper(Minecraft.getMinecraft().getRenderItem());
            initialized = true;
        }

        return Minecraft.getMinecraft().getRenderItem();
    }

    /**
     * Handles the transformations for rendering the model. This method is used to handle the
     * specific cases for the new model types added, such as IGlTransformer.
     *
     * @param stack The ItemStack being rendered.
     * @param model The model being rendered.
     * @param transformType The type of transformation being applied.
     * @param isLeftHand Whether or not this is happening in the left hand.
     * @return The resulting IBakedModel with all transforms applied.
     */
    private IBakedModel handleTransforms (ItemStack stack, IBakedModel model, TransformType transformType, boolean isLeftHand) {

        IBakedModel transformedModel = model;

        if (transformedModel instanceof IGlTransformer) {
            ((IGlTransformer) transformedModel).applyTransforms(transformType, isLeftHand);
        }
        else if (transformedModel instanceof IPerspectiveAwareModel) {
            transformedModel = ForgeHooksClient.handleCameraTransforms(transformedModel, transformType, isLeftHand);
        }
        else if (transformedModel instanceof IStackPerspectiveAwareModel) {

            final Pair<? extends IBakedModel, Matrix4f> pair = ((IStackPerspectiveAwareModel) transformedModel).handlePerspective(stack, transformType);

            if (pair.getRight() != null) {

                final Matrix4f matrix = new Matrix4f(pair.getRight());

                if (isLeftHand) {

                    matrix.mul(flipX, matrix);
                    matrix.mul(matrix, flipX);
                }

                ForgeHooksClient.multiplyCurrentGlMatrix(matrix);
            }

            return pair.getLeft();
        }

        return transformedModel;
    }

    /**
     * Checks if a model is an extended model. Extended in this case applies to any model which
     * implements one of the custom render interfaces provided by the mod.
     *
     * @param model The model to check.
     * @return Whether or not the model is considered extended.
     */
    private boolean isExtendedModel (IBakedModel model) {

        return model instanceof IItemRenderer || model instanceof IGlTransformer || model instanceof IStackPerspectiveAwareModel;
    }

    @Override
    public void renderItem (ItemStack stack, IBakedModel model) {

        if (stack != null && model instanceof IItemRenderer) {

            final IItemRenderer renderer = (IItemRenderer) model;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            renderer.renderItem(stack);
            GlStateManager.popMatrix();
            return;
        }

        this.parent.renderItem(stack, model);
    }

    @Override
    public void renderItemModel (ItemStack stack, IBakedModel bakedModel, TransformType transform, boolean leftHanded) {

        if (stack.getItem() != null)

            if (this.isExtendedModel(bakedModel)) {

                this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();

                final IBakedModel transformedModel = this.handleTransforms(stack, bakedModel, transform, leftHanded);

                this.renderItem(stack, transformedModel);
                GlStateManager.cullFace(GlStateManager.CullFace.BACK);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            }
            else {
                this.parent.renderItemModel(stack, bakedModel, transform, leftHanded);
            }
    }

    @Override
    public void renderItemModelIntoGUI (ItemStack stack, int x, int y, IBakedModel bakedModel) {

        if (this.isExtendedModel(bakedModel)) {

            GlStateManager.pushMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.setupGuiTransform(x, y, bakedModel.isGui3d());

            final IBakedModel transformedModel = this.handleTransforms(stack, bakedModel, ItemCameraTransforms.TransformType.GUI, false);

            this.renderItem(stack, transformedModel);
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }
        else {
            this.parent.renderItemModelIntoGUI(stack, x, y, bakedModel);
        }
    }

    @Override
    public void renderItem (ItemStack stack, TransformType cameraTransformType) {

        if (stack != null) {

            final IBakedModel bakedModel = this.getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null);

            if (this.isExtendedModel(bakedModel)) {
                this.renderItemModel(stack, bakedModel, cameraTransformType, false);
            }

            this.parent.renderItem(stack, cameraTransformType);
        }
    }

    @Override
    public void renderItem (ItemStack stack, EntityLivingBase livingBase, TransformType transform, boolean leftHanded) {

        if (stack != null && livingBase != null && stack.getItem() != null) {

            final IBakedModel bakedModel = this.getItemModelWithOverrides(stack, livingBase.worldObj, livingBase);

            if (this.isExtendedModel(bakedModel)) {
                this.renderItemModel(stack, bakedModel, transform, leftHanded);
            }
            else {
                this.parent.renderItem(stack, livingBase, transform, leftHanded);
            }
        }
    }

    @Override
    public void renderItemIntoGUI (ItemStack stack, int x, int y) {

        final IBakedModel bakedModel = this.getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null);

        if (this.isExtendedModel(bakedModel)) {
            this.renderItemModelIntoGUI(stack, x, y, bakedModel);
        }
        else {
            this.parent.renderItemIntoGUI(stack, x, y);
        }
    }

    @Override
    public void renderItemAndEffectIntoGUI (ItemStack stack, int xPosition, int yPosition) {

        this.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().thePlayer, stack, xPosition, yPosition);
    }

    @Override
    public void renderItemAndEffectIntoGUI (@Nullable EntityLivingBase livingBase, final ItemStack stack, int x, int y) {

        if (stack != null && stack.getItem() != null) {
            try {

                final IBakedModel model = this.getItemModelWithOverrides(stack, (World) null, livingBase);

                if (this.isExtendedModel(model)) {

                    this.zLevel += 50.0F;
                    this.renderItemModelIntoGUI(stack, x, y, model);
                    this.zLevel -= 50.0F;
                }
                else {
                    this.parent.renderItemAndEffectIntoGUI(livingBase, stack, x, y);
                }

            }

            catch (final Exception e) {

                final CrashReport crashreport = CrashReport.makeCrashReport(e, "Rendering item");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                crashreportcategory.setDetail("Item Aux", () -> String.valueOf(stack.getMetadata()));
                crashreportcategory.setDetail("Item NBT", () -> String.valueOf(stack.getTagCompound()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(stack.hasEffect()));
                throw new ReportedException(crashreport);
            }
        }
    }

    @Override
    public void registerItems () {

        // Overridden to prevent items from being re-registered.
    }

    @Override
    public void registerItem (Item item, int subType, String identifier) {

        this.parent.registerItem(item, subType, identifier);
    }

    @Override
    public void isNotRenderingEffectsInGUI (boolean isNot) {

        this.parent.isNotRenderingEffectsInGUI(isNot);
    }

    @Override
    public ItemModelMesher getItemModelMesher () {

        return this.parent.getItemModelMesher();
    }

    @Override
    public boolean shouldRenderItemIn3D (ItemStack stack) {

        return this.parent.shouldRenderItemIn3D(stack);
    }

    @Override
    public IBakedModel getItemModelWithOverrides (ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {

        return this.parent.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn);
    }

    @Override
    public void onResourceManagerReload (IResourceManager resourceManager) {

        this.parent.onResourceManagerReload(resourceManager);
    }
}