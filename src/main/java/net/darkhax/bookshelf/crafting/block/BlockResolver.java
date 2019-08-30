package net.darkhax.bookshelf.crafting.block;

import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A block resolver that can resolve a single block.
 */
public class BlockResolver implements IBlockResolver {
    
    private final Block block;
    private final Collection<Block> collection;
    
    public BlockResolver(Block block) {
        
        this.block = block;
        this.collection = Collections.unmodifiableCollection(Collections.singleton(block));
    }
    
    @Override
    public JsonObject serialize () {
        
        final JsonObject blockObject = new JsonObject();
        blockObject.addProperty("type", this.getTypeId().toString());
        blockObject.addProperty("block", this.block.getRegistryName().toString());
        return blockObject;
    }
    
    @Override
    public Collection<Block> resolveBlocks () {
        
        return this.collection;
    }
    
    @Override
    public void serialize (PacketBuffer buffer) {
        
        buffer.writeString(this.getTypeId().toString());
        buffer.writeResourceLocation(this.block.getRegistryName());
    }
    
    public static BlockResolver deserialize (JsonObject json) {
        
        final ResourceLocation id = new ResourceLocation(json.get("block").getAsString());
        final Block block = ForgeRegistries.BLOCKS.getValue(id);
        
        if (block == null) {
            
            throw new IllegalArgumentException("Failed to find block with ID " + id.toString());
        }
        
        return new BlockResolver(block);
    }
    
    public static BlockResolver deserialize (PacketBuffer buffer) {
        
        final ResourceLocation id = buffer.readResourceLocation();
        final Block block = ForgeRegistries.BLOCKS.getValue(id);
        
        if (block == null) {
            
            throw new IllegalArgumentException("Failed to find block with ID " + id.toString());
        }
        
        return new BlockResolver(block);
    }
    
    @Override
    public ResourceLocation getTypeId () {
        
        return BlockIngredient.TYPE_BLOCK;
    }
}