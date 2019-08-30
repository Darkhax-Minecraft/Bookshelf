package net.darkhax.bookshelf.crafting.block;

import java.util.Collection;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * This interface defines the backing requirements for a block resolver.
 */
public interface IBlockResolver {
    
    /**
     * Serialize the resolver to a json object.
     * 
     * @return The serializes json object.
     */
    JsonObject serialize ();
    
    /**
     * Serialize the resolver to a packet buffer.
     * 
     * @param buffer The buffer to write to.
     */
    void serialize (PacketBuffer buffer);
    
    /**
     * Resolves the blocks to make valid.
     * 
     * @return The blocks that are now valid.
     */
    Collection<Block> resolveBlocks ();
    
    /**
     * The ID for the resolver. This should be consistent based on the type. It is used to
     * lookup deserializers.
     * 
     * @return The type ID used to deserialize the resolver.
     */
    ResourceLocation getTypeId ();
}