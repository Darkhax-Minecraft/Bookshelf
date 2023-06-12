package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.darkhax.bookshelf.api.util.JSONHelper;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.EnumUtils;

import java.util.Locale;

public class SerializerEnum<T extends Enum<T>> implements ISerializer<T> {

    public final T defaultValue;
    private final Class<T> enumClass;

    public SerializerEnum(Class<T> enumClass) {

        this(enumClass, null);
    }

    public SerializerEnum(Class<T> enumClass, T defaultValue) {

        this.enumClass = enumClass;
        this.defaultValue = defaultValue;
    }

    @Override
    public T fromJSON(JsonElement json) {

        return this.getByName(JSONHelper.getAsString(json));
    }

    @Override
    public JsonElement toJSON(T toWrite) {

        return new JsonPrimitive(toWrite.name());
    }

    @Override
    public T fromByteBuf(FriendlyByteBuf buffer) {

        return this.getByName(buffer.readUtf());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, T toWrite) {

        buffer.writeUtf(toWrite.name());
    }

    @Override
    public Tag toNBT(T toWrite) {

        return StringTag.valueOf(toWrite.name());
    }

    @Override
    public T fromNBT(Tag nbt) {

        if (nbt instanceof IntTag integer) {

            return this.getByIndex(integer.getAsInt());
        }

        if (nbt instanceof StringTag stringTag) {

            return this.getByName(stringTag.getAsString());
        }

        throw new NBTParseException("Expected NBT to be a String tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }

    private T getByIndex(int index) {

        final T[] values = enumClass.getEnumConstants();

        if (index < 0 || index > values.length) {

            throw new IndexOutOfBoundsException("Cannot read enum by index. Class: " + enumClass.getCanonicalName() + " Index: " + index + " Size: " + values.length);
        }

        return values[index];
    }

    private T getByName(String name) {

        T value = EnumUtils.getEnum(this.enumClass, name);

        if (value == null) {

            value = EnumUtils.getEnum(this.enumClass, name.toUpperCase(Locale.ROOT));
        }

        if (value == null) {

            value = this.defaultValue;
        }

        return value;
    }
}