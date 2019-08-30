package net.darkhax.bookshelf.crafting.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * A block resolver that handles vanilla block tags.
 */
public class BlockTagResolver implements IBlockResolver {
    
    private final Tag<Block> blockTag;
    
    public BlockTagResolver(Tag<Block> tag) {
        
        this.blockTag = tag;
    }
    
    @Override
    public Collection<Block> resolveBlocks () {
        
        final List<Block> blocks = new ArrayList<>();
        blocks.addAll(this.blockTag.getAllElements());
        return blocks;
    }
    
    @Override
    public JsonObject serialize () {
        
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getTypeId().toString());
        jsonObject.addProperty("tag", this.blockTag.getId().toString());
        return jsonObject;
    }
    
    @Override
    public void serialize (PacketBuffer buffer) {
        
        buffer.writeString(this.getTypeId().toString());
        buffer.writeResourceLocation(this.blockTag.getId());
    }
    
    @Override
    public ResourceLocation getTypeId () {
        
        return BlockIngredient.TYPE_BLOCK_TAG;
    }
    
    public static BlockTagResolver deserialize (JsonObject json) {
        
        final ResourceLocation tagId = new ResourceLocation(json.get("tag").getAsString());
        final Tag<Block> tag = BlockTags.getCollection().get(tagId);
        
        if (tag == null) {
            
            throw new IllegalArgumentException("Could not resolve block tag for " + tagId.toString());
        }
        
        return new BlockTagResolver(tag);
    }
    
    public static BlockTagResolver deserialize (PacketBuffer buffer) {
        
        final ResourceLocation tagId = buffer.readResourceLocation();
        final Tag<Block> tag = BlockTags.getCollection().get(tagId);
        
        if (tag == null) {
            
            throw new IllegalArgumentException("Could not resolve block tag for " + tagId.toString());
        }
        
        return new BlockTagResolver(tag);
    }
}