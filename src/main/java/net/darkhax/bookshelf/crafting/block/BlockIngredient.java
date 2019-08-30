package net.darkhax.bookshelf.crafting.block;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * This class provides an implementation of block ingredient. It is similar to the vanilla
 * Ingredient class however it is designed to match Block objects instead of Item objects. It
 * comes with full serialization support for to and from json conversions.
 */
public class BlockIngredient implements Predicate<Block> {
    
    public static final ResourceLocation TYPE_BLOCK = new ResourceLocation(Bookshelf.MOD_ID, "block");
    public static final ResourceLocation TYPE_BLOCK_LIST = new ResourceLocation(Bookshelf.MOD_ID, "block_list");
    public static final ResourceLocation TYPE_BLOCK_TAG = new ResourceLocation(Bookshelf.MOD_ID, "block_tag");
    
    /**
     * A map of packet deserializers for block resolvers.
     */
    private static Map<ResourceLocation, Function<PacketBuffer, IBlockResolver>> packetDeserializers = new HashMap<>();
    
    /**
     * A map of json deserializers for block resolvers.
     */
    private static Map<ResourceLocation, Function<JsonObject, IBlockResolver>> jsonDeserializers = new HashMap<>();
    
    static {
        
        packetDeserializers.put(TYPE_BLOCK, BlockResolver::deserialize);
        packetDeserializers.put(TYPE_BLOCK_LIST, BlockListResolver::deserialize);
        packetDeserializers.put(TYPE_BLOCK_TAG, BlockTagResolver::deserialize);
        
        jsonDeserializers.put(TYPE_BLOCK, BlockResolver::deserialize);
        jsonDeserializers.put(TYPE_BLOCK_LIST, BlockListResolver::deserialize);
        jsonDeserializers.put(TYPE_BLOCK_TAG, BlockTagResolver::deserialize);
    }
    
    /**
     * An array containing all of the block resolvers for the ingredient. This is where the
     * valid blocks for the ingredient come from.
     */
    private final IBlockResolver[] resolvers;
    
    /**
     * A list of all the valid blocks.
     */
    private List<Block> validBlocks;
    
    /**
     * Creates a new block ingredient.
     * 
     * @param resolvers The resolvers to use for the ingredient.
     */
    public BlockIngredient(IBlockResolver... resolvers) {
        
        this.resolvers = resolvers;
    }
    
    @Override
    public boolean test (Block toTest) {
        
        this.determineMatchingBlocks();
        
        if (toTest != null && this.validBlocks != null && !this.validBlocks.isEmpty()) {
            
            return this.validBlocks.contains(toTest);
        }
        
        return false;
    }
    
    /**
     * Resolves the various resolvers from {@link #resolvers}. If the valid block list exists
     * then it will be ignored. You can call {@link #invalidate()} to reset the list and allow
     * this method to be used again.
     */
    private void determineMatchingBlocks () {
        
        if (this.validBlocks == null) {
            
            this.validBlocks = Collections.unmodifiableList(Arrays.stream(this.resolvers).flatMap(resolver -> resolver.resolveBlocks().stream()).distinct().collect(Collectors.toList()));
        }
    }
    
    /**
     * Invalidates the internal list of valid blocks.
     */
    public void invalidate () {
        
        this.validBlocks = null;
    }
    
    /**
     * Serializes the ingredient into a json element. If there is one serializer this will be
     * an object. If there are more it will be an array.
     * 
     * @return The serialized json object.
     */
    public JsonElement serialize () {
        
        if (this.resolvers.length == 1) {
            
            return this.resolvers[0].serialize();
        }
        
        final JsonArray resolverArray = new JsonArray();
        
        for (final IBlockResolver resolver : this.resolvers) {
            
            resolverArray.add(resolver.serialize());
        }
        
        return resolverArray;
    }
    
    /**
     * Serializes the ingredient to a packet buffer.
     * 
     * @param buffer The buffer to write the block data to.
     */
    public void serialize (PacketBuffer buffer) {
        
        buffer.writeInt(this.resolvers.length);
        
        for (final IBlockResolver resolver : this.resolvers) {
            
            resolver.serialize(buffer);
        }
    }
    
    /**
     * Gets a list of valid blocks. This list is unmodifiable.
     * 
     * @return An unmodifiable list of valid blocks.
     */
    public List<Block> getValidBlocks () {
        
        this.determineMatchingBlocks();
        return this.validBlocks;
    }
    
    /**
     * Deserializes a block ingredient from a packet buffer.
     * 
     * @param buffer The packet buffer.
     * @return The deserialized ingredient, and all it's resolvers.
     */
    public static BlockIngredient deserialize (PacketBuffer buffer) {
        
        final int count = buffer.readInt();
        final IBlockResolver[] blockResolvers = new IBlockResolver[count];
        
        for (int i = 0; i < count; i++) {
            
            final ResourceLocation typeId = ResourceLocation.tryCreate(buffer.readString());
            
            if (typeId != null) {
                
                blockResolvers[i] = packetDeserializers.get(typeId).apply(buffer);
            }
        }
        
        return new BlockIngredient(blockResolvers);
    }
    
    /**
     * Deserializes a block ingredient from a json element.
     * 
     * @param json The json element to read from.
     * @return The deserialized ingredient, and all it's resolvers.
     */
    public static BlockIngredient deserialize (JsonElement json) {
        
        IBlockResolver[] blockResolvers = null;
        
        if (json.isJsonArray()) {
            
            final JsonArray array = json.getAsJsonArray();
            
            blockResolvers = new IBlockResolver[array.size()];
            
            for (int i = 0; i < blockResolvers.length; i++) {
                
                final JsonElement element = array.get(i);
                
                if (element.isJsonObject()) {
                    
                    final JsonObject object = element.getAsJsonObject();
                    final ResourceLocation typeId = ResourceLocation.tryCreate(object.get("type").getAsString());
                    blockResolvers[i] = jsonDeserializers.get(typeId).apply(object);
                }
            }
        }
        
        else if (json.isJsonObject()) {
            
            final JsonObject jsonObject = json.getAsJsonObject();
            blockResolvers = new IBlockResolver[1];
            final ResourceLocation typeId = ResourceLocation.tryCreate(jsonObject.get("type").getAsString());
            blockResolvers[0] = jsonDeserializers.get(typeId).apply(jsonObject);
        }
        
        else {
            
            throw new IllegalArgumentException("Tried to deserialize from an unsupported json type! " + json.toString());
        }
        
        return new BlockIngredient(blockResolvers);
    }
}