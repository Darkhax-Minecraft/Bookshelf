package net.darkhax.bookshelf.client.model;

import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.darkhax.bookshelf.lib.BlockStates;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * This class defines a model which allows for certain parts of it to be retextured to other
 * blocks. Specifically parts which use {@link #textureVariable} for the texture. This has a
 * near endless amount of possibilities when it comes to decoration, customization and upgrads.
 * To use this model effectively, there are several different things you need to do. Luckily
 * there are examples available!
 * 
 * Bake/register your instance of ModelRetexturable so it can be seen by MC's renderer.
 * https://goo.gl/I6Roou
 * 
 * Set the Item model to point to the model, like you would any item.
 * https://goo.gl/Cd3PlT
 * 
 * Create an ItemOverrideList to remap the ItemStack to the correct variable. 
 * https://goo.gl/Aup8MK
 */
public class ModelRetexturable implements IPerspectiveAwareModel {
    
    /**
     * The base model to retexture. This is your model!
     */
    private final IRetexturableModel baseModel;
    
    /**
     * The name of the texture variable to retexture. Only support for one right now.
     */
    private final String textureVariable;
    
    /**
     * Function used to get a texture sprite. The following is one of the best defaults for
     * this. location ->
     * Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString())
     */
    private final Function<ResourceLocation, TextureAtlasSprite> spriteGetter;
    
    /**
     * Map of all the TRSRTransformations. See
     * {@link RenderUtils#getBasicTransforms(IPerspectiveAwareModel)} for a default option for
     * this.
     */
    private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
    
    /**
     * A cache which links texture names to their retextured model counter part.
     */
    private final Map<String, IBakedModel> cache = Maps.newHashMap();
    
    /**
     * Whether or not the model uses ambientOcclusion.
     * https://en.wikipedia.org/wiki/Ambient_occlusion
     */
    private final boolean ambientOcclusion;
    
    /**
     * Whether or not the model is 3d in a gui.
     */
    private final boolean gui3D;
    
    /**
     * Whether or not the model is considered to be built in. This is almost always false.
     */
    private final boolean builtin;
    
    /**
     * The blockstate to use for the default particle texture.
     */
    private final IBlockState particle;
    
    /**
     * The camera transforms for this model. A good default is
     * {@link ItemCameraTransforms#DEFAULT}
     */
    private final ItemCameraTransforms cameraTransforms;
    
    /**
     * An override for the item version of the model. Allows you to map an ItemStack to the
     * correct model.
     */
    private final ItemOverrideList itemOverride;
    
    /**
     * Creates a new model which represents a retexturable version of a json model. One of the
     * texture variables can be remapped to any other block texture.
     * 
     * @param baseModel The base model to use for this model.
     * @param textureVariable The specific texture variable to retexture.
     * @param particle The Blockstate of the particle to use for this model.
     * @param transforms Map of TRSRTransformations to use for the model.
     * @param itemOverride An override for the item version of the model. Allows you to map an
     *        ItemStack to the correct model.
     */
    public ModelRetexturable(IRetexturableModel baseModel, String textureVariable, IBlockState particle, ImmutableMap<TransformType, TRSRTransformation> transforms, ItemOverrideList itemOverride) {
        
        this(baseModel, textureVariable, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()), transforms, true, true, false, particle, ItemCameraTransforms.DEFAULT, itemOverride);
    }
    
    /**
     * Creates a new model which can have aspects of it retextured.
     * 
     * @param baseModel The base model to use for this model.
     * @param textureVariable The specific texture variable to retexture. Only supports one
     *        right now.
     * @param spriteGetter Function to handle item sprite lookup.
     * @param transforms Map of TRSRTransformations to use for the model.
     * @param ambientOcclusion Whether or not the model should use ambient occlusion.
     * @param gui3d Whether or not the model is 3d in a gui.
     * @param builtin Whether or not the model is built in.
     * @param particle Blockstate of the particle to use for this model.
     * @param cameraTransforms The camera transforms for the model.
     * @param itemOverride An override for the item version of the model. Allows you to map an
     *        ItemStack to the correct model.
     */
    public ModelRetexturable(IRetexturableModel baseModel, String textureVariable, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ImmutableMap<TransformType, TRSRTransformation> transforms, boolean ambientOcclusion, boolean gui3d, boolean builtin, IBlockState particle, ItemCameraTransforms cameraTransforms, ItemOverrideList itemOverride) {
        
        this.baseModel = baseModel;
        this.textureVariable = textureVariable;
        this.spriteGetter = spriteGetter;
        this.transforms = transforms;
        this.ambientOcclusion = ambientOcclusion;
        this.gui3D = gui3d;
        this.builtin = builtin;
        this.particle = particle;
        this.cameraTransforms = cameraTransforms;
        this.itemOverride = itemOverride;
    }
    
    /**
     * Gets the retextured representation for a given texture name. If one does not exist it
     * will be made and added to the cache.
     * 
     * @param textureName The texture name to look for.
     * @return The retextured representation of the base model.
     */
    public IBakedModel getRetexturedModel (String textureName) {
        
        IBakedModel model = this;
        
        if (this.cache.containsKey(textureName))
            model = this.cache.get(textureName);
            
        else if (this.baseModel != null) {
            
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put(this.textureVariable, textureName);
            final IModel retexturedModel = this.baseModel.retexture(builder.build());
            final IModelState modelState = new SimpleModelState(this.transforms);
            model = retexturedModel.bake(modelState, DefaultVertexFormats.BLOCK, this.spriteGetter);
            this.cache.put(textureName, model);
        }
        
        return model;
    }
    
    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing side, long rand) {
        
        final IBlockState heldState = ((IExtendedBlockState) state).getValue(BlockStates.HELD_STATE);
        
        if (heldState == null)
            RenderUtils.getMissingquads(heldState, side, rand);
            
        return this.getRetexturedModel(RenderUtils.getSprite(heldState).getIconName()).getQuads(state, side, rand);
    }
    
    @Override
    public boolean isAmbientOcclusion () {
        
        return this.ambientOcclusion;
    }
    
    @Override
    public boolean isGui3d () {
        
        return this.gui3D;
    }
    
    @Override
    public boolean isBuiltInRenderer () {
        
        return this.builtin;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture () {
        
        return RenderUtils.getSprite(this.particle);
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        
        return this.cameraTransforms;
    }
    
    @Override
    public ItemOverrideList getOverrides () {
        
        return this.itemOverride;
    }
    
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective (TransformType cameraTransformType) {
        
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
    }
}