package net.darkhax.bookshelf.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class SerializerVec3d implements ISerializer<Vec3> {

    public static final ISerializer<Vec3> SERIALIZER = new SerializerVec3d();

    @Override
    public Vec3 read (JsonElement json) {

        final List<Double> values = Serializers.DOUBLE.readList(json);

        if (values.size() == 3) {

            return new Vec3(values.get(0), values.get(1), values.get(2));
        }

        throw new JsonParseException("Expected 3 elements, had " + values.size() + " instead.");
    }

    @Override
    public JsonElement write (Vec3 toWrite) {

        return Serializers.DOUBLE.writeList(new ArrayList<>(Arrays.asList(toWrite.x, toWrite.y, toWrite.z)));
    }

    @Override
    public Vec3 read (FriendlyByteBuf buffer) {

        final List<Double> values = Serializers.DOUBLE.readList(buffer);

        if (values.size() == 3) {

            return new Vec3(values.get(0), values.get(1), values.get(2));
        }

        throw new IllegalStateException("Expected 3 elements, had " + values.size() + " instead.");
    }

    @Override
    public void write (FriendlyByteBuf buffer, Vec3 toWrite) {

        Serializers.DOUBLE.writeList(buffer, new ArrayList<>(Arrays.asList(toWrite.x, toWrite.y, toWrite.z)));
    }

    @Override
    public Tag writeNBT (Vec3 toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putDouble("x", toWrite.x());
        tag.putDouble("y", toWrite.y());
        tag.putDouble("z", toWrite.z());
        return tag;
    }

    @Override
    public Vec3 read (Tag nbt) {

        if (nbt instanceof CompoundTag) {

            final CompoundTag tag = (CompoundTag) nbt;
            final double x = tag.getDouble("x");
            final double y = tag.getDouble("y");
            final double z = tag.getDouble("z");
            return new Vec3(x, y, z);
        }

        throw new IllegalArgumentException("Expected NBT to be a compound tag. Class was " + nbt.getClass() + " with ID " + nbt.getId() + " instead.");
    }
}