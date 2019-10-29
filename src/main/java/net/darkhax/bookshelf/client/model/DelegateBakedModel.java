package net.darkhax.bookshelf.client.model;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

/**
 * This model is a delegate model, meaning all functions will be redirected to the delegate
 * object passed at construction time. This is useful for mimicing or expanding existing
 * models.
 */
public class DelegateBakedModel implements IBakedModel {
    
    /**
     * The delegate model. All methods will redirect to the delegate
     */
    protected final IBakedModel delegateModel;
    
    public DelegateBakedModel(IBakedModel delegate) {
        
        this.delegateModel = delegate;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads (@Nullable BlockState state, @Nullable Direction side, Random rand) {
        
        return this.delegateModel.getQuads(state, side, rand);
    }
    
    @Override
    public boolean isAmbientOcclusion () {
        
        return this.delegateModel.isAmbientOcclusion();
    }
    
    @Override
    public boolean isGui3d () {
        
        return this.delegateModel.isGui3d();
    }
    
    @Override
    public boolean isBuiltInRenderer () {
        
        return this.delegateModel.isBuiltInRenderer();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite getParticleTexture () {
        
        return this.delegateModel.getParticleTexture();
    }
    
    @Override
    public ItemOverrideList getOverrides () {
        
        return this.delegateModel.getOverrides();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms () {
        
        return this.delegateModel.getItemCameraTransforms();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective (ItemCameraTransforms.TransformType cameraTransformType) {
        
        return this.delegateModel.handlePerspective(cameraTransformType);
    }
}