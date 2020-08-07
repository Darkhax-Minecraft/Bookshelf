package net.darkhax.bookshelf.serialization;

import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;

public final class SerializerUUID implements ISerializer<UUID> {
    
    public static final ISerializer<UUID> SERIALIZER = new SerializerUUID();
    
    private SerializerUUID() {
        
    }
    
    @Override
    public UUID read (JsonElement json) {
        
        return UUID.fromString(json.getAsString());
    }
    
    @Override
    public JsonElement write (UUID toWrite) {
        
        return new JsonPrimitive(toWrite.toString());
    }
    
    @Override
    public UUID read (PacketBuffer buffer) {
        
        return UUID.fromString(buffer.readString());
    }
    
    @Override
    public void write (PacketBuffer buffer, UUID toWrite) {
        
        buffer.writeString(toWrite.toString());
    }
}