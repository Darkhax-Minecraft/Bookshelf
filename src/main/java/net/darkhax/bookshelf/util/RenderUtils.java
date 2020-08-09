/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

@OnlyIn(Dist.CLIENT)
public final class RenderUtils {
    
    private static final Random RANDOM = new Random();
    private static final BitSet BITS = new BitSet(3);
    
    /**
     * Gets the particle texture for an item stack.
     * 
     * @param stack The item to get the particle texture of.
     * @return The particle texture for the item stack.
     */
    public static TextureAtlasSprite getParticleSprite (ItemStack stack) {
        
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack).getParticleTexture();
    }
    
    /**
     * Gets the particle sprite for a block with world context.
     * 
     * @param state The state to get the particle texture of.
     * @param world The world the block is in.
     * @param pos The position of the block.
     * @return The particle texture for the block state.
     */
    public TextureAtlasSprite getParticleSprite (BlockState state, World world, BlockPos pos) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state, world, pos);
    }
    
    /**
     * Gets the particle texture for a block state.
     * 
     * @param state The state to get the particle texture of.
     * @return The particle texture for the block state.
     */
    public static TextureAtlasSprite getParticleSprite (BlockState state) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }
    
    /**
     * Gets a model by it's location name.
     * 
     * @param name The model location name.
     * @return The associated model.
     */
    public static IBakedModel getModel (ModelResourceLocation name) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(name);
    }
    
    /**
     * Gets a model for an item stack.
     * 
     * @param stack The stack to get the model for.
     * @return The model for the stack.
     */
    public static IBakedModel getModel (ItemStack stack) {
        
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
    }
    
    /**
     * Gets a model for a block state.
     * 
     * @param state The state to get the model for.
     * @return The model for the state.
     */
    public static IBakedModel getModel (BlockState state) {
        
        return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
    }
    
    /**
     * Renders a block model. Allows the sites rendered to be specifically controlled by the
     * caller.
     * 
     * @param renderer The block model renderer instance.
     * @param world The world instance.
     * @param model The model to render.
     * @param state The state of the block.
     * @param pos The position of the block.
     * @param matrix The render matrix.
     * @param buffer The render buffer.
     * @param sides The sides of the model to render.
     */
    public static void renderModel (BlockModelRenderer renderer, IBlockDisplayReader world, IBakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, IVertexBuilder buffer, Direction[] sides) {
        
        final IModelData modelData = model.getModelData(world, pos, state, EmptyModelData.INSTANCE);
        
        // Renders only the sided model quads.
        for (final Direction side : sides) {
            
            RANDOM.setSeed(0L);
            final List<BakedQuad> sidedQuads = model.getQuads(state, side, RANDOM, modelData);
            
            if (!sidedQuads.isEmpty()) {
                
                final int lightForSide = WorldRenderer.getPackedLightmapCoords(world, state, pos.offset(side));
                renderer.renderQuadsFlat(world, state, pos, lightForSide, OverlayTexture.NO_OVERLAY, false, matrix, buffer, sidedQuads, BITS);
            }
        }
        
        // Renders the non-sided model quads.
        RANDOM.setSeed(0L);
        final List<BakedQuad> unsidedQuads = model.getQuads(state, null, RANDOM, modelData);
        
        if (!unsidedQuads.isEmpty()) {
            
            renderer.renderQuadsFlat(world, state, pos, -1, OverlayTexture.NO_OVERLAY, true, matrix, buffer, unsidedQuads, BITS);
        }
    }
    
    /**
     * Directly get the name of a render state such as a RenderType. This field is normally
     * private and has been made accessible through access transformers. This method is
     * required for use in dev environments.
     * 
     * @param state The state to get the name of.
     * @return The name of the state.
     */
    public static String getName (RenderState state) {
        
        return state.name;
    }
}