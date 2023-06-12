package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerDouble implements ISerializer<Double> {

    public static final ISerializer<Double> SERIALIZER = new SerializerDouble();

    private SerializerDouble() {

    }

    @Override
    public Double fromJSON(JsonElement json) {

        return json.getAsDouble();
    }

    @Override
    public JsonElement toJSON(Double toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Double fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readDouble();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Double toWrite) {

        buffer.writeDouble(toWrite);
    }

    @Override
    public Tag toNBT(Double toWrite) {

        return DoubleTag.valueOf(toWrite);
    }

    @Override
    public Double fromNBT(Tag nbt) {

        if (nbt instanceof DoubleTag doubleTag) {

            return doubleTag.getAsDouble();
        }

        throw new NBTParseException("Expected NBT to be a double tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}