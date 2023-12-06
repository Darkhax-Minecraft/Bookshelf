package net.darkhax.bookshelf.impl.gametest.tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.darkhax.bookshelf.api.util.EnchantmentHelper;
import net.darkhax.bookshelf.impl.gametest.ITestable;
import net.darkhax.bookshelf.impl.gametest.TestHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class CodecTests<T> implements ITestable {

    protected final String type;
    protected final CodecHelper<T> codecHelper;
    protected final T singleton;
    protected final T[] collection;
    protected final BiPredicate<T, T> equality;

    public CodecTests(String type, CodecHelper<T> serializer, T... collection) {

        this(type, serializer, Objects::equals, collection[0], collection);
    }

    public CodecTests(String type, CodecHelper<T> codecHelper, BiPredicate<T, T> equality, T... collection) {

        this(type, codecHelper, equality, collection[0], collection);
    }

    public CodecTests(String type, CodecHelper<T> codecHelper, BiPredicate<T, T> equality, T singleton, T... collection) {

        this.type = type;
        this.codecHelper = codecHelper;
        this.equality = equality;
        this.singleton = singleton;
        this.collection = collection;
    }

    @GameTest
    public void test_singleton(GameTestHelper helper) {

        final JsonElement toJson = toJson(helper, codecHelper.get(), singleton);
        final T fromJson = fromJson(helper, codecHelper.get(), toJson);

        if (!this.equality.test(this.singleton, fromJson)) {
            helper.fail("Deserialized singleton does not match input! a=" + this.singleton + " b=" + fromJson);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_array(GameTestHelper helper) {

        final JsonElement toJson = toJson(helper, codecHelper.getArray(), this.collection);
        final T[] fromJson = fromJson(helper, codecHelper.getArray(), toJson);
        TestHelper.assertEqual(helper, this.collection, fromJson, this.equality);
    }

    @GameTest
    public void test_list(GameTestHelper helper) {

        final List<T> inputList = Arrays.asList(this.collection);
        final JsonElement toJson = toJson(helper, codecHelper.getList(), inputList);
        final List<T> fromJson = fromJson(helper, codecHelper.getList(), toJson);
        TestHelper.assertEqual(helper, inputList, fromJson, this.equality);
    }

    @GameTest
    public void test_set(GameTestHelper helper) {

        final Set<T> inputSet = Arrays.stream(this.collection).collect(Collectors.toSet());
        final JsonElement toJson = toJson(helper, codecHelper.getSet(), inputSet);
        final Set<T> fromJson = fromJson(helper, codecHelper.getSet(), toJson);
        TestHelper.assertEqual(helper, inputSet, fromJson, this.equality);
    }

    @GameTest
    public void test_optional(GameTestHelper helper) {

        final Optional<T> input = Optional.of(this.singleton);
        final JsonElement toJson = toJson(helper, codecHelper.getOptional("value"), input);
        final Optional<T> fromJson = fromJson(helper, codecHelper.getOptional("value"), toJson);
        TestHelper.assertEqual(helper, input, fromJson, this.equality);
    }

    @GameTest
    public void test_optional_empty(GameTestHelper helper) {

        final Optional<T> input = Optional.empty();
        final JsonElement toJson = toJson(helper, codecHelper.getOptional("value"), input);
        final Optional<T> fromJson = fromJson(helper, codecHelper.getOptional("value"), toJson);
        TestHelper.assertEqual(helper, input, fromJson, this.equality);
    }

    @GameTest
    public void test_nullable(GameTestHelper helper) {

        final JsonElement toJson = toJson(helper, codecHelper.getNullable("value"), this.singleton);
        final T fromJson = fromJson(helper, codecHelper.getNullable("value"), toJson);

        if (!this.equality.test(this.singleton, fromJson)) {
            helper.fail("Deserialized singleton does not match input! a=" + this.singleton + " b=" + fromJson);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_nullable_null(GameTestHelper helper) {

        final JsonElement toJson = toJson(helper, codecHelper.getNullable("value"), null);
        final T fromJson = fromJson(helper, codecHelper.getNullable("value"), toJson);

        if (!this.equality.test(null, fromJson)) {
            helper.fail("Deserialized singleton does not match input! a=" + null + " b=" + fromJson);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_weighted(GameTestHelper helper) {

        final WeightedEntry.Wrapper<T> input = WeightedEntry.wrap(this.singleton, 22);
        final JsonElement toJson = toJson(helper, codecHelper.getWeighted(), input);
        final WeightedEntry.Wrapper<T> fromJson = fromJson(helper, codecHelper.getWeighted(), toJson);
        TestHelper.assertEqual(helper, input, fromJson, this.equality);
    }

    @GameTest
    public void test_weighted_list(GameTestHelper helper) {

        final SimpleWeightedRandomList.Builder<T> builder = new SimpleWeightedRandomList.Builder<>();

        for (int i = 0; i < this.collection.length; i++) {

            builder.add(this.collection[i], i * 4);
        }

        final SimpleWeightedRandomList<T> input = builder.build();
        final JsonElement toJson = toJson(helper, codecHelper.getWeightedList(), input);
        final SimpleWeightedRandomList<T> fromJson = fromJson(helper, codecHelper.getWeightedList(), toJson);
        TestHelper.assertEqual(helper, input, fromJson, this.equality);
    }

    @Override
    public String getDefaultBatch() {

        return "bookshelf.codecs." + this.type;
    }

    record ValueHolder<VT>(VT value) {

    }

    public static <DT> JsonElement toJson(GameTestHelper helper, MapCodec<DT> codec, DT data) {

        return codec.encoder().encodeStart(JsonOps.INSTANCE, data).getOrThrow(false, error -> helper.fail("Failed to serialize: " + error));
    }

    public static <DT> DT fromJson(GameTestHelper helper, MapCodec<DT> codec, JsonElement data) {

        return codec.decoder().decode(JsonOps.INSTANCE, data).getOrThrow(false, error -> helper.fail("Failed to deserialize: " + error)).getFirst();
    }

    public static <DT> JsonElement toJson(GameTestHelper helper, Codec<DT> codec, DT data) {

        return codec.encodeStart(JsonOps.INSTANCE, data).getOrThrow(false, error -> helper.fail("Failed to serialize: " + error));
    }

    public static <DT> DT fromJson(GameTestHelper helper, Codec<DT> codec, JsonElement json) {

        return codec.decode(JsonOps.INSTANCE, json).getOrThrow(false, error -> helper.fail("Failed to deserialize: " + error)).getFirst();
    }
}