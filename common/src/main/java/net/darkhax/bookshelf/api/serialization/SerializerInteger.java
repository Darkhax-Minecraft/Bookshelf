package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerInteger implements ISerializer<Integer> {

    public static final ISerializer<Integer> SERIALIZER = new SerializerInteger();

    private SerializerInteger() {

    }

    @Override
    public Integer fromJSON(JsonElement json) {

        return json.getAsInt();
    }

    @Override
    public JsonElement toJSON(Integer toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Integer fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readInt();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Integer toWrite) {

        buffer.writeInt(toWrite);
    }

    @Override
    public Tag toNBT(Integer toWrite) {

        return IntTag.valueOf(toWrite);
    }

    @Override
    public Integer fromNBT(Tag nbt) {

        if (nbt instanceof NumericTag intTag) {

            return intTag.getAsInt();
        }

        throw new NBTParseException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}