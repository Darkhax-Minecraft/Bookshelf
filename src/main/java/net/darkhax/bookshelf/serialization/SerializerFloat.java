package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.network.PacketBuffer;

public final class SerializerFloat implements ISerializer<Float> {
    
    public static final ISerializer<Float> SERIALIZER = new SerializerFloat();
    
    private SerializerFloat() {
        
    }
    
    @Override
    public Float read (JsonElement json) {
        
        return json.getAsFloat();
    }
    
    @Override
    public JsonElement write (Float toWrite) {
        
        return new JsonPrimitive(toWrite);
    }
    
    @Override
    public Float read (PacketBuffer buffer) {
        
        return buffer.readFloat();
    }
    
    @Override
    public void write (PacketBuffer buffer, Float toWrite) {
        
        buffer.writeFloat(toWrite);
    }
    
    @Override
    public INBT writeNBT (Float toWrite) {
        
        return FloatNBT.valueOf(toWrite);
    }
    
    @Override
    public Float read (INBT nbt) {
        
        if (nbt instanceof NumberNBT) {
            
            return ((NumberNBT) nbt).getAsFloat();
        }
        
        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}