package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerBoolean implements ISerializer<Boolean> {

    public static final ISerializer<Boolean> SERIALIZER = new SerializerBoolean();

    private SerializerBoolean() {

    }

    @Override
    public Boolean fromJSON(JsonElement json) {

        return json.getAsBoolean();
    }

    @Override
    public JsonElement toJSON(Boolean toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Boolean fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readBoolean();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Boolean toWrite) {

        buffer.writeBoolean(toWrite);
    }

    @Override
    public Tag toNBT(Boolean toWrite) {

        return ByteTag.valueOf(toWrite);
    }

    @Override
    public Boolean fromNBT(Tag nbt) {

        if (nbt instanceof ByteTag byteTag) {

            return byteTag.getAsByte() != 0;
        }

        throw new NBTParseException("Expected NBT to be a byte/boolean tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}