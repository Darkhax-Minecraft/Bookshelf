package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3f;

public class SerializerVector3f implements ISerializer<Vector3f> {

    public static final ISerializer<Vector3f> SERIALIZER = new SerializerVector3f();

    @Override
    public Vector3f fromJSON(JsonElement json) {

        // If it's an object explicitly read each axis.
        if (json.isJsonObject()) {

            final JsonObject object = json.getAsJsonObject();
            final float x = Serializers.FLOAT.fromJSON(object, "x");
            final float y = Serializers.FLOAT.fromJSON(object, "y");
            final float z = Serializers.FLOAT.fromJSON(object, "z");
            return new Vector3f(x, y, z);
        }

        // If it's an array of 3 elements read in X,Y,Z order.
        else if (json.isJsonArray()) {

            final JsonArray array = json.getAsJsonArray();

            if (array.size() == 3) {

                return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
            }

            throw new JsonParseException("Invalid number of args in array. Expected 3 but got " + array.size());
        }

        throw new JsonParseException("Vector3f data is not in a readable format.");
    }

    @Override
    public JsonElement toJSON(Vector3f toWrite) {

        final JsonObject object = new JsonObject();
        object.addProperty("x", toWrite.x());
        object.addProperty("y", toWrite.y());
        object.addProperty("z", toWrite.z());
        return object;
    }

    @Override
    public Vector3f fromByteBuf(FriendlyByteBuf buffer) {

        return new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void toByteBuf(FriendlyByteBuf buffer, Vector3f toWrite) {

        buffer.writeFloat(toWrite.x());
        buffer.writeFloat(toWrite.y());
        buffer.writeFloat(toWrite.z());
    }

    @Override
    public Tag toNBT(Vector3f toWrite) {

        final CompoundTag tag = new CompoundTag();
        tag.putFloat("x", toWrite.x());
        tag.putFloat("y", toWrite.y());
        tag.putFloat("z", toWrite.z());
        return tag;
    }

    @Override
    public Vector3f fromNBT(Tag nbt) {

        // If it's a compound explicitly read each axis.
        if (nbt instanceof CompoundTag compound) {

            final float x = compound.getFloat("x");
            final float y = compound.getFloat("y");
            final float z = compound.getFloat("z");
            return new Vector3f(x, y, z);
        }

        // If it's a list of 3 elements read in X,Y,Z order.
        else if (nbt instanceof ListTag list) {

            if (list.size() == 3) {

                return new Vector3f(Serializers.FLOAT.fromNBT(list.get(0)), Serializers.FLOAT.fromNBT(list.get(1)), Serializers.FLOAT.fromNBT(list.get(2)));
            }

            throw new NBTParseException("Invalid number of args in array. Expected 3 but got " + list.size());
        }

        throw new NBTParseException("Vector3f data is not in a readable format.");
    }

}