package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerFloat implements ISerializer<Float> {

    public static final ISerializer<Float> SERIALIZER = new SerializerFloat();

    private SerializerFloat() {

    }

    @Override
    public Float fromJSON(JsonElement json) {

        return json.getAsFloat();
    }

    @Override
    public JsonElement toJSON(Float toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Float fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readFloat();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Float toWrite) {

        buffer.writeFloat(toWrite);
    }

    @Override
    public Tag toNBT(Float toWrite) {

        return FloatTag.valueOf(toWrite);
    }

    @Override
    public Float fromNBT(Tag nbt) {

        if (nbt instanceof NumericTag floatTag) {

            return floatTag.getAsFloat();
        }

        throw new NBTParseException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}