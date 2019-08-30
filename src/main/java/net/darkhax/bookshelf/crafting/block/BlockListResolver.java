package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A block resolver that resolves a list of block values.
 */
public class BlockListResolver implements IBlockResolver {
    
    private final Collection<Block> blocks;
    
    public BlockListResolver(Collection<Block> blocks) {
        
        this.blocks = Collections.unmodifiableCollection(blocks);
    }
    
    @Override
    public JsonObject serialize () {
        
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getTypeId().toString());
        final JsonArray blockArray = new JsonArray();
        this.blocks.forEach(block -> blockArray.add(block.getRegistryName().toString()));
        jsonObject.add("blocks", blockArray);
        return jsonObject;
    }
    
    @Override
    public Collection<Block> resolveBlocks () {
        
        return this.blocks;
    }
    
    @Override
    public void serialize (PacketBuffer buffer) {
        
        buffer.writeString(this.getTypeId().toString());
        buffer.writeInt(this.blocks.size());
        
        for (final Block block : this.blocks) {
            
            buffer.writeResourceLocation(block.getRegistryName());
        }
    }
    
    @Override
    public ResourceLocation getTypeId () {
        
        return BlockIngredient.TYPE_BLOCK_LIST;
    }
    
    public static BlockListResolver deserialize (JsonObject json) {
        
        final Collection<Block> blocks = new ArrayList<>();
        final JsonArray array = json.get("blocks").getAsJsonArray();
        
        for (int i = 0; i < array.size(); i++) {
            
            final JsonElement object = array.get(i);
            
            final ResourceLocation id = new ResourceLocation(object.getAsString());
            final Block block = ForgeRegistries.BLOCKS.getValue(id);
            
            if (block == null) {
                
                throw new IllegalArgumentException("Failed to find block with ID " + id.toString());
            }
            
            blocks.add(block);
        }
        
        return new BlockListResolver(blocks);
    }
    
    public static BlockListResolver deserialize (PacketBuffer buffer) {
        
        final int amount = buffer.readInt();
        final Collection<Block> blocks = new ArrayList<>();
        
        for (int i = 0; i < amount; i++) {
            
            final ResourceLocation id = buffer.readResourceLocation();
            final Block block = ForgeRegistries.BLOCKS.getValue(id);
            
            if (block == null) {
                
                throw new IllegalArgumentException("Failed to find block with ID " + id.toString());
            }
            
            blocks.add(block);
        }
        
        return new BlockListResolver(blocks);
    }
}