package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;

public class SerializerIngredient implements ISerializer<Ingredient> {
    
    public static final ISerializer<Ingredient> SERIALIZER = new SerializerIngredient();
    
    private SerializerIngredient() {
        
    }
    
    @Override
    public Ingredient read (JsonElement json) {
        
        return Ingredient.deserialize(json);
    }
    
    @Override
    public JsonElement write (Ingredient toWrite) {
        
        return toWrite.serialize();
    }
    
    @Override
    public Ingredient read (PacketBuffer buffer) {
        
        return Ingredient.read(buffer);
    }
    
    @Override
    public void write (PacketBuffer buffer, Ingredient toWrite) {
        
        toWrite.write(buffer);
    }
}