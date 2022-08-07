package net.darkhax.bookshelf.api.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.api.util.JSONHelper;
import net.darkhax.bookshelf.mixin.util.random.AccessorWeightedRandomList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Provides a structure for serializing common data types into the various data exchange formats used in the game. These
 * currently support JSON, NBT, and byte buffers (networking).
 *
 * @param <T> The data type that is handled by the serializer.
 */
public interface ISerializer<T> {

    /**
     * Reads the value from a specific JSON element.
     *
     * @param json The JSON element to read data from.
     * @return The value that was read.
     */
    T fromJSON(JsonElement json);

    /**
     * Reads a value from a member on a parent JSON object.
     *
     * @param json       The parent JSON object to read from.
     * @param memberName The name of the child element to read from.
     * @return The value that was read.
     */
    default T fromJSON(JsonObject json, String memberName) {

        return this.fromJSON(json.get(memberName));
    }

    /**
     * Reads a value from a member on a parent JSON object.
     *
     * @param json       The parent JSON object to read from.
     * @param memberName The name of the child element to read from.
     * @param fallback   A fallback value used when the child element does not exist.
     * @return The value that was read or the fallback value.
     */
    default T fromJSON(JsonObject json, String memberName, T fallback) {

        return json.has(memberName) ? this.fromJSON(json, memberName) : fallback;
    }

    /**
     * Reads a value from a member on a parent JSON object.
     *
     * @param json       The parent JSON object to read from.
     * @param memberName The name of the child element to read from.
     * @param fallback   A fallback value used when the child element does not exist.
     * @return The value that was read or the fallback value.
     */
    default T fromJSON(JsonObject json, String memberName, Supplier<T> fallback) {

        return json.has(memberName) ? this.fromJSON(json, memberName) : fallback.get();
    }

    default void toJSON(JsonObject json, String name, T toWrite) {

        json.add(name, this.toJSON(toWrite));
    }

    /**
     * Writes a value to a JSON element.
     *
     * @param toWrite The object to serialize into JSON data.
     * @return The serialized JSON element.
     */
    JsonElement toJSON(T toWrite);

    /**
     * Reads a value from a byte buffer. This is commonly used for networking.
     *
     * @param buffer The byte buffer to read from.
     * @return The value that was read.
     */
    T fromByteBuf(FriendlyByteBuf buffer);

    /**
     * Writes a value to a byte buffer. This is commonly used for networking.
     *
     * @param buffer  The byte buffer to write to.
     * @param toWrite The value to write.
     */
    void toByteBuf(FriendlyByteBuf buffer, T toWrite);

    /**
     * Writes a value to a named binary tag (NBT).
     *
     * @param toWrite The value to write.
     * @return The serialized NBT data.
     */
    Tag toNBT(T toWrite);

    /**
     * Reads a value from a named binary tag (NBT).
     *
     * @param nbt The tag to read data from.
     * @return The value that was read from the data.
     */
    T fromNBT(Tag nbt);

    default T fromNBT(CompoundTag tag, String name) {

        if (tag.contains(name)) {

            return this.fromNBT(tag.get(name));
        }

        throw new NBTParseException("Required tag " + name + " was not present.");
    }

    default T fromNBT(@Nullable CompoundTag tag, String name, T fallback) {

        return tag != null && tag.contains(name) ? this.fromNBT(tag.get(name)) : fallback;
    }

    default void toNBT(CompoundTag tag, String name, T toWrite) {

        tag.put(name, this.toNBT(toWrite));
    }

    default void toNBTList(@Nullable CompoundTag tag, String name, @Nullable List<T> toWrite) {

        if (tag != null && toWrite != null && !toWrite.isEmpty()) {

            tag.put(name, this.toNBTList(toWrite));
        }
    }

    default ListTag toNBTList(List<T> toWrite) {

        final ListTag listTag = new ListTag();

        for (T value : toWrite) {

            listTag.add(this.toNBT(value));
        }

        return listTag;
    }

