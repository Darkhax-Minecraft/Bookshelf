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

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.ImmutableMap;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ThreadDownloadImageData;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.model.TRSRTransformation;

@OnlyIn(Dist.CLIENT)
public final class RenderUtils {
    
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private RenderUtils() {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Registers a LayerRenderer to both the Alex and Steve player renderers.
     *
     * @param layer The LayerRenderer to register.
     */
    public static void registerPlayerLayer (LayerRenderer<EntityLivingBase> layer) {
        
        getSteveRenderer().addLayer(layer);
        getAlexRenderer().addLayer(layer);
    }
    
    /**
     * Gets the RenderPlayer instance for the Steve model.
     *
     * @return RederPlayer The RenderPlayer instance for the Steve model.
     */
    public static RenderPlayer getSteveRenderer () {
        
        return getPlayerRenderer("default");
    }
    
    /**
     * Gets the RenderPlayer instance for the Alex model.
     *
     * @return RenderPlayer The RenderPlayer instance for the Alex model.
     */
    public static RenderPlayer getAlexRenderer () {
        
        return getPlayerRenderer("slim");
    }
    
    /**
     * Gets a RenderPlayer instance for the specified model type. Currently only Alex (slim)
     * and Steve (default) are added by vanilla. Can be null if no renderer was found.
     *
     * @param type The name of the model type to retrieve.
     * @return RenderPlayer The RenderPlayer instance for the specified model.
     */
    public static RenderPlayer getPlayerRenderer (String type) {
        
        return Minecraft.getInstance().getRenderManager().getSkinMap().get(type);
    }
    
    /**
     * Attempts to download a resource from the web and load it into the game. If the resource
     * can not be downloaded successfully. Wraps
     * {@link #downloadResource(String, ResourceLocation, ResourceLocation, IImageBuffer)} but
     * returns the output ResourceLocation.
     *
     * @param url The URL to download the resource from. This should be the raw/source url.
     * @param outputResource The ResourceLocation to use for the newly downloaded resource.
     * @param defaultResource The default texture to use, on the chance that it fails to
     *        download a texture. This must be a valid texture, or else you will get a missing
     *        texture.
     * @param buffer A special buffer to use when downloading the image. It is okay to pass
     *        null for this if you don't want anything fancy.
     * @return The output resource location.
     */
    public static ResourceLocation downloadResourceLocation (String url, ResourceLocation outputResource, ResourceLocation defaultResource, IImageBuffer buffer) {
        
        downloadResource(url, outputResource, defaultResource, buffer);
        return outputResource;
    }
    
    /**
     * Attempts to download a resource from the web and load it into the game. If the resource
     * can not be downloaded successfully.
     *
     * @param url The URL to download the resource from. This should be the raw/source url.
     * @param outputResource The ResourceLocation to use for the newly downloaded resource.
     * @param defaultResource The default texture to use, on the chance that it fails to
     *        download a texture. This must be a valid texture, or else you will get a missing
     *        texture.
     * @param buffer A special buffer to use when downloading the image. It is okay to pass
     *        null for this if you don't want anything fancy.
     * @return The downloaded image data.
     */
    public static ThreadDownloadImageData downloadResource (String url, ResourceLocation outputResource, ResourceLocation defaultResource, IImageBuffer buffer) {
        
        final TextureManager manager = Minecraft.getInstance().getTextureManager();
        
        ThreadDownloadImageData imageData = (ThreadDownloadImageData) manager.getTexture(outputResource);
        
        if (imageData == null) {
            
            imageData = new ThreadDownloadImageData(null, url, defaultResource, buffer);
            manager.loadTexture(outputResource, imageData);
        }
        
        return imageData;
    }
    
    /**
     * Gets the camera for a specific entity.
     *
     * @param entity The entity to get the camera for.
     * @param partialTicks The partial ticks for the camera.
     * @return The camera for the entity.
     */
    public static Frustum getCamera (Entity entity, float partialTicks) {
        
        final double cameraX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        final double cameraY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        final double cameraZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        
        final Frustum camera = new Frustum();
        camera.setPosition(cameraX, cameraY, cameraZ);
        return camera;
    }
    
    /**
     * Translates the render state to be relative to the player's position. Allows for
     * rendering at a static world position that is not tied to the player's position.
     *
     * @param pos The BlockPos The position to translate to within the world.
     * @param offset If the position should be offset by half a block.
     */
    public static void translateAgainstPlayer (BlockPos pos, boolean offset) {
        
        final float x = (float) (pos.getX() - TileEntityRendererDispatcher.staticPlayerX);
        final float y = (float) (pos.getY() - TileEntityRendererDispatcher.staticPlayerY);
        final float z = (float) (pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
        
        if (offset) {
            GlStateManager.translatef(x + 0.5f, y + 0.5f, z + 0.5f);
        }
        else {
            GlStateManager.translatef(x, y, z);
        }
    }
    
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
     * Gets the missing quads for a given state.
     *
     * @param state The state to use.
     * @param side The side to get quads for.
     * @param rand A random long seed.
     * @return The missing quads for the missing model.
     */
    public static List<BakedQuad> getMissingquads (IBlockState state, EnumFacing side, Random rand) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);
    }
    
    /**
     * Attempts to get a block sprite from a block state.
     *
     * @param state The block state to get the sprite for.
     * @return The block sprite.
     */
    public static TextureAtlasSprite getSprite (IBlockState state) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }
    
    /**
     * Creates the basic TRSRTransformations for a perspective aware model.
     *
     * @param model The model to get the transforms for.
     * @return An immutable map of all the transforms.
     */
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
     * Gets a baked model for the passed state. This model is pulled from the baked model
     * store.
     *
     * @param state The state to get the model for.
     * @return The model for that state.
     */
    public static IBakedModel getModelForState (IBlockState state) {
        
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
}