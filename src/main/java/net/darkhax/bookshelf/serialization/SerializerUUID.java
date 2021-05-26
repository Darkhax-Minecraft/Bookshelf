package net.darkhax.bookshelf.serialization;

import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
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
        
        return UUID.fromString(buffer.readUtf());
    }
    
    @Override
    public void write (PacketBuffer buffer, UUID toWrite) {
        
        buffer.writeUtf(toWrite.toString());
    }
    
    @Override
    public INBT writeNBT (UUID toWrite) {
        
        final CompoundNBT tag = new CompoundNBT();
        tag.putUUID("id", toWrite);
        return tag;
    }
    
    @Override
    public UUID read (INBT nbt) {
        
        if (nbt instanceof CompoundNBT) {
            
            return ((CompoundNBT) nbt).getUUID("id");
        }
        
        throw new IllegalArgumentException("Expected NBT to be a compund tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}