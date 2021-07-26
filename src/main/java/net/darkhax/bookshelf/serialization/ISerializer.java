package net.darkhax.bookshelf.serialization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.NotImplementedException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;

/**
 * Provides a simple layout for serializing data to and from Json and packets.
 *
 * @param <T> The type of object to serialize.
 */
public interface ISerializer<T> {

    T read (JsonElement json);

    default T read (JsonObject json, String memberName) {

        return this.read(json.get(memberName));
    }

    default T read (JsonObject json, String memberName, T fallback) {

        return json.has(memberName) ? this.read(json, memberName) : fallback;
    }

    JsonElement write (T toWrite);

    T read (PacketBuffer buffer);

    void write (PacketBuffer buffer, T toWrite);

    public default INBT writeNBT (T toWrite) {

        throw new NotImplementedException("NBT serialization is not yet implemented for " + this.getClass().getName());
    }

    public default T read (INBT nbt) {

        throw new NotImplementedException("NBT serialization is not yet implemented for " + this.getClass().getName());
    }

    default List<T> readList (JsonElement json) {

        final List<T> list = new ArrayList<>();

        if (json.isJsonArray()) {

            for (final JsonElement element : json.getAsJsonArray()) {

                list.add(this.read(element));
            }
        }

        else {
            list.add(this.read(json));
        }

        return list;
    }

    default List<T> readList (JsonObject json, String memberName) {

        if (json.has(memberName)) {

            return this.readList(json.get(memberName));
        }

        throw new JsonParseException("Expected member " + memberName + " was not found.");
    }

    default List<T> readList (JsonObject json, String memberName, List<T> fallback) {

        return json.has(memberName) ? this.readList(json, memberName) : fallback;
    }

    default JsonElement writeList (List<T> toWrite) {

        final JsonArray json = new JsonArray();
        toWrite.forEach(t -> json.add(this.write(t)));
        return json;
    }

    default List<T> readList (PacketBuffer buffer) {

        final int size = buffer.readInt();
        final List<T> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {

            list.add(this.read(buffer));
        }

        return list;
    }

    default void writeList (PacketBuffer buffer, List<T> toWrite) {

        buffer.writeInt(toWrite.size());
        toWrite.forEach(t -> this.write(buffer, t));
    }

    default Set<T> readSet (JsonElement json) {

        final Set<T> set = new HashSet<>();

        if (json.isJsonArray()) {

            for (final JsonElement element : json.getAsJsonArray()) {

                set.add(this.read(element));
            }
        }

        else {
            set.add(this.read(json));
        }

        return set;
    }

    default Set<T> readSet (JsonObject json, String memberName) {

        if (json.has(memberName)) {

            return this.readSet(json.get(memberName));
        }

        throw new JsonParseException("Expected member " + memberName + " was not found.");
    }

    default Set<T> readSet (JsonObject json, String memberName, Set<T> fallback) {

        return json.has(memberName) ? this.readSet(json, memberName) : fallback;
    }

    default JsonElement writeSet (Set<T> toWrite) {

        final JsonArray json = new JsonArray();
        toWrite.forEach(t -> json.add(this.write(t)));
        return json;
    }

    default Set<T> readSet (PacketBuffer buffer) {

        final int size = buffer.readInt();
        final Set<T> set = new HashSet<>(size);

        for (int i = 0; i < size; i++) {

            set.add(this.read(buffer));
        }

        return set;
    }

    default void writeSet (PacketBuffer buffer, Set<T> toWrite) {

        buffer.writeInt(toWrite.size());
        toWrite.forEach(t -> this.write(buffer, t));
    }

    default Optional<T> readOptional (JsonObject json, String memberName) {

        return json.has(memberName) ? Optional.of(this.read(json.get(memberName))) : Optional.empty();
    }

    default Optional<T> readOptional (PacketBuffer buffer) {

        return buffer.readBoolean() ? Optional.of(this.read(buffer)) : Optional.empty();
    }

    default void writeOptional (PacketBuffer buffer, Optional<T> optional) {

        final boolean isPresent = optional.isPresent();
        buffer.writeBoolean(isPresent);

        if (isPresent) {
            this.write(buffer, optional.get());
        }
    }

    default void writeOptional (JsonObject json, String memberName, Optional<T> optional) {

        if (optional.isPresent()) {

            json.add(memberName, this.write(optional.get()));
        }
    }

    @Nullable
    default JsonElement writeOptional (Optional<T> optional) {

        return optional.isPresent() ? this.write(optional.get()) : null;
    }
}