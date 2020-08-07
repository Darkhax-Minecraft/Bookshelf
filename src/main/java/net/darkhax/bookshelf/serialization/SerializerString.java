package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;

public final class SerializerString implements ISerializer<String> {
    
    public static final ISerializer<String> SERIALIZER = new SerializerString();
    
    private SerializerString() {
        
    }
    
    @Override
    public String read (JsonElement json) {
        
        return json.getAsString();
    }
    
    @Override
    public JsonElement write (String toWrite) {
        
        return new JsonPrimitive(toWrite);
    }
    
    @Override
    public String read (PacketBuffer buffer) {
        
        return buffer.readString();
    }
    
    @Override
    public void write (PacketBuffer buffer, String toWrite) {
        
        buffer.writeString(toWrite);
    }
}