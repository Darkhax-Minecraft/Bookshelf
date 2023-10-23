package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public class SerializerIngredient implements ISerializer<Ingredient> {

    public static final ISerializer<Ingredient> SERIALIZER = new SerializerIngredient();

    private SerializerIngredient() {

    }

    @Override
    public Ingredient fromJSON(JsonElement json) {

        // Support Empty Ingredient
        if (json instanceof JsonArray array && array.isEmpty()) {

            return Ingredient.EMPTY;
        }

        return Ingredient.CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow();
    }

    @Override
    public JsonElement toJSON(Ingredient toWrite) {

        return toWrite.toJson(true);
    }

    @Override
    public Ingredient fromByteBuf(FriendlyByteBuf buffer) {

        return Ingredient.fromNetwork(buffer);
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Ingredient toWrite) {

        toWrite.toNetwork(buffer);
    }

    @Override
    public Tag toNBT(Ingredient toWrite) {

        return Serializers.STRING.toNBT(this.toJSONString(toWrite));
    }

    @Override
    public Ingredient fromNBT(Tag nbt) {

        return this.fromJSONString(Serializers.STRING.fromNBT(nbt));
    }
}