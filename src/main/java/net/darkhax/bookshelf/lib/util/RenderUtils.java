package net.darkhax.bookshelf.lib.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.client.particle.OpenParticleDigging;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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
     * @param manager The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effects in.
     * @param pos The position to spawn the particles at.
     * @param side The side offset for the effect.
     * @return boolean Whether or not the effect actually spawned.
     */
    public static boolean spawnDigParticles (ParticleManager manager, IBlockState state, World world, BlockPos pos, EnumFacing side) {
        
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
                
            manager.addEffect(new OpenParticleDigging(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, state).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
        
        return false;
    }
    
    /**
     * Spawns the break particles for a block, similarly to the normal block break effect
     * particle. The intended use of this method is to override the block break effects.
     * 
     * @param manager The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effect in.
     * @param pos The position to spawn the particles at.
     * @return boolean Whether or not the effect actually spawned.
     */
    public static boolean spawnBreakParticles (ParticleManager manager, IBlockState state, World world, BlockPos pos) {
        
        if (state.getBlock() != null) {
            
            final int multiplier = 4;
            
            for (int xOffset = 0; xOffset < multiplier; xOffset++)
                for (int yOffset = 0; yOffset < multiplier; yOffset++)
                    for (int zOffset = 0; zOffset < multiplier; zOffset++) {
                        
                        final double xPos = pos.getX() + (xOffset + 0.5D) / multiplier;
                        final double yPos = pos.getY() + (yOffset + 0.5D) / multiplier;
                        final double zPos = pos.getZ() + (zOffset + 0.5D) / multiplier;
                        manager.addEffect(new OpenParticleDigging(world, xPos, yPos, zPos, xPos - pos.getX() - 0.5D, yPos - pos.getY() - 0.5D, zPos - pos.getZ() - 0.5D, state).setBlockPos(pos));
                    }
                    
            return true;
        }
        
        return false;
    }
    
    /**
     * Spawns particles in a ring, centered around a certain position.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *            will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnParticleRing (World world, EnumParticleTypes particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < 2 * Math.PI; degree += step)
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
    }
    
    /**
     * Spawns particles in a ring, centered around a certain point. This method takes a percent
     * argument which is used to calculate the amount of the ring to spawn.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param percent The percentage of the ring to render.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *            will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnPercentageParticleRing (World world, EnumParticleTypes particle, float percentage, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < 2 * Math.PI * percentage; degree += step)
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
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
     *            download a texture. This must be a valid texture, or else you will get a
     *            missing texture.
     * @param buffer A special buffer to use when downloading the image. It is okay to pass
     *            null for this if you don't want anything fancy.
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
     *            download a texture. This must be a valid texture, or else you will get a
     *            missing texture.
     * @param buffer A special buffer to use when downloading the image. It is okay to pass
     *            null for this if you don't want anything fancy.
     * @return The downloaded image data.
     */
    public static ThreadDownloadImageData downloadResource (String url, ResourceLocation outputResource, ResourceLocation defaultResource, IImageBuffer buffer) {
        
        final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        
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
}