    default List<T> fromNBTList(CompoundTag tag, String name) {

        if (tag.contains(name)) {

            return fromNBTList(tag.get(name));
        }

        throw new NBTParseException("Expected list tag named " + name);
    }

    default List<T> fromNBTList(Tag toRead) {

        final List<T> list = new LinkedList<>();

        if (toRead instanceof ListTag listTag) {

            for (Tag tag : listTag) {

                list.add(this.fromNBT(tag));
            }
        }

        else {

            list.add(this.fromNBT(toRead));
        }

        return list;
    }

    /**
     * Reads a list of values from JSON. When given a JSON array each entry is added to the list. When given a JSON
     * object it will be treated as a list of one element.
     *
     * @param json The JSON element to read from.
     * @return A list of values read from the JSON.
     */
    default List<T> fromJSONList(JsonElement json) {

        final List<T> list = new ArrayList<>();

        if (json.isJsonArray()) {

            for (final JsonElement element : json.getAsJsonArray()) {

                list.add(this.fromJSON(element));
            }
        }
        else {
            list.add(this.fromJSON(json));
        }

        return list;
    }

    /**
     * Reads a list of values from a child element on a JSON object. If the child element is an array each entry will be
     * added to the list. If the child element is an object it will be treated as a list of one.
     *
     * @param json       The parent JSON element.
     * @param memberName The member of the element to read from.
     * @return The list of values read from the JSON.
     */
    default List<T> fromJSONList(JsonObject json, String memberName) {

        if (json.has(memberName)) {

            return this.fromJSONList(json.get(memberName));
        }

        throw new JsonParseException("Expected member " + memberName + " was not found.");
    }

    /**
     * Reads a list of values from a child element on a JSON object. If the child element is an array each entry will be
     * added to the list. If the child element is an object it will be treated as a list of one.
     *
     * @param json       The parent JSON element.
     * @param memberName The member of the element to read from.
     * @param fallback   A list of fallback values to use when the child member is not present.
     * @return The list of values read from the JSON.
     */
    default List<T> fromJSONList(JsonObject json, String memberName, List<T> fallback) {

        return json.has(memberName) ? this.fromJSONList(json, memberName) : fallback;
    }

    /**
     * Reads a list of values from a child element on a JSON object. If the child element is an array each entry will be
     * added to the list. If the child element is an object it will be treated as a list of one.
     *
     * @param json       The parent JSON element.
     * @param memberName The member of the element to read from.
     * @param fallback   A list of fallback values to use when the child member is not present.
     * @return The list of values read from the JSON.
     */
    default List<T> fromJSONList(JsonObject json, String memberName, Supplier<List<T>> fallback) {

        return json.has(memberName) ? this.fromJSONList(json, memberName) : fallback.get();
    }

    /**
     * Writes a list of values to a JSON element.
     *
     * @param toWrite The values to write.
     * @return The serialized JSON element.
     */
    default JsonElement toJSONList(List<T> toWrite) {

        final JsonArray json = new JsonArray();
        toWrite.forEach(t -> json.add(this.toJSON(t)));
        return json;
    }

    /**
     * Writes a list of values to a JSON object, if the list is null or empty it will be ignored.
     *
     * @param json       The JSON object to add the list to.
     * @param memberName The name to assign the list member.
     * @param toWrite    The list to write.
     */
    default void toJSONList(JsonObject json, String memberName, @Nullable List<T> toWrite) {

        if (toWrite != null && !toWrite.isEmpty()) {

            json.add(memberName, this.toJSONList(toWrite));
        }
    }

    /**
     * Reads a list of values from a byte buffer.
     *
     * @param buffer The buffer to read data from.
     * @return The list of values read from the buffer.
     */
    default List<T> fromByteBufList(FriendlyByteBuf buffer) {

        final int size = buffer.readInt();
        final List<T> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {

            list.add(this.fromByteBuf(buffer));
        }

        return list;
    }

    /**
     * Writes a list of values to a byte buffer.
     *
     * @param buffer  The buffer to write to.
     * @param toWrite The value to write.
     */
    default void toByteBufList(FriendlyByteBuf buffer, List<T> toWrite) {

        buffer.writeInt(toWrite.size());
        toWrite.forEach(t -> this.toByteBuf(buffer, t));
    }

