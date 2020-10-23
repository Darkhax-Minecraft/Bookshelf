package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
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
    
    @Override
    public INBT writeNBT (Boolean toWrite) {
        
        return ByteNBT.valueOf(toWrite);
    }
    
    @Override
    public Boolean read (INBT nbt) {
        
        if (nbt instanceof ByteNBT) {
            
            return ((ByteNBT) nbt).getByte() != 0;
        }
        
        throw new IllegalArgumentException("Expected NBT to be a byte/boolean tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}