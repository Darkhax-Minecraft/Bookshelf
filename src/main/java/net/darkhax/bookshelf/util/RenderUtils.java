/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
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
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderUtils {

    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private RenderUtils () {

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

        if (offset) {
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        }
        else {
            GlStateManager.translate(x, y, z);
        }
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
        final Block block = StackUtils.getBlockFromStack(stack);

        if (block == null) {

            final ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
            return !stack.isEmpty() ? mesher.getParticleIcon(stack.getItem(), stack.getItemDamage()) : mesher.getItemModel(null).getParticleTexture();
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
        final int brightness = mc.world.getCombinedLight(pos, fluid.getFluid().getLuminosity());

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setupRenderState(x, y, z);
        GlStateManager.translate(x, y, z);

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

        while (u1 > 1f) {
            u1 -= 1f;
        }

        final double vy = y % 1d;
        double vy1 = vy + height;

        while (vy1 > 1f) {
            vy1 -= 1f;
        }

        final double vz = z % 1d;
        double vz1 = vz + length;

        while (vz1 > 1f) {
            vz1 -= 1f;
        }

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
    @Deprecated
    public static void setupRenderState (double x, double y, double z) {

        setupRenderState();
    }

    /**
     * Handles all of the basic startup to minimize render conflicts with existing rendering.
     * Make sure to
     */
    public static void setupRenderState () {

        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        }
        else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }
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

    /**
     * A modified version of the vanilla glint effect. This modified version accepts a color
     * and texture.
     *
     * @param renderer Instance of RenderItem to use when rendering the effect.
     * @param stack The ItemStack to render the effect to.
     * @param model The model to render the effect around.
     * @param texture The texture for the glint effect.
     * @param color The color for the glint effect.
     */
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

    /**
     * Gets the missing quads for a given state.
     *
     * @param state The state to use.
     * @param side The side to get quads for.
     * @param rand A random long seed.
     * @return The missing quads for the missing model.
     */
    public static List<BakedQuad> getMissingquads (IBlockState state, EnumFacing side, long rand) {

        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);
    }

    /**
     * Attempts to get a block sprite from a block state.
     *
     * @param state The block state to get the sprite for.
     * @return The block sprite.
     */
    public static TextureAtlasSprite getSprite (IBlockState state) {

        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }

    /**
     * Creates the basic TRSRTransformations for a perspective aware model.
     *
     * @param model The model to get the transforms for.
     * @return An immutable map of all the transforms.
     */
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getBasicTransforms (IPerspectiveAwareModel model) {

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
     * Applies a color to GL state based on the current tick. This is the same code used by
     * sheeps for their jeb color effect.
     *
     * @param entity The entity to render the effect on.
     * @param partialTicks The partial ticks.
     */
    public static void colorRainbow (EntityLivingBase entity, float partialTicks) {

        rainbowColor(entity.ticksExisted, entity.getEntityId(), partialTicks);
    }

    /**
     * Applies a color to GL state based on the current tick. This is similar to the code used
     * by jeb sheeps.
     *
     * @param previousTicks The previous tick value.
     * @param offset An offset value.
     * @param partialTicks The partial ticks.
     */
    public static void rainbowColor (int previousTicks, int offset, float partialTicks) {

        final int ticks = previousTicks / 25 + offset;
        final int colorCount = EnumDyeColor.values().length;
        final int colorMeta1 = ticks % colorCount;
        final int colorMeta2 = (ticks + 1) % colorCount;
        final float f = (previousTicks % 25 + partialTicks) / 25.0F;
        final float[] color1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(colorMeta1));
        final float[] color2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(colorMeta2));
        GlStateManager.color(color1[0] * (1.0F - f) + color2[0] * f, color1[1] * (1.0F - f) + color2[1] * f, color1[2] * (1.0F - f) + color2[2] * f);
    }
}