    /**
     * Reads a set of values from a JSON element. A JSON array will have each element added to the set. A JSON object
     * will be treated as a set of one element.
     *
     * @param json The JSON element to read from.
     * @return The set of values that were read.
     */
    default Set<T> fromJSONSet(JsonElement json) {

        final Set<T> set = new LinkedHashSet<>();

        if (json.isJsonArray()) {

            for (final JsonElement element : json.getAsJsonArray()) {

                set.add(this.fromJSON(element));
            }
        }
        else {
            set.add(this.fromJSON(json));
        }

        return set;
    }

    /**
     * Reads a set of values from a child member from the parent JSON element.
     *
     * @param json       The parent JSON element.
     * @param memberName The name of the child member.
     * @return The set of values that were read.
     */
    default Set<T> fromJSONSet(JsonObject json, String memberName) {

        if (json.has(memberName)) {

            return this.fromJSONSet(json.get(memberName));
        }

        throw new JsonParseException("Expected member " + memberName + " was not found.");
    }

    /**
     * Reads a set of values from a child member from the parent JSON element. If the member does not exist the fallback
     * set will be used.
     *
     * @param json       The parent JSON element.
     * @param memberName The name of the child member.
     * @param fallback   The fallback set to use when the member is not present.
     * @return The set of values that were read.
     */
    default Set<T> fromJSONSet(JsonObject json, String memberName, Set<T> fallback) {

        return json.has(memberName) ? this.fromJSONSet(json, memberName) : fallback;
    }

    /**
     * Reads a set of values from a child member from the parent JSON element. If the member does not exist the fallback
     * set will be used.
     *
     * @param json       The parent JSON element.
     * @param memberName The name of the child member.
     * @param fallback   The fallback set to use when the member is not present.
     * @return The set of values that were read.
     */
    default Set<T> fromJSONSet(JsonObject json, String memberName, Supplier<Set<T>> fallback) {

        return json.has(memberName) ? this.fromJSONSet(json, memberName) : fallback.get();
    }

    /**
     * Writes a set of values to a JSON element.
     *
     * @param toWrite The set of values to write.
     * @return The JSON value that was written.
     */
    default JsonElement toJSONSet(Set<T> toWrite) {

        final JsonArray json = new JsonArray();
        toWrite.forEach(t -> json.add(this.toJSON(t)));
        return json;
    }

    /**
     * Reads a set of values from a byte buffer.
     *
     * @param buffer The byte buffer to read from.
     * @return The set of values that were read from the buffer.
     */
    default Set<T> readByteBufSet(FriendlyByteBuf buffer) {

        final int size = buffer.readInt();
        final Set<T> set = new LinkedHashSet<>(size);

        for (int i = 0; i < size; i++) {

            set.add(this.fromByteBuf(buffer));
        }

        return set;
    }

    /**
     * Writes a set of values from a byte buffer.
     *
     * @param buffer  The byte buffer to write to.
     * @param toWrite The set of values to write.
     */
    default void writeByteBufSet(FriendlyByteBuf buffer, Set<T> toWrite) {

        buffer.writeInt(toWrite.size());
        toWrite.forEach(t -> this.toByteBuf(buffer, t));
    }

    /**
     * Read an optional value from a child JSON element. If the child JSON member does not exist an empty optional will
     * be used.
     *
     * @param json       The parent JSON element to read from.
     * @param memberName The name of the child member to read.
     * @return An optional containing the value that was read.
     */
    default Optional<T> fromJSONOptional(JsonObject json, String memberName) {

        return this.fromJSONOptional(json.get(memberName));
    }

    /**
     * Reads an optional value from a JSON element. If the element is null an empty optional will be returned.
     *
     * @param json The JSON to read data from.
     * @return An optional containing the value that was read.
     */
    default Optional<T> fromJSONOptional(@Nullable JsonElement json) {

        return json != null ? Optional.ofNullable(this.fromJSON(json)) : Optional.empty();
    }

