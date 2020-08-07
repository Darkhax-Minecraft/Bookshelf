package net.darkhax.bookshelf.serialization;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class SerializerBlockState implements ISerializer<BlockState> {
    
    public static final ISerializer<BlockState> SERIALIZER = new SerializerBlockState();
    
    private SerializerBlockState() {
        
    }
    
    @Override
    public BlockState read (JsonElement json) {
        
        if (json.isJsonObject()) {
            
            final JsonObject obj = json.getAsJsonObject();
            final Block block = Serializers.BLOCK.read(obj, "block");
            
            BlockState state = block.getDefaultState();
            
            if (obj.has("properties")) {
                
                final JsonElement properties = obj.get("properties");
                
                if (properties.isJsonObject()) {
                    
                    for (final Entry<String, JsonElement> property : properties.getAsJsonObject().entrySet()) {
                        
                        state = this.readProperty(state, property.getKey(), property.getValue());
                    }
                }
            }
            
            return state;
        }
        
        else {
            
            throw new JsonParseException("Expected properties to be an object. Recieved " + JSONUtils.toString(json));
        }
    }
    
    @Override
    public JsonElement write (BlockState toWrite) {
        
        final JsonObject json = new JsonObject();
        json.add("block", Serializers.BLOCK.write(toWrite.getBlock()));
        
        final JsonObject properties = new JsonObject();
        
        for (final Property prop : toWrite.getProperties()) {
            
            properties.addProperty(prop.getName(), prop.getName(toWrite.get(prop)));
        }
        
        json.add("properties", properties);
        return json;
    }
    
    @Override
    public BlockState read (PacketBuffer buffer) {
        
        final ResourceLocation id = buffer.readResourceLocation();
        final Block block = ForgeRegistries.BLOCKS.getValue(id);
        
        if (block != null) {
            
            final int size = buffer.readInt();
            
            BlockState state = block.getDefaultState();
            
            for (int i = 0; i < size; i++) {
                
                final String propName = buffer.readString();
                final String value = buffer.readString();
                
                final Property blockProperty = block.getStateContainer().getProperty(propName);
                
                if (blockProperty != null) {
                    
                    final Optional<Comparable> propValue = blockProperty.parseValue(value);
                    
                    if (propValue.isPresent()) {
                        
                        try {
                            
                            state = state.with(blockProperty, propValue.get());
                        }
                        
                        catch (final Exception e) {
                            
                            Bookshelf.LOG.error("Failed to read state for block {}. The mod that adds this block may have issues.", block.getRegistryName());
                            Bookshelf.LOG.catching(e);
                            throw e;
                        }
                    }
                }
            }
            
            return state;
        }
        
        throw new IllegalStateException("Tried to read null block " + id.toString());
    }
    
    @Override
    public void write (PacketBuffer buffer, BlockState toWrite) {
        
        buffer.writeResourceLocation(toWrite.getBlock().getRegistryName());
        
        final Collection<Property<?>> properties = toWrite.getProperties();
        
        buffer.writeInt(properties.size());
        
        for (final Property property : properties) {
            
            buffer.writeString(property.getName());
            buffer.writeString(toWrite.get(property).toString());
        }
    }
    
    private BlockState readProperty (BlockState state, String propName, JsonElement propValue) {
        
        final Property blockProperty = state.getBlock().getStateContainer().getProperty(propName);
        
        if (blockProperty != null) {
            
            if (propValue.isJsonPrimitive()) {
                
                final String valueString = propValue.getAsString();
                final Optional<Comparable> parsedValue = blockProperty.parseValue(valueString);
                
                if (parsedValue.isPresent()) {
                    
                    try {
                        
                        return state.with(blockProperty, parsedValue.get());
                    }
                    
                    catch (final Exception e) {
                        
                        Bookshelf.LOG.error("Failed to update state for block {}. The mod that adds this block may have an issue.", state.getBlock().getRegistryName());
                        Bookshelf.LOG.catching(e);
                        throw e;
                    }
                }
                
                else {
                    
                    throw new JsonSyntaxException("The property " + propName + " with value " + valueString + " coul not be parsed!");
                }
            }
            
            else {
                
                throw new JsonSyntaxException("Expected property value for " + propName + " to be primitive string. Got " + JSONUtils.toString(propValue));
            }
        }
        
        else {
            
            throw new JsonSyntaxException("The property " + propName + " is not valid for block " + state.getBlock().getRegistryName());
        }
    }
}