package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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
        
        return buffer.readUtf();
    }
    
    @Override
    public void write (PacketBuffer buffer, String toWrite) {
        
        buffer.writeUtf(toWrite);
    }
    
    @Override
    public INBT writeNBT (String toWrite) {
        
        return StringNBT.valueOf(toWrite);
    }
    
    @Override
    public String read (INBT nbt) {
        
        if (nbt instanceof StringNBT) {
            
            return ((StringNBT) nbt).getAsString();
        }
        
        throw new IllegalArgumentException("Expected NBT to be a string tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}