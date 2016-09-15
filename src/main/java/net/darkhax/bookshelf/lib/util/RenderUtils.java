package net.darkhax.bookshelf.lib.util;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.client.particle.OpenParticleDigging;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    
    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    
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
     *        will create 1 particle per ring. 0.15 is a nice value.
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
     *        will create 1 particle per ring. 0.15 is a nice value.
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
    
    /**
     * Translates the render state to be relative to the player's position. Allows for
     * rendering at a static world position that is not tied to the player's position.
     * 
     * @param pos The BlockPos The position to translate to within the world.
     */
    public static void translateAgainstPlayer (BlockPos pos, boolean offset) {
        
        final float x = (float) (pos.getX() - TileEntityRendererDispatcher.staticPlayerX);
        final float y = (float) (pos.getY() - TileEntityRendererDispatcher.staticPlayerY);
        final float z = (float) (pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
        
        if (offset)
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        else
            GlStateManager.translate(x, y, z);
    }
    
    /**
     * Gets the particle sprite for an ItemStack.
     * 
     * @param stack The ItemStack to get the particle for.
     * @return A TextureAtlasSprite that points to the particle texture for the ItemStack.
     */
    public static TextureAtlasSprite getParticleTexture (ItemStack stack) {
        
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture();
    }
    
    /**
     * Gets the TextureAtlasSprite for the ItemStack. Has support for both Items and Blocks.
     * 
     * @param stack The ItemStack to get the sprite for.
     * @return The sprite for the ItemStack.
     */
    public static TextureAtlasSprite getSprite (ItemStack stack) {
        
        final Minecraft mc = Minecraft.getMinecraft();
        final Block block = ItemStackUtils.getBlockFromStack(stack);
        
        if (block == null) {
            
            final ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
            return ItemStackUtils.isValidStack(stack) ? mesher.getParticleIcon(stack.getItem(), stack.getItemDamage()) : mesher.getItemModel(null).getParticleTexture();
        }
        
        return mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getStateFromMeta(stack.getItemDamage()));
    }
    
    /**
     * Gets the baked model for a ModelResourceLocation.
     * 
     * @param name The location to get the model from.
     * @return The baked model that was found.
     */
    public static IBakedModel getBakedModel (ModelResourceLocation name) {
        
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(name);
    }
    
    /**
     * Gets the baked model for an ItemStack.
     * 
     * @param stack The stack to get the model of.
     * @return The baked model for the ItemStack.
     */
    public static IBakedModel getBakedModel (ItemStack stack) {
        
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
    }
    
    /**
     * Renders a fluid at the given position.
     * 
     * @param fluid The fluid to render.
     * @param pos The position in the world to render the fluid.
     * @param x Precise X position.
     * @param y Precise Y position.
     * @param z Precise Z position.
     * @param width The width of the block. 1 = full block.
     * @param height The height of the block. 1 = full block.
     * @param length The length of the block. 1 = full block.
     */
    public static void renderFluid (FluidStack fluid, BlockPos pos, double x, double y, double z, double width, double height, double length) {
        
        final double x1 = (1d - width) / 2d;
        final double y1 = (1d - height) / 2d;
        final double z1 = (1d - length) / 2d;
        renderFluid(fluid, pos, x, y, z, x1, y1, z1, 1d - x1, 1d - y1, 1d - z1);
    }
    
    /**
     * Renders a fluid at the given position.
     * 
     * @param fluid The fluid to render.
     * @param pos The position in the world to render the fluid.
     * @param x The base X position.
     * @param y The base Y position.
     * @param z The base Z position.
     * @param x1 The middle X position.
     * @param y1 The middle Y position.
     * @param z1 The middle Z position.
     * @param x2 The max X position.
     * @param y2 The max Y position.
     * @param z2 The max Z position.
     */
    public static void renderFluid (FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        
        final int color = fluid.getFluid().getColor(fluid);
        renderFluid(fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }
    
    /**
     * Renders a fluid at the given position.
     * 
     * @param fluid The fluid to render.
     * @param pos The position in the world to render the fluid.
     * @param x The base X position.
     * @param y The base Y position.
     * @param z The base Z position.
     * @param x1 The middle X position.
     * @param y1 The middle Y position.
     * @param z1 The middle Z position.
     * @param x2 The max X position.
     * @param y2 The max Y position.
     * @param z2 The max Z position.
     * @param color The color offset used by the fluid. Default is white.
     */
    public static void renderFluid (FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        
        final Minecraft mc = Minecraft.getMinecraft();
        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer buffer = tessellator.getBuffer();
        final int brightness = mc.theWorld.getCombinedLight(pos, fluid.getFluid().getLuminosity());
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setupRenderState(x, y, z);
        
        final TextureAtlasSprite still = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
        final TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());
        
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness);
        addTexturedQuad(buffer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness);
        tessellator.draw();
        
        cleanupRenderState();
    }
    
    /**
     * Adds a textured quad to a VertexBuffer. This is intended to be used for block rendering.
     * 
     * @param buffer The VertexBuffer to add to.
     * @param sprite The texture to use.
     * @param x The X position.
     * @param y The Y position.
     * @param z The Z position.
     * @param width The width of the quad.
     * @param height The height of the quad.
     * @param length The length of the quad.
     * @param face The face of a cube to render.
     * @param color The color multiplier to apply.
     * @param brightness The brightness of the cube.
     */
    public static void addTexturedQuad (VertexBuffer buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, EnumFacing face, int color, int brightness) {
        
        if (sprite == null) {
            
            Constants.LOG.warn("Attempted to draw a textures quad with no texture! X:%f Y:%f Z:%f");
            return;
        }
        
        final int firstLightValue = brightness >> 0x10 & 0xFFFF;
        final int secondLightValue = brightness & 0xFFFF;
        final int alpha = color >> 24 & 0xFF;
        final int red = color >> 16 & 0xFF;
        final int green = color >> 8 & 0xFF;
        final int blue = color & 0xFF;
        
        addTextureQuad(buffer, sprite, x, y, z, width, height, length, face, red, green, blue, alpha, firstLightValue, secondLightValue);
    }
    
    /**
     * Adds a textured quad to a VertexBuffer. This is intended to be used for block rendering.
     * 
     * @param buffer The VertexBuffer to add to.
     * @param sprite The texture to use.
     * @param x The X position.
     * @param y The Y position.
     * @param z The Z position.
     * @param width The width of the quad.
     * @param height The height of the quad.
     * @param length The length of the quad.
     * @param face The face of a cube to render.
     * @param red The red multiplier.
     * @param green The green multiplier.
     * @param blue The blue multiplier.
     * @param alpha The alpha multiplier.
     * @param light1 The first light map value.
     * @param light2 The second light map value.
     */
    public static void addTextureQuad (VertexBuffer buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, EnumFacing face, int red, int green, int blue, int alpha, int light1, int light2) {
        
        double minU;
        double maxU;
        double minV;
        double maxV;
        
        final double size = 16f;
        
        final double x2 = x + width;
        final double y2 = y + height;
        final double z2 = z + length;
        
        final double u = x % 1d;
        double u1 = u + width;
        
        while (u1 > 1f)
            u1 -= 1f;
            
        final double vy = y % 1d;
        double vy1 = vy + height;
        
        while (vy1 > 1f)
            vy1 -= 1f;
            
        final double vz = z % 1d;
        double vz1 = vz + length;
        
        while (vz1 > 1f)
            vz1 -= 1f;
            
        switch (face) {
            
            case DOWN:
            
            case UP:
                minU = sprite.getInterpolatedU(u * size);
                maxU = sprite.getInterpolatedU(u1 * size);
                minV = sprite.getInterpolatedV(vz * size);
                maxV = sprite.getInterpolatedV(vz1 * size);
                break;
                
            case NORTH:
            
            case SOUTH:
                minU = sprite.getInterpolatedU(u1 * size);
                maxU = sprite.getInterpolatedU(u * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;
                
            case WEST:
            
            case EAST:
                minU = sprite.getInterpolatedU(vz1 * size);
                maxU = sprite.getInterpolatedU(vz * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;
                
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }
        
        switch (face) {
            
            case DOWN:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
                
            case UP:
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
                
            case NORTH:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
                
            case SOUTH:
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
                
            case WEST:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
                
            case EAST:
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }
    
    /**
     * Handles all of the basic startup to minimize render conflicts with existing rendering.
     * Make sure to call {@link #cleanupRenderState()} after the rendering code, to return the
     * state to normal.
     * 
     * @param x The X position to render at.
     * @param y The Y position to render at.
     * @param z The Z position to render at.
     */
    public static void setupRenderState (double x, double y, double z) {
        
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        if (Minecraft.isAmbientOcclusionEnabled())
            GL11.glShadeModel(GL11.GL_SMOOTH);
        else
            GL11.glShadeModel(GL11.GL_FLAT);
            
        GlStateManager.translate(x, y, z);
    }
    
    /**
     * Counteracts the state changes caused by
     * {@link #setupRenderState(double, double, double)}. Should only be called after that.
     */
    public static void cleanupRenderState () {
        
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }
    
    public static void renderGlintEffect (RenderItem renderer, ItemStack stack, IBakedModel model, ResourceLocation texture, int color) {
        
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        renderer.textureManager.bindTexture(texture);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        final float f = Minecraft.getSystemTime() % 3000L / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        renderer.renderModel(model, color);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        final float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderer.renderModel(model, color);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        renderer.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
}