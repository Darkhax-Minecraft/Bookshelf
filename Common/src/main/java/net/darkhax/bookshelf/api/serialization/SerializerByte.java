package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public final class SerializerByte implements ISerializer<Byte> {

    public static final ISerializer<Byte> SERIALIZER = new SerializerByte();

    private SerializerByte() {

    }

    @Override
    public Byte fromJSON(JsonElement json) {

        return json.getAsByte();
    }

    @Override
    public JsonElement toJSON(Byte toWrite) {

        return new JsonPrimitive(toWrite);
    }

    @Override
    public Byte fromByteBuf(FriendlyByteBuf buffer) {

        return buffer.readByte();
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Byte toWrite) {

        buffer.writeByte(toWrite);
    }

    @Override
    public Tag toNBT(Byte toWrite) {

        return ByteTag.valueOf(toWrite);
    }

    @Override
    public Byte fromNBT(Tag nbt) {

        if (nbt instanceof NumericTag byteTag) {

            return byteTag.getAsByte();
        }

        throw new NBTParseException("Expected NBT to be a number tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}