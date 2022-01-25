package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerLong implements ISerializer<Long> {

    public static final ISerializer<Long> SERIALIZER = new SerializerLong();

    private SerializerLong() {

    }

    @Override
    public Long fromJSON(JsonElement json) {

        return json.getAsLong();
    }

    @Override
    public JsonElement toJSON(Long toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Long fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readLong();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Long toWrite) {

        buffer.writeLong(toWrite);
    }

    @Override
    public Tag toNBT(Long toWrite) {

        return LongTag.valueOf(toWrite);
    }

    @Override
    public Long fromNBT(Tag nbt) {

        if (nbt instanceof NumericTag longTag) {

            return longTag.getAsLong();
        }

        throw new NBTParseException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}