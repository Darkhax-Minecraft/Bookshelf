package net.darkhax.bookshelf.impl.gametest;

import com.google.common.collect.Iterables;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.darkhax.bookshelf.api.serialization.ISerializer;
import net.darkhax.bookshelf.mixin.util.random.AccessorWeightedRandomList;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

public class TestSerialization<T> implements ITestable {

    /**
     * A short string that denotes the type of data serialized by the serializer being tested. This is purely to help
     * display information about the tests.
     */
    private final String type;

    /**
     * The serializer to test.
     */
    private final ISerializer<T> serializer;

    /**
     * A singleton instance to test serialization with. This is considered immutable.
     */
    private final T singleton;

    /**
     * A collection of instances to test collection based serialization with. This is considered immutable.
     */
    private final T[] collection;

    private final BiPredicate<T, T> equality;

    public TestSerialization(String type, ISerializer<T> serializer, T... collection) {

        this(type, serializer, Objects::equals, collection[0], collection);
    }

    public TestSerialization(String type, ISerializer<T> serializer, BiPredicate<T, T> equality, T... collection) {

        this(type, serializer, equality, collection[0], collection);
    }

    public TestSerialization(String type, ISerializer<T> serializer, BiPredicate<T, T> equality, T singleton, T... collection) {

        this.type = type;
        this.serializer = serializer;
        this.equality = equality;
        this.singleton = singleton;
        this.collection = collection;
    }

