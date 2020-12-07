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
import net.minecraft.util.ResourceLocation;

public class BlockIngredientCheckBlock extends BlockIngredient {
    
    public static final ResourceLocation ID = new ResourceLocation(Bookshelf.MOD_ID, "check_block");
    public static final Serializer SERIALIZER = new Serializer();
    
    private final List<Block> blocks;
    private List<BlockState> cache;
    
    public BlockIngredientCheckBlock(List<Block> blocks) {
        
        this.blocks = blocks;
    }
    
    @Override
    public boolean test (BlockState t) {
        
        this.buildCache();
        return this.cache.contains(t);
    }
    
    @Override
    public Collection<BlockState> getValidStates () {
        
        this.buildCache();
        return this.cache;
    }
    
    @Override
    public ResourceLocation getSerializeId () {
        
        return ID;
    }
    
    private void buildCache () {
        
        if (this.cache == null) {
            
            this.cache = new ArrayList<>();
            
            for (final Block block : this.blocks) {
                
                block.getStateContainer().getValidStates().forEach(this.cache::add);
            }
        }
    }
    
    static class Serializer implements IBlockIngredientSerializer<BlockIngredientCheckBlock> {
        
        @Override
        public BlockIngredientCheckBlock read (JsonElement json) {
            
            final List<Block> states = Serializers.BLOCK.readList(json.getAsJsonObject(), "block");
            return new BlockIngredientCheckBlock(states);
        }
        
        @Override
        public JsonElement write (BlockIngredientCheckBlock ingredient) {
            
            final JsonObject obj = new JsonObject();
            obj.add("block", Serializers.BLOCK.writeList(ingredient.blocks));
            return obj;
        }
        
        @Override
        public BlockIngredientCheckBlock read (PacketBuffer buf) {
            
            final List<Block> states = Serializers.BLOCK.readList(buf);
            return new BlockIngredientCheckBlock(states);
        }
        
        @Override
        public void write (PacketBuffer buf, BlockIngredientCheckBlock ingredient) {
            
            Serializers.BLOCK.writeList(buf, ingredient.blocks);
        }
    }
}