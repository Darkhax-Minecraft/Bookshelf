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
    
    private final ResourceLocation tagId;
    
    public BlockTagResolver(ResourceLocation tagId) {
        
        this.tagId = tagId;
    }
    
    @Override
    public Collection<Block> resolveBlocks () {
        
        final List<Block> blocks = new ArrayList<>();
        
        Tag<Block> tag = BlockTags.getCollection().get(this.tagId);
        
        if (tag != null) {
            
            blocks.addAll(tag.getAllElements());
        }
        
        return blocks;
    }
    
    @Override
    public JsonObject serialize () {
        
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getTypeId().toString());
        jsonObject.addProperty("tag", this.tagId.toString());
        return jsonObject;
    }
    
    @Override
    public void serialize (PacketBuffer buffer) {
        
        buffer.writeString(this.getTypeId().toString());
        buffer.writeResourceLocation(this.tagId);
    }
    
    @Override
    public ResourceLocation getTypeId () {
        
        return BlockIngredient.TYPE_BLOCK_TAG;
    }
    
    public static BlockTagResolver deserialize (JsonObject json) {
        
        final ResourceLocation tagId = ResourceLocation.tryCreate(json.get("tag").getAsString());       
        return new BlockTagResolver(tagId);
    }
    
    public static BlockTagResolver deserialize (PacketBuffer buffer) {
        
        final ResourceLocation tagId = buffer.readResourceLocation();        
        return new BlockTagResolver(tagId);
    }
}