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

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.ImmutableMap;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.model.TRSRTransformation;

@OnlyIn(Dist.CLIENT)
public final class RenderUtils {
    
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    
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
            if (!transformation.equals(TRSRTransformation.identity()))
                copiedTransforms.put(type, TRSRTransformation.blockCenterToCorner(transformation));
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
            
            if (!transformation.equals(TRSRTransformation.identity()))
                builder.put(type, TRSRTransformation.blockCenterToCorner(transformation));
        }
        
        return builder.build();
    }
}