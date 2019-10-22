/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.model.TRSRTransformation;

@OnlyIn(Dist.CLIENT)
public final class RenderUtils {
    
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    
    /**
     * An internal random reference. The seed should always be set to 41 before using it.
     */
    private static final Random ITEM_RANDOM = new Random();
    
    /**
     * Gets the particle sprite for an ItemStack.
     *
     * @param stack The ItemStack to get the particle for.
     * @return A TextureAtlasSprite that points to the particle texture for the ItemStack.
     */
    public static TextureAtlasSprite getParticleTexture (ItemStack stack) {
        
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack).getParticleTexture();
    }
    
    /**
     * Gets the baked model for a ModelResourceLocation.
     *
     * @param name The location to get the model from.
     * @return The baked model that was found.
     */
    public static IBakedModel getBakedModel (ModelResourceLocation name) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(name);
    }
    
    /**
     * Gets the baked model for an ItemStack.
     *
     * @param stack The stack to get the model of.
     * @return The baked model for the ItemStack.
     */
    public static IBakedModel getBakedModel (ItemStack stack) {
        
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
    }
    
    /**
     * Attempts to get a block sprite from a block state.
     *
     * @param state The block state to get the sprite for.
     * @return The block sprite.
     */
    public static TextureAtlasSprite getSprite (BlockState state) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }
    
    /**
     * Gets a baked model for the passed state. This model is pulled from the baked model
     * store.
     *
     * @param state The state to get the model for.
     * @return The model for that state.
     */
    public static IBakedModel getModelForState (BlockState state) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
    }
    
    /**
     * Finds and saves a texture from the game data to a file.
     *
     * @param textureId The ID of the texture to save.
     * @param file The file to save the texture to.
     */
    public static void saveTextureToFile (int textureId, File file) {
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        
        final int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        final int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        final int size = width * height;
        
        final BufferedImage bufferedimage = new BufferedImage(width, height, 2);
        
        final IntBuffer buffer = BufferUtils.createIntBuffer(size);
        final int[] data = new int[size];
        
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        buffer.get(data);
        bufferedimage.setRGB(0, 0, width, height, data, 0, width);
        
        try {
            ImageIO.write(bufferedimage, "png", file);
        }
        
        catch (final Exception e) {
            
            Bookshelf.LOG.error("Failed to save texture {} to {}.", textureId, file.getName());
            Bookshelf.LOG.catching(e);
        }
    }
    
    /**
     * Builds a new copy of the transformations for a baked model.
     *
     * @param model The model to pull the transformation data from.
     * @return An immutable map which maps transformation types to their transformation data in
     *         the base model.
     */
    @SuppressWarnings("deprecation")
    public static ImmutableMap<TransformType, TRSRTransformation> copyTransforms (IBakedModel model) {
        
        final ImmutableMap.Builder<TransformType, TRSRTransformation> copiedTransforms = ImmutableMap.builder();
        
        // Iterate through all the item transform types
        for (final TransformType type : TransformType.values()) {
            
            // Copies transformation for the transform type.
            final TRSRTransformation transformation = new TRSRTransformation(model.handlePerspective(type).getRight());
            
            // Filters out the base transformation.
            if (!transformation.equals(TRSRTransformation.identity())) {
                copiedTransforms.put(type, TRSRTransformation.blockCenterToCorner(transformation));
            }
        }
        
        return copiedTransforms.build();
    }
    
    /**
     * Creates the basic TRSRTransformations for a perspective aware model.
     *
     * @param model The model to get the transforms for.
     * @return An immutable map of all the transforms.
     */
    @SuppressWarnings("deprecation")
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getBasicTransforms (IBakedModel model) {
        
        final ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
        
        for (final ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values()) {
            
            final TRSRTransformation transformation = new TRSRTransformation(model.handlePerspective(type).getRight());
            
            if (!transformation.equals(TRSRTransformation.identity())) {
                builder.put(type, TRSRTransformation.blockCenterToCorner(transformation));
            }
        }
        
        return builder.build();
    }
    
    /**
     * Renders an item and it's effect layer int a GUI. Allows the color of the item to be
     * changed.
     * 
     * @param stack The item to render.
     * @param xPosition The x coordinate to render the item at.
     * @param yPosition The y coordinate to render the item at.
     * @param color The color to render the item. Supports alpha values.
     */
    public static void renderItemAndEffectIntoGUI (ItemStack stack, int xPosition, int yPosition, int color) {
        
        renderItemAndEffectIntoGUI(stack, xPosition, yPosition, color, DEFAULT_QUAD_COLORS);
    }
    
    /**
     * Renders an item and it's effect layer int a GUI. Allows the color of the item to be
     * changed.
     * 
     * @param stack The item to render.
     * @param xPosition The x coordinate to render the item at.
     * @param yPosition The y coordinate to render the item at.
     * @param color The color to render the item. Supports alpha values.
     * @param colorHandler A function that handles tint index colors.
     */
    public static void renderItemAndEffectIntoGUI (ItemStack stack, int xPosition, int yPosition, int color, IQuadColorHandler colorHandler) {
        
        final Minecraft mc = Minecraft.getInstance();
        renderItemAndEffectIntoGUI(mc.getTextureManager(), mc.getItemRenderer(), mc.player, stack, xPosition, yPosition, color, colorHandler);
    }
    
    /**
     * Renders an item into a gui.
     * 
     * @param textureManager The texture manager instance.
     * @param itemRenderer The item renderer.
     * @param entityIn The entity, usually the client player.
     * @param itemIn The item to render.
     * @param x The x coordinate of the item.
     * @param y The y coordinate of the item.
     * @param color The color of the item.
     */
    public static void renderItemAndEffectIntoGUI (TextureManager textureManager, ItemRenderer itemRenderer, @Nullable LivingEntity entityIn, ItemStack itemIn, int x, int y, int color) {
        
        renderItemAndEffectIntoGUI(textureManager, itemRenderer, entityIn, itemIn, x, y, color, DEFAULT_QUAD_COLORS);
    }
    
    /**
     * Renders an item into a gui.
     * 
     * @param textureManager The texture manager instance.
     * @param itemRenderer The item renderer.
     * @param entityIn The entity, usually the client player.
     * @param itemIn The item to render.
     * @param x The x coordinate of the item.
     * @param y The y coordinate of the item.
     * @param color The color of the item.
     * @param colorHandler A function that handles tint index colors.
     */
    public static void renderItemAndEffectIntoGUI (TextureManager textureManager, ItemRenderer itemRenderer, @Nullable LivingEntity entityIn, ItemStack itemIn, int x, int y, int color, IQuadColorHandler colorHandler) {
        
        if (!itemIn.isEmpty()) {
            
            itemRenderer.zLevel += 50f;
            
            try {
                
                renderItemModelIntoGUI(textureManager, itemRenderer, itemIn, x, y, color, itemRenderer.getItemModelWithOverrides(itemIn, (World) null, entityIn), colorHandler);
            }
            
            catch (final Exception exception) {
                
                final CrashReport crashreport = CrashReport.makeCrashReport(exception, "Rendering item");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", () -> String.valueOf(itemIn.getItem()));
                crashreportcategory.addDetail("Registry Name", () -> String.valueOf(itemIn.getItem().getRegistryName()));
                crashreportcategory.addDetail("Item Damage", () -> String.valueOf(itemIn.getDamage()));
                crashreportcategory.addDetail("Item NBT", () -> String.valueOf(itemIn.getTag()));
                crashreportcategory.addDetail("Item Foil", () -> String.valueOf(itemIn.hasEffect()));
                throw new ReportedException(crashreport);
            }
            
            itemRenderer.zLevel -= 50.0F;
        }
    }
    
    /**
     * Renders an Item into a GUI.
     * 
     * @param textureManager The texture manager instance.
     * @param itemRenderer The item renderer.
     * @param stack The item to render.
     * @param x The x coordinate of the item.
     * @param y The y coordinate of the item.
     * @param color The color of the item.
     * @param bakedmodel The model of the item.
     */
    @SuppressWarnings("deprecation")
    public static void renderItemModelIntoGUI (TextureManager textureManager, ItemRenderer itemRenderer, ItemStack stack, int x, int y, int color, IBakedModel bakedmodel) {
        
        renderItemModelIntoGUI(textureManager, itemRenderer, stack, x, y, color, bakedmodel, DEFAULT_QUAD_COLORS);
    }
    
    /**
     * Renders an Item into a GUI.
     * 
     * @param textureManager The texture manager instance.
     * @param itemRenderer The item renderer.
     * @param stack The item to render.
     * @param x The x coordinate of the item.
     * @param y The y coordinate of the item.
     * @param color The color of the item.
     * @param bakedmodel The model of the item.
     * @param colorHandler A function that handles tint index colors.
     */
    @SuppressWarnings("deprecation")
    public static void renderItemModelIntoGUI (TextureManager textureManager, ItemRenderer itemRenderer, ItemStack stack, int x, int y, int color, IBakedModel bakedmodel, IQuadColorHandler colorHandler) {
        
        GlStateManager.pushMatrix();
        textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        setupGuiTransform(x, y, bakedmodel.isGui3d(), itemRenderer.zLevel);
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        renderItem(itemRenderer, stack, bakedmodel, color, colorHandler);
        GlStateManager.disableAlphaTest();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }
    
    /**
     * Sets up the transforms and scale for rendering an item in a gui.
     * 
     * @param xPosition The x coord of the item.
     * @param yPosition The y coord of the item.
     * @param isGui3d Whether or not the gui is 3d.
     * @param zLevel The zlevel of the item.
     */
    public static void setupGuiTransform (int xPosition, int yPosition, boolean isGui3d, float zLevel) {
        
        GlStateManager.translatef(xPosition, yPosition, 100.0F + zLevel);
        GlStateManager.translatef(8.0F, 8.0F, 0.0F);
        GlStateManager.scalef(1.0F, -1.0F, 1.0F);
        GlStateManager.scalef(16.0F, 16.0F, 16.0F);
        
        if (isGui3d) {
            
            GlStateManager.enableLighting();
        }
        
        else {
            
            GlStateManager.disableLighting();
        }
        
    }
    
    /**
     * Renders an item with an item renderer. Will not render if the item is empty. It also
     * offsets the item.
     * 
     * @param itemRenderer The item renderer.
     * @param model The model to render.
     * @param color The color for the model.
     * @param stack The ItemStack instance.
     */
    public static void renderItem (ItemRenderer itemRenderer, ItemStack stack, IBakedModel model, int color) {
        
        renderItem(itemRenderer, stack, model, color, DEFAULT_QUAD_COLORS);
    }
    
    /**
     * Renders an item with an item renderer. Will not render if the item is empty. It also
     * offsets the item.
     * 
     * @param itemRenderer The item renderer.
     * @param model The model to render.
     * @param color The color for the model.
     * @param stack The ItemStack instance.
     * @param colorHandler A function that handles tint index colors.
     */
    public static void renderItem (ItemRenderer itemRenderer, ItemStack stack, IBakedModel model, int color, IQuadColorHandler colorHandler) {
        
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            
            renderModel(itemRenderer, model, color, stack, colorHandler);
            
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * Renders the actual quads of an item using an ItemRenderer.
     * 
     * @param itemRenderer The item renderer.
     * @param model The model to render.
     * @param color The color for the model.
     * @param stack The ItemStack instance.
     */
    public static void renderModel (ItemRenderer itemRenderer, IBakedModel model, int color, ItemStack stack) {
        
        renderModel(itemRenderer, model, color, stack, DEFAULT_QUAD_COLORS);
    }
    
    /**
     * Renders the actual quads of an item using an ItemRenderer.
     * 
     * @param itemRenderer The item renderer.
     * @param model The model to render.
     * @param color The color for the model.
     * @param stack The ItemStack instance.
     * @param colorHandler A function that handles tint index colors.
     */
    public static void renderModel (ItemRenderer itemRenderer, IBakedModel model, int color, ItemStack stack, IQuadColorHandler colorHandler) {
        
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        
        for (final Direction direction : Direction.values()) {
            
            ITEM_RANDOM.setSeed(42L);
            renderQuads(bufferbuilder, model.getQuads((BlockState) null, direction, ITEM_RANDOM), color, stack, colorHandler);
        }
        
        ITEM_RANDOM.setSeed(42L);
        renderQuads(bufferbuilder, model.getQuads((BlockState) null, (Direction) null, ITEM_RANDOM), color, stack, colorHandler);
        tessellator.draw();
    }
    
    public static void renderQuads (BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        
        renderQuads(renderer, quads, color, stack, DEFAULT_QUAD_COLORS);
    }
    
    public static void renderQuads (BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack, IQuadColorHandler colorHandler) {
        
        for (final BakedQuad quad : quads) {
            
            final int quadColor = colorHandler.getColorForQuad(stack, quad, color);
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, quad, quadColor);
        }
    }
    
    public static final IQuadColorHandler DEFAULT_QUAD_COLORS = (stack, quad, providedColor) -> {
        
        if (providedColor != -1 || stack.isEmpty() || !quad.hasTintIndex()) {
            
            return providedColor;
        }
        
        else {
            
            int color = Minecraft.getInstance().getItemColors().getColor(stack, quad.getTintIndex());
            color = color | -16777216;
            return color;
        }
    };
    
    @FunctionalInterface
    public static interface IQuadColorHandler {
        
        int getColorForQuad (ItemStack stack, BakedQuad quad, int providedColor);
    }
    
    /**
     * Draws a textured rectangle onto the screen. Texture is taken from the currently bound
     * texture.
     * 
     * @param x The starting x position to draw the rectangle at.
     * @param y The starting y position to draw the rectangle at.
     * @param z The z level or blit offset of the gui.
     * @param u The starting x texture coordinate.
     * @param v The starting y texture coordinate.
     * @param width The width of the texture segment.
     * @param height The height of the texture segment.
     * @param textureWidth The over all width of the texture.
     * @param textureHeight The over all height of the texture.
     * @param color The color to tint the texture.
     */
    public static void drawTexturedRect (int x, int y, int z, float u, float v, int width, int height, float textureWidth, float textureHeight, int color) {
        
        drawTexturedRect(x, y, z, u, v, width, height, textureWidth, textureHeight, color >> 16 & 0xFF, color >> 8 & 0xFF, color >> 0 & 0xFF, color >> 24 & 0xff);
    }
    
    /**
     * Draws a textured rectangle onto the screen. Texture is taken from the currently bound
     * texture.
     * 
     * @param x The starting x position to draw the rectangle at.
     * @param y The starting y position to draw the rectangle at.
     * @param z The z level or blit offset of the gui.
     * @param u The starting x texture coordinate.
     * @param v The starting y texture coordinate.
     * @param width The width of the texture segment.
     * @param height The height of the texture segment.
     * @param textureWidth The over all width of the texture.
     * @param textureHeight The over all height of the texture.
     * @param red The red component.
     * @param green The green component.
     * @param blue The blue component.
     * @param alpha The alpha component.
     */
    public static void drawTexturedRect (int x, int y, int z, float u, float v, int width, int height, float textureWidth, float textureHeight, int red, int green, int blue, int alpha) {
        
        final float widthRatio = 1.0F / textureWidth;
        final float heightRatio = 1.0F / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x, y + height, z).tex(u * widthRatio, (v + height) * heightRatio).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + width, y + height, z).tex((u + width) * widthRatio, (v + height) * heightRatio).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + width, y, z).tex((u + width) * widthRatio, v * heightRatio).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, z).tex(u * widthRatio, v * heightRatio).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }
}