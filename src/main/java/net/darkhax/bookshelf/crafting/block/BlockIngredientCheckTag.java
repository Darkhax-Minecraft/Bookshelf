package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class BlockIngredientCheckTag extends BlockIngredient {
    
    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "check_tag");
    public static final Serializer SERIALIZER = new Serializer();
    
    private final IOptionalNamedTag<Block> tag;
    private List<BlockState> cache;
    
    public BlockIngredientCheckTag(IOptionalNamedTag<Block> tag) {
        
        this.tag = tag;
    }
    
    @Override
    public boolean test (BlockState t) {
        
        this.buildCache();
        return this.cache.contains(t);
    }
    
    @Override
    public boolean isEmpty () {
        
        this.buildCache();
        return this.cache.isEmpty();
    }
    
    @Override
    public Collection<BlockState> getValidStates () {
        
        this.buildCache();
        return this.cache;
    }
    
    @Override
    public ResourceLocation getId () {
        
        return ID;
    }
    
    @Override
    public void invalidate () {
        
        this.cache = null;
    }
    
    private void buildCache () {
        
        if (this.cache == null) {
            
            this.cache = new ArrayList<>();
            
            for (final Block block : this.tag.getAllElements()) {
                
                block.getStateContainer().getValidStates().forEach(this.cache::add);
            }
        }
    }
    
    static class Serializer implements IBlockIngredientSerializer<BlockIngredientCheckTag> {
        
        @Override
        public BlockIngredientCheckTag read (JsonElement json) {
            
            final ResourceLocation tagName = Serializers.RESOURCE_LOCATION.read(json.getAsJsonObject(), "tag");
            return new BlockIngredientCheckTag(BlockTags.createOptional(tagName));
        }
        
        @Override
        public JsonElement write (BlockIngredientCheckTag ingredient) {
            
            final JsonObject obj = new JsonObject();
            obj.add("tag", Serializers.RESOURCE_LOCATION.write(ingredient.tag.getName()));
            return obj;
        }
        
        @Override
        public BlockIngredientCheckTag read (PacketBuffer buf) {
            
            final ResourceLocation tagName = Serializers.RESOURCE_LOCATION.read(buf);
            return new BlockIngredientCheckTag(BlockTags.createOptional(tagName));
        }
        
        @Override
        public void write (PacketBuffer buf, BlockIngredientCheckTag ingredient) {
            
            Serializers.RESOURCE_LOCATION.write(buf, ingredient.tag.getName());
        }
    }
}