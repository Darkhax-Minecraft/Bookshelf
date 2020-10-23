package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
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
    
    @Override
    public INBT writeNBT (Double toWrite) {
        
        return DoubleNBT.valueOf(toWrite);
    }
    
    @Override
    public Double read (INBT nbt) {
        
        if (nbt instanceof DoubleNBT) {
            
            return ((DoubleNBT) nbt).getDouble();
        }
        
        throw new IllegalArgumentException("Expected NBT to be a double tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}