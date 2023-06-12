package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerShort implements ISerializer<Short> {

    public static final ISerializer<Short> SERIALIZER = new SerializerShort();

    private SerializerShort() {

    }

    @Override
    public Short fromJSON(JsonElement json) {

        return json.getAsShort();
    }

    @Override
    public JsonElement toJSON(Short toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Short fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readShort();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Short toWrite) {

        buffer.writeShort(toWrite);
    }

    @Override
    public Tag toNBT(Short toWrite) {

        return ShortTag.valueOf(toWrite);
    }

    @Override
    public Short fromNBT(Tag nbt) {

        if (nbt instanceof NumericTag shortTag) {

            return shortTag.getAsShort();
        }

        throw new NBTParseException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}