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
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
        
        for (final RenderType blockType : RenderType.getBlockRenderTypes()) {
            if (RenderTypeLookup.canRenderInLayer(state, blockType)) {
                return blockType;
            }
        }
        
        return RenderTypeLookup.func_239221_b_(state);
    }
    
    /**
     * Renders a block state into the world.
     * 
     * @param state The state to render.
     * @param world The world context to render into.
     * @param pos The position of the block.
     * @param matrix The render matrix.
     * @param buffer The render buffer.
     * @param preferredSides The sides to render, allows faces to be culled. Will be ignored if
     *        Optifine is installed.
     */
    public static void renderState (BlockState state, World world, BlockPos pos, MatrixStack matrix, IRenderTypeBuffer buffer, Direction[] preferredSides) {
        
        if (!ModUtils.isOptifineLoaded()) {
            
            renderBlock(state, world, pos, matrix, buffer, preferredSides);
        }
        
        else {
            
            renderBlock(state, world, pos, matrix, buffer);
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
        
        final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        final IBakedModel model = dispatcher.getModelForState(state);
        
        final RenderType type = RenderUtils.findRenderType(state);
        
        if (type != null) {
            
            ForgeHooksClient.setRenderLayer(type);
            
            final IVertexBuilder builder = buffer.getBuffer(type);
            RenderUtils.renderModel(dispatcher.getBlockModelRenderer(), world, model, state, pos, matrix, builder, renderSides);
            
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
        
        final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        final IBakedModel model = dispatcher.getModelForState(state);
        final boolean useAO = Minecraft.isAmbientOcclusionEnabled() && state.getLightValue(world, pos) == 0 && model.isAmbientOcclusion();
        
        final RenderType type = RenderUtils.findRenderType(state);
        
        if (type != null) {
            
            ForgeHooksClient.setRenderLayer(type);
            
            final IVertexBuilder builder = buffer.getBuffer(type);
            renderModel(dispatcher.getBlockModelRenderer(), useAO, world, model, state, pos, matrix, builder, false, OverlayTexture.NO_OVERLAY);
            
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
            
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state);
            crashreportcategory.addDetail("Using AO", useAO);
            throw new ReportedException(crashreport);
        }
    }
}