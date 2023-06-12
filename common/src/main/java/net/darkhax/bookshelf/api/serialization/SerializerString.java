package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerString implements ISerializer<String> {

    public static final ISerializer<String> SERIALIZER = new SerializerString();

    private SerializerString() {

    }

    @Override
    public String fromJSON(JsonElement json) {

        return json.getAsString();
    }

    @Override
    public JsonElement toJSON(String toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public String fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readUtf();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, String toWrite) {

        buffer.writeUtf(toWrite);
    }

    @Override
    public Tag toNBT(String toWrite) {

        return StringTag.valueOf(toWrite);
    }

    @Override
    public String fromNBT(Tag nbt) {

        if (nbt instanceof StringTag stringTag) {

            return stringTag.getAsString();
        }

        throw new NBTParseException("Expected NBT to be a string tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}