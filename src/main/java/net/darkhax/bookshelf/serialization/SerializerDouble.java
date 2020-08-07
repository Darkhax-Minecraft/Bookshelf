package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.network.PacketBuffer;

public final class SerializerDouble implements ISerializer<Double> {
    
    public static final ISerializer<Double> SERIALIZER = new SerializerDouble();
    
    private SerializerDouble() {
        
    }
    
    @Override
    public Double read (JsonElement json) {
        
        return json.getAsDouble();
    }
    
    @Override
    public JsonElement write (Double toWrite) {
        
        return new JsonPrimitive(toWrite);
    }
    
    @Override
    public Double read (PacketBuffer buffer) {
        
        return buffer.readDouble();
    }
    
    @Override
    public void write (PacketBuffer buffer, Double toWrite) {
        
        buffer.writeDouble(toWrite);
    }
}