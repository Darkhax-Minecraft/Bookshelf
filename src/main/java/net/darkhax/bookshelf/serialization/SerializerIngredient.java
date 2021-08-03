package net.darkhax.bookshelf.serialization;

import com.google.gson.JsonElement;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;

public class SerializerIngredient implements ISerializer<Ingredient> {

    public static final ISerializer<Ingredient> SERIALIZER = new SerializerIngredient();

    private SerializerIngredient () {

    }

    @Override
    public Ingredient read (JsonElement json) {

        return Ingredient.fromJson(json);
    }

    @Override
    public JsonElement write (Ingredient toWrite) {

        return toWrite.toJson();
    }

    @Override
    public Ingredient read (FriendlyByteBuf buffer) {

        return Ingredient.fromNetwork(buffer);
    }

    @Override
    public void write (FriendlyByteBuf buffer, Ingredient toWrite) {

        toWrite.toNetwork(buffer);
    }
}