    /**
     * Writes an optional value to a JSON element. If the value is not present a null value will be returned.
     *
     * @param value The optional value to write.
     * @return The written JSON element. If the optional value was not present this will be null.
     */
    @Nullable
    default JsonElement toJSONOptional(@Nullable T value) {

        return this.toJSONOptional(Optional.ofNullable(value));
    }

    /**
     * Writes an optional value to a JSON element. If the value is not present a null value will be returned.
     *
     * @param value The optional value to write.
     * @return The written JSON element. If the optional value was not present this will be null.
     */
    @Nullable
    default JsonElement toJSONOptional(Optional<T> value) {

        return value.map(this::toJSON).orElse(null);
    }

    /**
     * Write an optional value to a JSON object.
     *
     * @param json       The JSON object to write to.
     * @param memberName The name of the member to write the value to.
     * @param value      The value to write.
     */
    default void toJSONOptional(JsonObject json, String memberName, Optional<T> value) {

        value.ifPresent(v -> json.add(memberName, this.toJSON(v)));
    }

    /**
     * Read an optional value from a byte buffer.
     *
     * @param buffer The buffer to read from.
     * @return An optional containing the value that was read.
     */
    default Optional<T> fromByteBufOptional(FriendlyByteBuf buffer) {

        return buffer.readBoolean() ? Optional.of(this.fromByteBuf(buffer)) : Optional.empty();
    }

    /**
     * Writes an optional value to the byte buffer.
     *
     * @param buffer   The buffer to write to.
     * @param optional The optional value to write.
     */
    default void toByteBufOptional(FriendlyByteBuf buffer, Optional<T> optional) {

        final boolean isPresent = optional.isPresent();
        buffer.writeBoolean(isPresent);

        if (isPresent) {
            this.toByteBuf(buffer, optional.get());
        }
    }

    /**
     * Writes the value to a string representation of a JSON element.
     *
     * @param toWrite The value to write.
     * @return A string representation of the element when written to JSON.
     */
    default String toJSONString(T toWrite) {

        return this.toJSON(toWrite).toString();
    }

    /**
     * Reads the value from a JSON string.
     *
     * @param jsonString A string representation of the JSON element to read data from.
     * @return The value read from the JSON data.
     */
    default T fromJSONString(String jsonString) {

        return this.fromJSON(JSONHelper.getAsElement(jsonString));
    }

    /**
     * Reads a wrapped weighted entry from a JSON element. The weight is taken from a JSON Object property named
     * "weight". If the JSON is not an Object or the property does not exist a weight of 0 will be used.
     *
     * @param element The JSON element to read from.
     * @return The weighted wrapper.
     */
    default WeightedEntry.Wrapper<T> fromJSONWeighted(JsonElement element) {

        if (element instanceof JsonObject obj) {

            final T value = this.fromJSON(obj, "value");
            final int weight = Serializers.INT.fromJSON(obj, "weight", 0);
            return WeightedEntry.wrap(value, weight);
        }

        return WeightedEntry.wrap(this.fromJSON(element), 0);
    }

    /**
     * Writes a weighted entry to a JSON element. The value and weight are stored as separate properties.
     *
     * @param weightedEntry The weighted entry to write.
     * @return The serialized JSON.
     */
    default JsonElement toJSONWeighted(WeightedEntry.Wrapper<T> weightedEntry) {

        final JsonObject obj = new JsonObject();
        obj.add("value", this.toJSON(weightedEntry.getData()));
        obj.addProperty("weight", weightedEntry.getWeight().asInt());
        return obj;
    }

    /**
     * Writes a weighted wrapped value to a byte buffer.
     *
     * @param buffer  The buffer to write to.
     * @param toWrite The data to write.
     */
    default void toByteBufWeighted(FriendlyByteBuf buffer, WeightedEntry.Wrapper<T> toWrite) {

        this.toByteBuf(buffer, toWrite.getData());
        Serializers.INT.toByteBuf(buffer, toWrite.getWeight().asInt());
    }

