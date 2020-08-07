package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;

public final class SerializerByte implements ISerializer<Byte> {
    
    public static final ISerializer<Byte> SERIALIZER = new SerializerByte();
    
    private SerializerByte() {
        
    }
    
    @Override
    public Byte read (JsonElement json) {
        
        return json.getAsByte();
    }
    
    @Override
    public JsonElement write (Byte toWrite) {
        
        return new JsonPrimitive(toWrite);
    }
    
    @Override
    public Byte read (PacketBuffer buffer) {
        
        return buffer.readByte();
    }
    
    @Override
    public void write (PacketBuffer buffer, Byte toWrite) {
        
        buffer.writeByte(toWrite);
    }
}