/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class RenderUtils {
    
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
}