    /**
     * Reads a weighted wrapped value from a byte buffer.
     *
     * @param buffer The buffer to read from.
     * @return The value that was read from the buffer.
     */
    default WeightedEntry.Wrapper<T> fromByteBufWeighted(FriendlyByteBuf buffer) {

        final T value = this.fromByteBuf(buffer);
        final int weight = Serializers.INT.fromByteBuf(buffer);
        return WeightedEntry.wrap(value, weight);
    }

    /**
     * Reads a list of weighted wrapped values from the json.
     *
     * @param json The JSON data to read from.
     * @return A list of weighted wrapped values.
     */
    default SimpleWeightedRandomList<T> fromJSONWeightedList(JsonElement json) {

        final SimpleWeightedRandomList.Builder<T> list = SimpleWeightedRandomList.builder();

        if (json instanceof JsonArray array) {

            for (JsonElement element : array) {

                final WeightedEntry.Wrapper<T> wrapped = this.fromJSONWeighted(element);
                list.add(wrapped.getData(), wrapped.getWeight().asInt());
            }
        }

        else {

            final WeightedEntry.Wrapper<T> wrapped = this.fromJSONWeighted(json);
            list.add(wrapped.getData(), wrapped.getWeight().asInt());
        }

        return list.build();
    }

    /**
     * Writes a list of weighted wrapped entries to JSON.
     *
     * @param list The list to write.
     * @return The JSON output.
     */
    default JsonElement toJSONWeightedList(SimpleWeightedRandomList<T> list) {

        final JsonArray array = new JsonArray();
        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessor = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) list);

        for (WeightedEntry.Wrapper<T> entry : accessor.bookshelf$getEntries()) {

            array.add(this.toJSONWeighted(entry));
        }

        return array;
    }

    /**
     * Reads a list of weighted wrapped entries from a byte buffer.
     *
     * @param buffer The buffer to read from.
     * @return The list of weighted wrapped entries.
     */
    default SimpleWeightedRandomList<T> fromByteBufWeightedList(FriendlyByteBuf buffer) {

        final SimpleWeightedRandomList.Builder<T> list = SimpleWeightedRandomList.builder();
        final int size = buffer.readInt();

        for (int i = 0; i < size; i++) {

            final WeightedEntry.Wrapper<T> wrapped = this.fromByteBufWeighted(buffer);
            list.add(wrapped.getData(), wrapped.getWeight().asInt());
        }

        return list.build();
    }

    /**
     * Writes a list of weighted wrapped entries to a byte buffer.
     *
     * @param buffer The buffer to write to.
     * @param list   The list of entries to write.
     */
    default void toByteBufWeightedList(FriendlyByteBuf buffer, SimpleWeightedRandomList<T> list) {

        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessor = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) list);
        buffer.writeInt(accessor.bookshelf$getEntries().size());

        for (WeightedEntry.Wrapper<T> entry : accessor.bookshelf$getEntries()) {

            this.toByteBufWeighted(buffer, entry);
        }
    }

    // NULLABLE

    @Nullable
    default T fromJSONNullable(JsonObject json, String memberName) {

        return json.has(memberName) ? fromJSONNullable(json.get(memberName)) : null;
    }

    @Nullable
    default T fromJSONNullable(@Nullable JsonElement json) {

        return json != null ? this.fromJSON(json) : null;
    }

    @Nullable
    default JsonElement toJSONNullable(@Nullable T value) {

        return value != null ? this.toJSON(value) : null;
    }

    default void toJSONNullable(@Nullable JsonObject json, String memberName, @Nullable T value) {

        if (json != null && value != null) {

            json.add(memberName, this.toJSON(value));
        }
    }

    @Nullable
    default T fromByteBufNullable(FriendlyByteBuf buffer) {

        return buffer.readBoolean() ? this.fromByteBuf(buffer) : null;
    }

    default void toByteBufNullable(FriendlyByteBuf buffer, @Nullable T value) {

        buffer.writeBoolean(value != null);

        if (value != null) {

            this.toByteBuf(buffer,value);
        }
    }
}