package net.darkhax.bookshelf.client;

import net.darkhax.bookshelf.client.particle.OpenEntityDiggingFX;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderUtils {
    
    /**
     * Registers a LayerRenderer with a RendererLivingEntity. Only works for living entities.
     * 
     * @param entityClass The class of the Entity to register the renderer to.
     * @param layer The LayerRenderer to register to that entity.
     * @return boolean Whether or not the layer was succesfully registered.
     */
    public static boolean registerEntityLayer (Class<? extends Entity> entityClass, LayerRenderer layer) {
        
        RendererLivingEntity renderer = getEntityRenderer(entityClass);
        
        if (renderer != null) {
            
            renderer.addLayer(layer);
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a RendererLivingEntity instance from the render manager based on the provided
     * entity class. Only works for living entities.
     * 
     * @param entityClass The class of the Entity to register the renderer to.
     * @return
     */
    public static RendererLivingEntity getEntityRenderer (Class<? extends Entity> entityClass) {
        
        Render renderer = Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entityClass);
        return (renderer instanceof RendererLivingEntity) ? (RendererLivingEntity) renderer : null;
    }
    
    /**
     * Registers a LayerRenderer to both the Alex and Steve player renderers.
     * 
     * @param layer The LayerRenderer to register.
     */
    public static void registerPlayerLayer (LayerRenderer layer) {
        
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
        
        return Minecraft.getMinecraft().getRenderManager().skinMap.get(type);
    }
    
    /**
     * Attempts to set the skin texture for a player to the resource location passed to it.
     * 
     * @param player The AbstractClientPlayer to set the texture to.
     * @param skin The ResourceLocation for the new skin texture.
     * @return boolean Whether or not the texture was set.
     */
    public static boolean setPlayerSkin (AbstractClientPlayer player, ResourceLocation skin) {
        
        if (player.hasPlayerInfo() && skin != null) {
            
            player.getPlayerInfo().locationSkin = skin;
            return true;
        }
        
        return false;
    }
    
    /**
     * Attempts to set the cape texture for a player to the resource location passed to it.
     * 
     * @param player The AbstractClientPlayer to set the texture to.
     * @param cape The ResourceLocation for the new cape texture.
     * @return boolean Whether or not the texture was set.
     */
    public static boolean setPlayerCape (AbstractClientPlayer player, ResourceLocation cape) {
        
        if (player.hasPlayerInfo() && cape != null) {
            
            player.getPlayerInfo().locationCape = cape;
            return true;
        }
        
        return false;
    }
    
    /**
     * Spawns the digging particles for a block, similarly to the normal block hit effect
     * particle. The intended use of this method is to override the block hit effects.
     * 
     * @param renderer The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effects in.
     * @param pos The position to spawn the particles at.
     * @param side The side offset for the effect.
     * @return boolean Whether or not the effect actually spawned.
     */
    public static boolean spawnDigParticles (EffectRenderer renderer, IBlockState state, World world, BlockPos pos, EnumFacing side) {
        
        Block block = state.getBlock();
        
        if (block != null && block.getRenderType() != -1) {
            
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float offset = 0.1F;
            
            double xOffset = (double) x + Constants.RANDOM.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double) (offset * 2.0F)) + (double) offset + block.getBlockBoundsMinX();
            double yOffset = (double) y + Constants.RANDOM.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double) (offset * 2.0F)) + (double) offset + block.getBlockBoundsMinY();
            double zOffset = (double) z + Constants.RANDOM.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double) (offset * 2.0F)) + (double) offset + block.getBlockBoundsMinZ();
            
            if (side == EnumFacing.DOWN)
                yOffset = (double) y + block.getBlockBoundsMinY() - (double) offset;
                
            else if (side == EnumFacing.UP)
                yOffset = (double) y + block.getBlockBoundsMaxY() + (double) offset;
                
            else if (side == EnumFacing.NORTH)
                zOffset = (double) z + block.getBlockBoundsMinZ() - (double) offset;
                
            else if (side == EnumFacing.SOUTH)
                zOffset = (double) z + block.getBlockBoundsMaxZ() + (double) offset;
                
            else if (side == EnumFacing.WEST)
                xOffset = (double) x + block.getBlockBoundsMinX() - (double) offset;
                
            else if (side == EnumFacing.EAST)
                xOffset = (double) x + block.getBlockBoundsMaxX() + (double) offset;
                
            renderer.addEffect((new OpenEntityDiggingFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, state)).func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
            return true;
        }
        
        return false;
    }
    
    /**
     * Spawns the break particles for a block, similarly to the normal block break effect
     * particle. The intended use of this method is to override the block break effects.
     * 
     * @param renderer The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effect in.
     * @param pos The position to spawn the particles at.
     * @return boolean Whether or not the effect actually spawned.
     */
    public static boolean spawnBreakParticles (EffectRenderer renderer, IBlockState state, World world, BlockPos pos) {
        
        if (state.getBlock() != null) {
            
            int multiplier = 4;
            
            for (int xOffset = 0; xOffset < multiplier; xOffset++) {
                
                for (int yOffset = 0; yOffset < multiplier; yOffset++) {
                    
                    for (int zOffset = 0; zOffset < multiplier; zOffset++) {
                        
                        double xPos = (double) pos.getX() + ((double) xOffset + 0.5D) / (double) multiplier;
                        double yPos = (double) pos.getY() + ((double) yOffset + 0.5D) / (double) multiplier;
                        double zPos = (double) pos.getZ() + ((double) zOffset + 0.5D) / (double) multiplier;
                        renderer.addEffect((new OpenEntityDiggingFX(world, xPos, yPos, zPos, xPos - (double) pos.getX() - 0.5D, yPos - (double) pos.getY() - 0.5D, zPos - (double) pos.getZ() - 0.5D, state)).func_174846_a(pos));
                    }
                }
            }
            
            return true;
        }
        
        return false;
    }
}