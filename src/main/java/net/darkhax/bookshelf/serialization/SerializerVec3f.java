package net.darkhax.bookshelf.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.math.Vector3f;

public class SerializerVec3f implements ISerializer<Vector3f> {

    public static final ISerializer<Vector3f> SERIALIZER = new SerializerVec3f();

    @Override
    public Vector3f read (JsonElement json) {

        final List<Float> values = Serializers.FLOAT.readList(json);

        if (values.size() == 3) {

            return new Vector3f(values.get(0), values.get(1), values.get(2));
        }

        throw new JsonParseException("Expected 3 elements, had " + values.size() + " instead.");
    }

    @Override
    public JsonElement write (Vector3f toWrite) {

        return Serializers.FLOAT.writeList(new ArrayList<>(Arrays.asList(toWrite.x(), toWrite.y(), toWrite.z())));
    }

    @Override
    public Vector3f read (FriendlyByteBuf buffer) {

        final List<Float> values = Serializers.FLOAT.readList(buffer);

        if (values.size() == 3) {

            return new Vector3f(values.get(0), values.get(1), values.get(2));
        }

        throw new IllegalStateException("Expected 3 elements, had " + values.size() + " instead.");
    }

    @Override
    public void write (FriendlyByteBuf buffer, Vector3f toWrite) {

        Serializers.FLOAT.writeList(buffer, new ArrayList<>(Arrays.asList(toWrite.x(), toWrite.y(), toWrite.z())));
    }

    @Override
    public Tag writeNBT (Vector3f toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putFloat("x", toWrite.x());
        tag.putFloat("y", toWrite.y());
        tag.putFloat("z", toWrite.z());
        return tag;
    }

    @Override
    public Vector3f read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            final CompoundTag tag = (CompoundTag) nbt;
            final float x = tag.getFloat("x");
            final float y = tag.getFloat("y");
            final float z = tag.getFloat("z");
            return new Vector3f(x, y, z);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}