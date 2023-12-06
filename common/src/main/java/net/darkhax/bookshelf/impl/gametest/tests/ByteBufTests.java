package net.darkhax.bookshelf.impl.gametest.tests;

import io.netty.buffer.Unpooled;
import net.darkhax.bookshelf.api.data.bytebuf.ByteBufHelper;
import net.darkhax.bookshelf.impl.gametest.ITestable;
import net.darkhax.bookshelf.impl.gametest.TestHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ByteBufTests<T> implements ITestable {

    protected final String type;
    protected final ByteBufHelper<T> bufHelper;
    protected final T singleton;
    protected final T[] collection;
    protected final BiPredicate<T, T> equality;

    public ByteBufTests(String type, ByteBufHelper<T> serializer, T... collection) {

        this(type, serializer, Objects::equals, collection[0], collection);
    }

    public ByteBufTests(String type, ByteBufHelper<T> codecHelper, BiPredicate<T, T> equality, T... collection) {

        this(type, codecHelper, equality, collection[0], collection);
    }

    public ByteBufTests(String type, ByteBufHelper<T> codecHelper, BiPredicate<T, T> equality, T singleton, T... collection) {

        this.type = type;
        this.bufHelper = codecHelper;
        this.equality = equality;
        this.singleton = singleton;
        this.collection = collection;
    }

    @GameTest
    public void test_singleton(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.bufHelper.write(buffer, this.singleton);
        final T fromBuffer = this.bufHelper.read(buffer);

        if (!this.equality.test(this.singleton, fromBuffer)) {
            helper.fail("Deserialized singleton does not match input! a=" + this.singleton + " b=" + fromBuffer);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_array(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.bufHelper.writeArray(buffer, this.collection);
        final T[] fromBuffer = this.bufHelper.readArray(buffer);
        TestHelper.assertEqual(helper, this.collection, fromBuffer, this.equality);
    }

    @GameTest
    public void test_list(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final List<T> inputList = Arrays.asList(this.collection);
        this.bufHelper.writeList(buffer, inputList);
        final List<T> fromBuffer = this.bufHelper.readList(buffer);

        TestHelper.assertEqual(helper, inputList, fromBuffer, this.equality);
    }

    @GameTest
    public void test_set(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final Set<T> inputSet = Arrays.stream(this.collection).collect(Collectors.toSet());
        this.bufHelper.writeSet(buffer, inputSet);
        final Set<T> fromBuffer = this.bufHelper.readSet(buffer);
        TestHelper.assertEqual(helper, inputSet, fromBuffer, this.equality);
    }

    @GameTest
    public void test_optional(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final Optional<T> input = Optional.of(this.singleton);
        this.bufHelper.writeOptional(buffer, input);
        final Optional<T> fromBuffer = this.bufHelper.readOptional(buffer);
        TestHelper.assertEqual(helper, input, fromBuffer, this.equality);
    }

    @GameTest
    public void test_optional_empty(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final Optional<T> input = Optional.empty();
        this.bufHelper.writeOptional(buffer, input);
        final Optional<T> fromBuffer = this.bufHelper.readOptional(buffer);
        TestHelper.assertEqual(helper, input, fromBuffer, this.equality);
    }

    @GameTest
    public void test_nullable(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.bufHelper.writeNullable(buffer, this.singleton);
        final T fromBuffer = this.bufHelper.readNullable(buffer);

        if (!this.equality.test(this.singleton, fromBuffer)) {
            helper.fail("Deserialized singleton does not match input! a=" + this.singleton + " b=" + fromBuffer);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_nullable_null(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        this.bufHelper.writeNullable(buffer, null);
        final T fromBuffer = this.bufHelper.readNullable(buffer);

        if (!this.equality.test(null, fromBuffer)) {
            helper.fail("Deserialized singleton does not match input! a=" + null + " b=" + fromBuffer);
            return;
        }

        helper.succeed();
    }

    @GameTest
    public void test_weighted(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final WeightedEntry.Wrapper<T> input = WeightedEntry.wrap(this.singleton, 22);
        this.bufHelper.writeWeighted(buffer, input);
        final WeightedEntry.Wrapper<T> fromBuffer = this.bufHelper.readWeighted(buffer);
        TestHelper.assertEqual(helper, input, fromBuffer, this.equality);
    }

    @GameTest
    public void test_weighted_list(GameTestHelper helper) {

        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        final SimpleWeightedRandomList.Builder<T> builder = new SimpleWeightedRandomList.Builder<>();

        for (int i = 0; i < this.collection.length; i++) {

            builder.add(this.collection[i], i * 4);
        }

        final SimpleWeightedRandomList<T> input = builder.build();

        this.bufHelper.writeWeightedList(buffer, input);
        final SimpleWeightedRandomList<T> fromBuffer = this.bufHelper.readWeightedList(buffer);
        TestHelper.assertEqual(helper, input, fromBuffer, this.equality);
    }

    @Override
    public String getDefaultBatch() {

        return "bookshelf.bytebuf." + this.type;
    }
}