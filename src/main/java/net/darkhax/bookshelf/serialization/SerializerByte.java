package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
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
    
    @Override
    public INBT writeNBT (Byte toWrite) {
        
        return ByteNBT.valueOf(toWrite);
    }
    
    @Override
    public Byte read (INBT nbt) {
        
        if (nbt instanceof NumberNBT) {
            
            return ((NumberNBT) nbt).getAsByte();
        }
        
        throw new IllegalArgumentException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}