package net.darkhax.bookshelf.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class SerializerNBT implements ISerializer<CompoundNBT> {
    
    public static final ISerializer<CompoundNBT> SERIALIZER = new SerializerNBT();
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    private SerializerNBT() {
        
    }
    
    @Override
    public CompoundNBT read (JsonElement json) {
        
        try {
            
            if (json.isJsonObject()) {
                
                // Some numbers like shorts and array tags like IntArrayNBT can not be
                // deserialized from objects due to NBT spec being dumb.
                return JsonToNBT.parseTag(GSON.toJson(json));
            }
            
            else {
                
                return JsonToNBT.parseTag(JSONUtils.convertToString(json, "nbt"));
            }
        }
        
        catch (final CommandSyntaxException e) {
            
            throw new JsonParseException("Failed to read NBT from " + JSONUtils.getType(json), e);
        }
    }
    
    @Override
    public JsonElement write (CompoundNBT toWrite) {
        
        // While object is supported, some objects can never be deserialized properly.
        return new JsonPrimitive(toWrite.toString());
    }
    
    @Override
    public CompoundNBT read (PacketBuffer buffer) {
        
        return buffer.readNbt();
    }
    
    @Override
    public void write (PacketBuffer buffer, CompoundNBT toWrite) {
        
        buffer.writeNbt(toWrite);
    }
    
    @Override
    public INBT writeNBT (CompoundNBT toWrite) {
        
        return toWrite;
    }
    
    @Override
    public CompoundNBT read (INBT nbt) {
        
        if (nbt instanceof CompoundNBT) {
            
            return (CompoundNBT) nbt;
        }
        
        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}