package net.darkhax.bookshelf.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.client.particle.OpenEntityDiggingFX;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    
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
        
        return Minecraft.getMinecraft().getRenderManager().skinMap.get(type);
    }
    
    /**
     * Sets a player specific texture. This can be used to change the skin, cape or elytra
     * texture.
     * 
     * @param type The type of texture to apply. Vanilla has Skin, Cape and Elytra.
     * @param player The player to set the texture to.
     * @param texture The texture to apply.
     * @return boolean Whether or not the texture was succesfully applied.
     */
    public static boolean setPlayerTexture (Type type, AbstractClientPlayer player, ResourceLocation texture) {
        
        if (player.hasPlayerInfo() && texture != null) {
            
            player.getPlayerInfo().playerTextures.put(type, texture);
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
        
        if (state != null && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float offset = 0.1F;
            AxisAlignedBB bounds = state.getBoundingBox(world, pos);
            double xOffset = (double) x + Constants.RANDOM.nextDouble() * (bounds.maxX - bounds.minX - (double) (offset * 2.0F)) + (double) offset + bounds.minX;
            double yOffset = (double) y + Constants.RANDOM.nextDouble() * (bounds.maxY - bounds.minY - (double) (offset * 2.0F)) + (double) offset + bounds.minY;
            double zOffset = (double) z + Constants.RANDOM.nextDouble() * (bounds.maxZ - bounds.minZ - (double) (offset * 2.0F)) + (double) offset + bounds.minZ;
            
            if (side == EnumFacing.DOWN)
                yOffset = (double) y + bounds.minY - (double) offset;
                
            else if (side == EnumFacing.UP)
                yOffset = (double) y + bounds.maxY + (double) offset;
                
            else if (side == EnumFacing.NORTH)
                zOffset = (double) z + bounds.minZ - (double) offset;
                
            else if (side == EnumFacing.SOUTH)
                zOffset = (double) z + bounds.maxZ + (double) offset;
                
            else if (side == EnumFacing.WEST)
                xOffset = (double) x + bounds.minX - (double) offset;
                
            else if (side == EnumFacing.EAST)
                xOffset = (double) x + bounds.maxX + (double) offset;
                
            renderer.addEffect((new OpenEntityDiggingFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, state)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
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
                        renderer.addEffect((new OpenEntityDiggingFX(world, xPos, yPos, zPos, xPos - (double) pos.getX() - 0.5D, yPos - (double) pos.getY() - 0.5D, zPos - (double) pos.getZ() - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }
            
            return true;
        }
        
        return false;
    }
}