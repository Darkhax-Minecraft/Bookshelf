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
            
            final int x = pos.getX();
            final int y = pos.getY();
            final int z = pos.getZ();
            final float offset = 0.1F;
            final AxisAlignedBB bounds = state.getBoundingBox(world, pos);
            double xOffset = x + Constants.RANDOM.nextDouble() * (bounds.maxX - bounds.minX - offset * 2.0F) + offset + bounds.minX;
            double yOffset = y + Constants.RANDOM.nextDouble() * (bounds.maxY - bounds.minY - offset * 2.0F) + offset + bounds.minY;
            double zOffset = z + Constants.RANDOM.nextDouble() * (bounds.maxZ - bounds.minZ - offset * 2.0F) + offset + bounds.minZ;
            
            if (side == EnumFacing.DOWN)
                yOffset = y + bounds.minY - offset;
                
            else if (side == EnumFacing.UP)
                yOffset = y + bounds.maxY + offset;
                
            else if (side == EnumFacing.NORTH)
                zOffset = z + bounds.minZ - offset;
                
            else if (side == EnumFacing.SOUTH)
                zOffset = z + bounds.maxZ + offset;
                
            else if (side == EnumFacing.WEST)
                xOffset = x + bounds.minX - offset;
                
            else if (side == EnumFacing.EAST)
                xOffset = x + bounds.maxX + offset;
                
            renderer.addEffect(new OpenEntityDiggingFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, state).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
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
            
            final int multiplier = 4;
            
            for (int xOffset = 0; xOffset < multiplier; xOffset++)
                for (int yOffset = 0; yOffset < multiplier; yOffset++)
                    for (int zOffset = 0; zOffset < multiplier; zOffset++) {
                        
                        final double xPos = pos.getX() + (xOffset + 0.5D) / multiplier;
                        final double yPos = pos.getY() + (yOffset + 0.5D) / multiplier;
                        final double zPos = pos.getZ() + (zOffset + 0.5D) / multiplier;
                        renderer.addEffect(new OpenEntityDiggingFX(world, xPos, yPos, zPos, xPos - pos.getX() - 0.5D, yPos - pos.getY() - 0.5D, zPos - pos.getZ() - 0.5D, state).setBlockPos(pos));
                    }
                    
            return true;
        }
        
        return false;
    }
}