package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

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
            
            final String string = json.getAsString();
            
            try {
                
                return new ResourceLocation(string);
            }
            
            catch (final ResourceLocationException e) {
                
                throw new JsonParseException("Expected a valid resource location.", e);
            }
        }
        
        else {
            
            throw new JsonSyntaxException("Expected a string, was " + JSONUtils.toString(json));
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