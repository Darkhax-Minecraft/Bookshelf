package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;

public final class SerializerBoolean implements ISerializer<Boolean> {
    
    public static final ISerializer<Boolean> SERIALIZER = new SerializerBoolean();
    
    private SerializerBoolean() {
        
    }
    
    @Override
    public Boolean read (JsonElement json) {
        
        return json.getAsBoolean();
    }
    
    @Override
    public JsonElement write (Boolean toWrite) {
        
        return new JsonPrimitive(toWrite);
    }
    
    @Override
    public Boolean read (PacketBuffer buffer) {
        
        return buffer.readBoolean();
    }
    
    @Override
    public void write (PacketBuffer buffer, Boolean toWrite) {
        
        buffer.writeBoolean(toWrite);
    }
}