    @GameTest
    public void testJsonSingleton(GameTestHelper helper) {

        final JsonElement toJson = this.serializer.toJSON(this.singleton);
        final T fromJson = this.serializer.fromJSON(toJson);

        if (!this.equality.test(this.singleton, fromJson)) {

            helper.fail("Singleton from" + this.type + " JSON does not match! a= " + this.singleton + " b= " + fromJson);
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testBytebufSingleton(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBuf(buffer, this.singleton);
        final T fromBuffer = this.serializer.fromByteBuf(buffer);

        if (!this.equality.test(this.singleton, fromBuffer)) {

            helper.fail("Singleton from" + this.type + " ByteBuf does not match! a= " + this.singleton + " b= " + fromBuffer);
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testNbtSingleton(GameTestHelper helper) {

        final Tag toNBT = this.serializer.toNBT(this.singleton);
        final T fromNBT = this.serializer.fromNBT(toNBT);

        if (!this.equality.test(this.singleton, fromNBT)) {

            helper.fail("Singleton from" + this.type + " NBT does not match! a= " + this.singleton + " b= " + fromNBT);
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testJsonWeightedSingleton(GameTestHelper helper) {

        final WeightedEntry.Wrapper<T> weightedSingleton = WeightedEntry.wrap(this.singleton, 14);
        final JsonElement toJson = this.serializer.toJSONWeighted(weightedSingleton);
        final WeightedEntry.Wrapper<T> fromJson = this.serializer.fromJSONWeighted(toJson);

        if (!weightedSingleton.getWeight().equals(fromJson.getWeight())) {

            helper.fail("Weight for JSON does not match! a= " + weightedSingleton.getWeight() + " b= " + fromJson.getWeight());
        }

        if (!this.equality.test(this.singleton, fromJson.getData())) {

            helper.fail("Weighted Singleton from" + this.type + " JSON does not match! a= " + this.singleton + " b= " + fromJson.getData());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testBytebufWeightedSingleton(GameTestHelper helper) {

        final WeightedEntry.Wrapper<T> weightedSingleton = WeightedEntry.wrap(this.singleton, 14);
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufWeighted(buffer, weightedSingleton);
        final WeightedEntry.Wrapper<T> fromBuffer = this.serializer.fromByteBufWeighted(buffer);

        if (!weightedSingleton.getWeight().equals(fromBuffer.getWeight())) {

            helper.fail("Weight for ByteBuf does not match! a= " + weightedSingleton.getWeight() + " b= " + fromBuffer.getWeight());
        }

        if (!this.equality.test(this.singleton, fromBuffer.getData())) {

            helper.fail("Weighted Singleton from" + this.type + " ByteBuf does not match! a= " + this.singleton + " b= " + fromBuffer.getData());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testJsonOptionalSingleton(GameTestHelper helper) {

        final Optional<T> original = Optional.ofNullable(this.singleton);
        final JsonElement toJson = this.serializer.toJSONOptional(original);
        final Optional<T> fromJson = this.serializer.fromJSONOptional(toJson);

        if (original.isPresent() && fromJson.isPresent() && !this.equality.test(original.get(), fromJson.get())) {

            helper.fail("Optional Singleton from" + this.type + " JSON does not match! a= " + original.get() + " b= " + fromJson.get());
        }

        if (original.isPresent() != fromJson.isPresent()) {

            helper.fail("Optional Singleton from " + this.type + " JSON availability mismatch. a= " + original.isPresent() + " b= " + fromJson.isPresent());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testBytebufOptionalSingleton(GameTestHelper helper) {

        final Optional<T> original = Optional.ofNullable(this.singleton);
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufOptional(buffer, original);
        final Optional<T> fromByteBuf = this.serializer.fromByteBufOptional(buffer);

        if (original.isPresent() && fromByteBuf.isPresent() && !this.equality.test(original.get(), fromByteBuf.get())) {

            helper.fail("Optional Singleton from" + this.type + " ByteBuf does not match! a= " + original.get() + " b= " + fromByteBuf.get());
        }

        if (original.isPresent() != fromByteBuf.isPresent()) {

            helper.fail("Optional Singleton from " + this.type + " ByteBuf availability mismatch. a= " + original.isPresent() + " b= " + fromByteBuf.isPresent());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testJsonEmptyOptionalSingleton(GameTestHelper helper) {

        final Optional<T> original = Optional.empty();
        final JsonElement toJson = this.serializer.toJSONOptional(original);
        final Optional<T> fromJson = this.serializer.fromJSONOptional(toJson);

        if (original.isPresent() || fromJson.isPresent()) {

            helper.fail("Empty Optional Singleton from" + this.type + " JSON was not empty! a= " + original.isPresent() + " b= " + fromJson.isPresent());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testBytebufEmptyOptionalSingleton(GameTestHelper helper) {

        final Optional<T> original = Optional.empty();
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufOptional(buffer, original);
        final Optional<T> fromByteBuf = this.serializer.fromByteBufOptional(buffer);

        if (original.isPresent() || fromByteBuf.isPresent()) {

            helper.fail("Empty Optional Singleton from" + this.type + " JSON was not empty! a= " + original.isPresent() + " b= " + fromByteBuf.isPresent());
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testJsonList(GameTestHelper helper) {

        final List<T> list = List.of(this.collection);
        final JsonElement written = this.serializer.toJSONList(list);
        final List<T> read = this.serializer.fromJSONList(written);

        assertIterableEqual(helper, list, read);
    }

    @GameTest
    public void testJsonListEmpty(GameTestHelper helper) {

        final List<T> list = new ArrayList<>();
        final JsonObject testObj = new JsonObject();

        this.serializer.toJSONList(testObj, "test", list);

        if (testObj.has("test")) {

            helper.fail("Empty list should not be serialized.");
        }

        helper.succeed();
    }

    @GameTest
    public void testJsonListNull(GameTestHelper helper) {

        final JsonObject testObj = new JsonObject();

        this.serializer.toJSONList(testObj, "test", null);

        if (testObj.has("test")) {

            helper.fail("Empty list should not be serialized.");
        }

        helper.succeed();
    }

    @GameTest
    public void testBytebufList(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final List<T> list = List.of(this.collection);
        this.serializer.toByteBufList(buffer, list);
        final List<T> read = this.serializer.fromByteBufList(buffer);

        assertIterableEqual(helper, list, read);
    }

    @GameTest
    public void testNbtList(GameTestHelper helper) {

        final List<T> list = List.of(this.collection);
        final CompoundTag tag = new CompoundTag();
        this.serializer.toNBTList(tag, "test_list", list);

        if (!tag.contains("test_list")) {

            helper.fail("List was not written to tag.");
        }

        if (tag.get("test_list") instanceof ListTag listTag && listTag.size() != list.size()) {

            helper.fail("List size does not match on write. Has " + listTag.size() + " expected " + list.size());
        }

        final List<T> read = this.serializer.fromNBTList(tag, "test_list");

        assertIterableEqual(helper, list, read);
    }

    @GameTest
    public void testJsonSet(GameTestHelper helper) {

        final Set<T> set = this.createSet();
        final JsonElement written = this.serializer.toJSONSet(set);
        final Set<T> read = this.serializer.fromJSONSet(written);

        assertIterableEqual(helper, set, read);
    }

    @GameTest
    public void testBytebufSet(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final Set<T> set = this.createSet();
        this.serializer.writeByteBufSet(buffer, set);
        final Set<T> read = this.serializer.readByteBufSet(buffer);

        assertIterableEqual(helper, set, read);
    }

    @GameTest
    public void testJsonWeightedList(GameTestHelper helper) {

        final SimpleWeightedRandomList<T> original = this.createWeightedList();
        final JsonElement written = this.serializer.toJSONWeightedList(original);
        final SimpleWeightedRandomList<T> read = this.serializer.fromJSONWeightedList(written);

        assertWeightedListEqual(helper, original, read);
    }

    @GameTest
    public void testBytebufWeightedList(GameTestHelper helper) {

        final SimpleWeightedRandomList<T> original = this.createWeightedList();
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufWeightedList(buffer, original);
        final SimpleWeightedRandomList<T> read = this.serializer.fromByteBufWeightedList(buffer);

        assertWeightedListEqual(helper, original, read);
    }

    @GameTest
    public void testJsonNullable(GameTestHelper helper) {

        final JsonElement nullJson = this.serializer.toJSONNullable(null);

        if (nullJson != null) {

            helper.fail("Expected JSON to be null.");
        }

        final T nullValue = this.serializer.fromJSONNullable(nullJson);

        if (nullValue != null) {

            helper.fail("Expected value to be null.");
        }

        final JsonElement json = this.serializer.toJSONNullable(this.singleton);

        if (json == null) {

            helper.fail("Expected JSON to not be null.");
        }

        final T value = this.serializer.fromJSONNullable(json);

        if (value == null) {

            helper.fail("Expected value to not be null.");
        }

        if (!this.equality.test(this.singleton, value)) {

            helper.fail("Value written and read do not match.");
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testBytebufNullable(GameTestHelper helper) {

        final FriendlyByteBuf nullBuffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufNullable(nullBuffer, null);
        final T nullValue = this.serializer.fromByteBufNullable(nullBuffer);

        if (nullValue != null) {

            helper.fail("Expected value to be null.");
        }

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.serializer.toByteBufNullable(buffer, this.singleton);
        final T value = this.serializer.fromByteBufNullable(buffer);

        if (value == null) {

            helper.fail("Expected value to not be null.");
        }

        if (!this.equality.test(this.singleton, value)) {

            helper.fail("Value written and read do not match.");
        }

        else {

            helper.succeed();
        }
    }

    private Set<T> createSet() {

        final Set<T> values = new LinkedHashSet<>();
        Collections.addAll(values, this.collection);
        return values;
    }

    private SimpleWeightedRandomList<T> createWeightedList() {

        final SimpleWeightedRandomList.Builder<T> weightedList = SimpleWeightedRandomList.builder();

        for (T value : this.collection) {

            weightedList.add(value, 25);
        }

        return weightedList.build();
    }

    private void assertWeightedListEqual(GameTestHelper helper, SimpleWeightedRandomList<T> original, SimpleWeightedRandomList<T> result) {

        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessOriginal = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) original);
        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessResult = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) result);

        if (accessOriginal.bookshelf$getEntries().size() != accessResult.bookshelf$getEntries().size()) {

            helper.fail("List of type " + this.type + " has incorrect size. Original=" + accessOriginal.bookshelf$getEntries().size() + " Result=" + accessResult.bookshelf$getEntries().size());
        }

        else {

            for (int index = 0; index < accessOriginal.bookshelf$getEntries().size(); index++) {

                final WeightedEntry.Wrapper<T> a = accessOriginal.bookshelf$getEntries().get(index);
                final WeightedEntry.Wrapper<T> b = accessResult.bookshelf$getEntries().get(index);

                if (a.getWeight().asInt() != b.getWeight().asInt()) {

                    helper.fail("Weighted list of " + this.type + " do not match! index=" + index + " a=" + a.getWeight().asInt() + " b=" + b.getWeight().asInt());
                    return;
                }

                if (!this.equality.test(a.getData(), b.getData())) {

                    helper.fail("Weighted list of " + this.type + " do not match! index=" + index + " a=" + a.getData() + " b=" + b.getData());
                    return;
                }
            }

            helper.succeed();
        }
    }

    private void assertIterableEqual(GameTestHelper helper, Iterable<T> original, Iterable<T> result) {

        final int originalSize = Iterables.size(original);
        final int resultSize = Iterables.size(result);

        if (originalSize != resultSize) {

            helper.fail("List of type " + this.type + " has incorrect size. Original=" + originalSize + " Result=" + resultSize);
        }

        else {

            for (int index = 0; index < originalSize; index++) {

                final T a = Iterables.get(original, index);
                final T b = Iterables.get(result, index);

                if (!this.equality.test(a, b)) {

                    helper.fail("List of " + this.type + " do not match! index=" + index + " a=" + a + " b=" + b);
                    return;
                }
            }

            helper.succeed();
        }
    }

    @Override
    public String getDefaultBatch() {

        return "bookshelf_serialization_" + this.type;
    }
}