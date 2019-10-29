package net.darkhax.bookshelf.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;

/**
 * This model class has been adapted from Refined Storage's FullbrightBakedModel class. The
 * original class is licensed under the MIT license and can be found here:
 * https://raw.githubusercontent.com/raoulvdberge/refinedstorage/mc1.14/src/main/java/com/raoulvdberge/refinedstorage/ClientSetup.java
 */
public class FullbrightBakedModel extends DelegateBakedModel {
    
    private static final LoadingCache<CacheKey, List<BakedQuad>> CACHE = CacheBuilder.newBuilder().build(new CacheLoader<CacheKey, List<BakedQuad>>() {
        @Override
        public List<BakedQuad> load (CacheKey key) {
            
            return transformQuads(key.base.getQuads(key.state, key.side, key.random, EmptyModelData.INSTANCE), key.textures);
        }
    });
    
    private final Map<ResourceLocation, Float> textureLightValues;
    private boolean cacheDisabled = false;
    
    public FullbrightBakedModel(IBakedModel base) {
        
        super(base);
        this.textureLightValues = new HashMap<>();
    }
    
    public FullbrightBakedModel(IBakedModel base, Float light, ResourceLocation... textures) {
        
        this(base);
        
        for (final ResourceLocation texture : textures) {
            
            this.setLightLevel(texture, light);
        }
    }
    
    public void setLightLevel (ResourceLocation texture, float light) {
        
        this.textureLightValues.put(texture, light);
    }
    
    public FullbrightBakedModel disableCache () {
        
        this.cacheDisabled = true;
        
        return this;
    }
    
    @Override
    public List<BakedQuad> getQuads (@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData data) {
        
        if (state == null) {
            return this.delegateModel.getQuads(state, side, rand, data);
        }
        
        if (this.cacheDisabled) {
            return transformQuads(this.delegateModel.getQuads(state, side, rand, data), this.textureLightValues);
        }
        
        return CACHE.getUnchecked(new CacheKey(this.delegateModel, this.textureLightValues, rand, state, side));
    }
    
    private static List<BakedQuad> transformQuads (List<BakedQuad> oldQuads, Map<ResourceLocation, Float> textures) {
        
        final List<BakedQuad> quads = new ArrayList<>(oldQuads);
        
        for (int i = 0; i < quads.size(); ++i) {
            
            final BakedQuad quad = quads.get(i);
            
            final ResourceLocation texture = quad.getSprite().getName();
            if (textures.containsKey(texture)) {
                quads.set(i, transformQuad(quad, textures.get(texture)));
            }
        }
        
        return quads;
    }
    
    private static BakedQuad transformQuad (BakedQuad quad, float light) {
        
        if (RenderUtils.isForgeLightingPipeline()) {
            
            final VertexFormat newFormat = RenderUtils.getFormatWithLightMap(quad.getFormat());
            
            final UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(newFormat);
            
            final VertexLighterFlat trans = new VertexLighterFlat(Minecraft.getInstance().getBlockColors()) {
                @Override
                protected void updateLightmap (float[] normal, float[] lightmap, float x, float y, float z) {
                    
                    lightmap[0] = light;
                    lightmap[1] = light;
                }
                
                @Override
                public void setQuadTint (int tint) {
                    
                    // NO OP
                }
            };
            
            trans.setParent(builder);
            
            quad.pipe(trans);
            
            builder.setQuadTint(quad.getTintIndex());
            builder.setQuadOrientation(quad.getFace());
            builder.setTexture(quad.getSprite());
            builder.setApplyDiffuseLighting(false);
            
            return builder.build();
        }
        
        return quad;
    }
    
    private class CacheKey {
        private final IBakedModel base;
        private final Map<ResourceLocation, Float> textures;
        private final Random random;
        private final BlockState state;
        private final Direction side;
        
        public CacheKey(IBakedModel base, Map<ResourceLocation, Float> textures, Random random, BlockState state, Direction side) {
            
            this.base = base;
            this.textures = textures;
            this.random = random;
            this.state = state;
            this.side = side;
        }
        
        @Override
        public boolean equals (Object o) {
            
            if (this == o) {
                return true;
            }
            
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            
            final CacheKey cacheKey = (CacheKey) o;
            
            if (cacheKey.side != this.side) {
                return false;
            }
            
            if (!this.state.equals(cacheKey.state)) {
                return false;
            }
            
            return true;
        }
        
        @Override
        public int hashCode () {
            
            return this.state.hashCode() + 31 * (this.side != null ? this.side.hashCode() : 0);
        }
    }
}
