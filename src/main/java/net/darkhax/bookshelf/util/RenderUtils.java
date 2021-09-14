/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
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
        
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack).getParticleIcon();
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
        
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(state, world, pos);
    }
    
    /**
     * Gets the particle texture for a block state.
     *
     * @param state The state to get the particle texture of.
     * @return The particle texture for the block state.
     */
    public static TextureAtlasSprite getParticleSprite (BlockState state) {
        
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state);
    }
    
    /**
     * Gets a model by it's location name.
     *
     * @param name The model location name.
     * @return The associated model.
     */
    public static IBakedModel getModel (ModelResourceLocation name) {
        
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager().getModel(name);
    }
    
    /**
     * Gets a model for an item stack.
     *
     * @param stack The stack to get the model for.
     * @return The model for the stack.
     */
    public static IBakedModel getModel (ItemStack stack) {
        
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
    }
    
    /**
     * Gets a model for a block state.
     *
     * @param state The state to get the model for.
     * @return The model for the state.
     */
    public static IBakedModel getModel (BlockState state) {
        
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
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
                
                final int lightForSide = WorldRenderer.getLightColor(world, state, pos.relative(side));
                renderer.renderModelFaceFlat(world, state, pos, lightForSide, OverlayTexture.NO_OVERLAY, false, matrix, buffer, sidedQuads, BITS);
            }
        }
        
        // Renders the non-sided model quads.
        RANDOM.setSeed(0L);
        final List<BakedQuad> unsidedQuads = model.getQuads(state, null, RANDOM, modelData);
        
        if (!unsidedQuads.isEmpty()) {
            
            renderer.renderModelFaceFlat(world, state, pos, -1, OverlayTexture.NO_OVERLAY, true, matrix, buffer, unsidedQuads, BITS);
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
    
    /**
     * A cache of render types lazily generated by queries to
     * {@link #getRenderType(BlockState)}. The vanilla maps don't support modded blocks and are
     * deprecated.
     */
    private static final Map<Block, RenderType> RENDER_TYPES = new HashMap<>();
    
    /**
     * Gets a RenderType for a given block.
     *
     * @param state The block to get the type of.
     * @return The RenderType for the block.
     */
    public static RenderType getRenderType (BlockState state) {
        
        return RENDER_TYPES.computeIfAbsent(state.getBlock(), k -> findRenderType(state));
    }
    
    /**
     * Finds a valid RenderType for a block. This is done by testing all the block types and
     * returning the first valid match.
     *
     * @param state The block to get the type of.
     * @return The RenderType for the block.
     */
    public static RenderType findRenderType (BlockState state) {
        
        for (final RenderType blockType : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(state, blockType)) {
                return blockType;
            }
        }
        
        return RenderTypeLookup.getMovingBlockRenderType(state);
    }
    
    /**
     * Renders a block state onto the screen as if it were in the world.
     *
     * @param state The block state to render.
     * @param world The world context for rendering the state.
     * @param pos The position to render the block state at.
     * @param matrix The rendering matrix stack.
     * @param buffer The rendering buffer.
     * @param light Packed lighting data.
     * @param overlay Packed overlay data.
     * @param withFluid Should fluid states be rendered?
     * @param preferredSides The sides of the block that should rendered. This is used to cull
     *        the sides that you don't want. Due to invasive changes made by Optifine this will
     *        be ignored when Optifine is installed.
     */
    public static void renderState (BlockState state, World world, BlockPos pos, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay, boolean withFluid, Direction... preferredSides) {
        
        if (!ModUtils.isOptifineLoaded()) {
            
            renderBlock(state, world, pos, matrix, buffer, preferredSides);
        }
        
        else {
            
            renderBlock(state, world, pos, matrix, buffer);
        }
        
        if (withFluid) {
            
            // Handle fluids and waterlogging.
            final FluidState fluidState = state.getFluidState();
            
            if (fluidState != null && !fluidState.isEmpty()) {
                
                final Fluid fluid = fluidState.getType();
                final ResourceLocation texture = fluid.getAttributes().getStillTexture();
                final int[] color = unpackColor(fluid.getAttributes().getColor(world, pos));
                final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(texture);
                renderBlockSprite(buffer.getBuffer(RenderType.translucent()), matrix, sprite, light, overlay, color);
            }
        }
    }
    
    /**
     * Renders a block state into the world.
     *
     * @param state The state to render.
     * @param world The world context to render into.
     * @param pos The position of the block.
     * @param matrix The render matrix.
     * @param buffer The render buffer.
     * @param preferredSides The sides to render, allows faces to be culled.
     */
    private static void renderBlock (BlockState state, World world, BlockPos pos, MatrixStack matrix, IRenderTypeBuffer buffer, Direction[] renderSides) {
        
        final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        final IBakedModel model = dispatcher.getBlockModel(state);
        
        final RenderType type = RenderUtils.findRenderType(state);
        
        if (type != null) {
            
            ForgeHooksClient.setRenderLayer(type);
            
            final IVertexBuilder builder = buffer.getBuffer(type);
            RenderUtils.renderModel(dispatcher.getModelRenderer(), world, model, state, pos, matrix, builder, renderSides);
            
            ForgeHooksClient.setRenderLayer(null);
        }
    }
    
    /**
     * Renders a block state into the world. This only exists for optifine compatibility mode.
     *
     * @param state The state to render.
     * @param world The world context to render into.
     * @param pos The position of the block.
     * @param matrix The render matrix.
     * @param buffer The render buffer.
     */
    private static void renderBlock (BlockState state, World world, BlockPos pos, MatrixStack matrix, IRenderTypeBuffer buffer) {
        
        final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        final IBakedModel model = dispatcher.getBlockModel(state);
        final boolean useAO = Minecraft.useAmbientOcclusion() && state.getLightValue(world, pos) == 0 && model.useAmbientOcclusion();
        
        final RenderType type = RenderUtils.findRenderType(state);
        
        if (type != null) {
            
            ForgeHooksClient.setRenderLayer(type);
            
            final IVertexBuilder builder = buffer.getBuffer(type);
            renderModel(dispatcher.getModelRenderer(), useAO, world, model, state, pos, matrix, builder, false, OverlayTexture.NO_OVERLAY);
            
            ForgeHooksClient.setRenderLayer(null);
        }
    }
    
    /**
     * This only exists for optifine compatibility mode.
     */
    private static boolean renderModel (BlockModelRenderer renderer, boolean useAO, IBlockDisplayReader world, IBakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, IVertexBuilder buffer, boolean checkSides, int overlay) {
        
        try {
            
            final IModelData modelData = model.getModelData(world, pos, state, EmptyModelData.INSTANCE);
            return useAO ? renderer.renderModelSmooth(world, model, state, pos, matrix, buffer, checkSides, RANDOM, 0L, overlay, modelData) : renderer.renderModelFlat(world, model, state, pos, matrix, buffer, checkSides, RANDOM, 0L, overlay, modelData);
        }
        
        catch (final Throwable throwable) {
            
            final CrashReport crashreport = CrashReport.forThrowable(throwable, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(crashreportcategory, pos, state);
            crashreportcategory.setDetail("Using AO", useAO);
            throw new ReportedException(crashreport);
        }
    }
    
    public static void renderLinesWrapped (MatrixStack matrix, int x, int y, ITextComponent text, int textWidth) {
        
        final FontRenderer font = Minecraft.getInstance().font;
        renderLinesWrapped(matrix, font, x, y, font.lineHeight, 0, text, textWidth);
    }
    
    public static void renderLinesWrapped (MatrixStack matrix, FontRenderer fontRenderer, int x, int y, int spacing, int defaultColor, ITextComponent text, int textWidth) {
        
        // trimStringToWidth is actually wrapToWidth
        renderLinesWrapped(matrix, fontRenderer, x, y, spacing, defaultColor, fontRenderer.split(text, textWidth));
    }
    
    public static void renderLinesWrapped (MatrixStack matrix, FontRenderer fontRenderer, int x, int y, int spacing, int defaultColor, List<IReorderingProcessor> lines) {
        
        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
            
            final IReorderingProcessor lineFragment = lines.get(lineNum);
            fontRenderer.draw(matrix, lineFragment, x, y + lineNum * spacing, defaultColor);
        }
    }
    
    public static int renderLinesReversed (MatrixStack matrix, int x, int y, ITextComponent text, int textWidth) {
        
        final FontRenderer font = Minecraft.getInstance().font;
        return renderLinesReversed(matrix, font, x, y, font.lineHeight, 0xffffff, text, textWidth);
    }
    
    public static int renderLinesReversed (MatrixStack matrix, FontRenderer fontRenderer, int x, int y, int spacing, int defaultColor, ITextComponent text, int textWidth) {
        
        // trimStringToWidth is actually wrapToWidth
        return renderLinesReversed(matrix, fontRenderer, x, y, spacing, defaultColor, fontRenderer.split(text, textWidth));
    }
    
    public static int renderLinesReversed (MatrixStack matrix, FontRenderer fontRenderer, int x, int y, int spacing, int defaultColor, List<IReorderingProcessor> lines) {
        
        final int lineCount = lines.size();
        for (int lineNum = lineCount - 1; lineNum >= 0; lineNum--) {
            
            final IReorderingProcessor lineFragment = lines.get(lineCount - 1 - lineNum);
            fontRenderer.draw(matrix, lineFragment, x, y - (lineNum + 1) * (spacing + 1), defaultColor);
        }
        
        return lineCount * (spacing + 1);
    }
    
    /**
     * Unpacks a color into ARGB byte channels.
     *
     * @param color The color to unpack.
     * @return An array containing the ARGB color channels.
     */
    public static int[] unpackColor (int color) {
        
        final int[] colors = new int[4];
        colors[0] = color >> 24 & 0xff; // alpha
        colors[1] = color >> 16 & 0xff; // red
        colors[2] = color >> 8 & 0xff; // green
        colors[3] = color & 0xff; // blue
        return colors;
    }
    
    /**
     * Renders a block sprite as a cube.
     *
     * @param builder The vertex builder.
     * @param stack The render stack.
     * @param sprite The sprite to render.
     * @param light The packed lighting data.
     * @param overlay The packed overlay data.
     * @param color RGBA color to render.
     */
    public static void renderBlockSprite (IVertexBuilder builder, MatrixStack stack, TextureAtlasSprite sprite, int light, int overlay, int[] color) {
        
        renderBlockSprite(builder, stack.last().pose(), sprite, light, overlay, 0f, 1f, 0f, 1f, 0f, 1f, color);
    }
    
    /**
     * Renders a block sprite as a cube.
     *
     * @param builder The vertex builder.
     * @param stack The render stack.
     * @param sprite The sprite to render.
     * @param light The packed lighting data.
     * @param overlay The packed overlay data.
     * @param x1 The min x width of the cube to render. Between 0 and 1.
     * @param x2 The max x width of the cube to render. Between 0 and 1.
     * @param y1 The min y width of the cube to render. Between 0 and 1.
     * @param y2 The max y width of the cube to render. Between 0 and 1.
     * @param z1 The min z width of the cube to render. Between 0 and 1.
     * @param z2 The max z width of the cube to render. Between 0 and 1.
     * @param color RGBA color to render.
     */
    public static void renderBlockSprite (IVertexBuilder builder, MatrixStack stack, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        
        renderBlockSprite(builder, stack.last().pose(), sprite, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }
    
    /**
     * Renders a block sprite as a cube.
     *
     * @param builder The vertex builder.
     * @param pos The render position.
     * @param sprite The sprite to render.
     * @param light The packed lighting data.
     * @param overlay The packed overlay data.
     * @param x1 The min x width of the cube to render. Between 0 and 1.
     * @param x2 The max x width of the cube to render. Between 0 and 1.
     * @param y1 The min y width of the cube to render. Between 0 and 1.
     * @param y2 The max y width of the cube to render. Between 0 and 1.
     * @param z1 The min z width of the cube to render. Between 0 and 1.
     * @param z2 The max z width of the cube to render. Between 0 and 1.
     * @param color RGBA color to render.
     */
    public static void renderBlockSprite (IVertexBuilder builder, Matrix4f pos, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        
        renderSpriteSide(builder, pos, sprite, Direction.DOWN, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderSpriteSide(builder, pos, sprite, Direction.UP, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderSpriteSide(builder, pos, sprite, Direction.NORTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderSpriteSide(builder, pos, sprite, Direction.SOUTH, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderSpriteSide(builder, pos, sprite, Direction.WEST, light, overlay, x1, x2, y1, y2, z1, z2, color);
        renderSpriteSide(builder, pos, sprite, Direction.EAST, light, overlay, x1, x2, y1, y2, z1, z2, color);
    }
    
    /**
     * Renders a block sprite as a side of a cube.
     *
     * @param builder The vertex builder.
     * @param pos The render position.
     * @param sprite The sprite to render.
     * @param side The side of the cube to render.
     * @param light The packed lighting data.
     * @param overlay The packed overlay data.
     * @param x1 The min x width of the cube to render. Between 0 and 1.
     * @param x2 The max x width of the cube to render. Between 0 and 1.
     * @param y1 The min y width of the cube to render. Between 0 and 1.
     * @param y2 The max y width of the cube to render. Between 0 and 1.
     * @param z1 The min z width of the cube to render. Between 0 and 1.
     * @param z2 The max z width of the cube to render. Between 0 and 1.
     * @param color RGBA color to render.
     */
    public static void renderSpriteSide (IVertexBuilder builder, Matrix4f pos, TextureAtlasSprite sprite, Direction side, int light, int overlay, float x1, float x2, float y1, float y2, float z1, float z2, int[] color) {
        
        // Convert block size to pixel size
        final double px1 = x1 * 16;
        final double px2 = x2 * 16;
        final double py1 = y1 * 16;
        final double py2 = y2 * 16;
        final double pz1 = z1 * 16;
        final double pz2 = z2 * 16;
        
        if (side == Direction.DOWN) {
            final float u1 = sprite.getU(px1);
            final float u2 = sprite.getU(px2);
            final float v1 = sprite.getV(pz1);
            final float v2 = sprite.getV(pz2);
            builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
            builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
            builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
            builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, -1f, 0f).endVertex();
        }
        
        if (side == Direction.UP) {
            final float u1 = sprite.getU(px1);
            final float u2 = sprite.getU(px2);
            final float v1 = sprite.getV(pz1);
            final float v2 = sprite.getV(pz2);
            builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
            builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
            builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
            builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 1f, 0f).endVertex();
        }
        
        if (side == Direction.NORTH) {
            final float u1 = sprite.getU(px1);
            final float u2 = sprite.getU(px2);
            final float v1 = sprite.getV(py1);
            final float v2 = sprite.getV(py2);
            builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
            builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
            builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
            builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, -1f).endVertex();
        }
        
        if (side == Direction.SOUTH) {
            final float u1 = sprite.getU(px1);
            final float u2 = sprite.getU(px2);
            final float v1 = sprite.getV(py1);
            final float v2 = sprite.getV(py2);
            builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
            builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
            builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
            builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(0f, 0f, 1f).endVertex();
        }
        
        if (side == Direction.WEST) {
            final float u1 = sprite.getU(py1);
            final float u2 = sprite.getU(py2);
            final float v1 = sprite.getV(pz1);
            final float v2 = sprite.getV(pz2);
            builder.vertex(pos, x1, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
            builder.vertex(pos, x1, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
            builder.vertex(pos, x1, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
            builder.vertex(pos, x1, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(-1f, 0f, 0f).endVertex();
        }
        
        if (side == Direction.EAST) {
            final float u1 = sprite.getU(py1);
            final float u2 = sprite.getU(py2);
            final float v1 = sprite.getV(pz1);
            final float v2 = sprite.getV(pz2);
            builder.vertex(pos, x2, y1, z1).color(color[1], color[2], color[3], color[0]).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
            builder.vertex(pos, x2, y2, z1).color(color[1], color[2], color[3], color[0]).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
            builder.vertex(pos, x2, y2, z2).color(color[1], color[2], color[3], color[0]).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
            builder.vertex(pos, x2, y1, z2).color(color[1], color[2], color[3], color[0]).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(1f, 0f, 0f).endVertex();
        }
    }
}