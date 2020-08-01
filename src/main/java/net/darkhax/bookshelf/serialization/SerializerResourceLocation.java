package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;

public final class SerializerResourceLocation implements ISerializer<ResourceLocation> {
    
    public static final ISerializer<ResourceLocation> SERIALIZER = new SerializerResourceLocation();
    
    private SerializerResourceLocation() {
        
    }
    
    @Override
    public ResourceLocation read (JsonElement json) {
        
        if (json.isJsonPrimitive()) {
            
            final JsonPrimitive primitive = json.getAsJsonPrimitive();
            
            if (primitive.isString()) {
                
                final String text = primitive.getAsString();
                
                try {
                    
                    return new ResourceLocation(text);
                }
                
                catch (final ResourceLocationException e) {
                    
                    throw new JsonParseException("Expected a valid resource location.", e);
                }
            }
            
            else {
                
                throw new JsonParseException("Expected a string, got " + JSONUtils.toString(json));
            }
        }
        
        else {
            
            throw new JsonParseException("Expected a string, got " + JSONUtils.toString(json));
        }
    }
    
    @Override
    public JsonElement write (ResourceLocation toWrite) {
        
        return new JsonPrimitive(toWrite.toString());
    }
    
    @Override
    public ResourceLocation read (PacketBuffer buffer) {
        
        return buffer.readResourceLocation();
    }
    
    @Override
    public void write (PacketBuffer buffer, ResourceLocation toWrite) {
        
        buffer.writeResourceLocation(toWrite);
    }
}