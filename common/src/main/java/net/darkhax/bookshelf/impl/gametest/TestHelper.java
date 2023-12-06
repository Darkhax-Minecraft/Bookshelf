package net.darkhax.bookshelf.impl.gametest;

import com.google.common.collect.Iterables;
import net.darkhax.bookshelf.api.util.ItemStackHelper;
import net.darkhax.bookshelf.mixin.accessors.util.random.AccessorWeightedRandomList;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class TestHelper {

    public static <T> void assertEqual(GameTestHelper helper, Optional<T> a, Optional<T> b, BiPredicate<T, T> equality) {

        if (a.isPresent() != b.isPresent()) {
            helper.fail("Optional values inconsistent. Original=" + a.isPresent() + " Result=" + b.isPresent());
            return;
        }

        else {

            if (a.isPresent() && b.isPresent() && !equality.test(a.get(), b.get())) {

                helper.fail("Optional values do not match. Original=" + a.get() + " Result=" + b.get());
                return;
            }
        }

        helper.succeed();
    }

    public static <T> void assertEqual(GameTestHelper helper, T[] a, T[] b, BiPredicate<T, T> equality) {

        if (a.length != b.length) {

            helper.fail("Arrays do not have the same size. Original=" + a.length + " Result=" + b.length);
        }

        else {

            for (int index = 0; index < a.length; index++) {

                if (!equality.test(a[index], b[index])) {

                    helper.fail("List entry does not match! index=" + index + " a=" + a + " b=" + b);
                    return;
                }
            }

            helper.succeed();
        }
    }

    public static boolean assertEqual(EnchantmentInstance a, EnchantmentInstance b) {

        return Objects.equals(a, b) || (a.enchantment == b.enchantment && a.level == b.level);
    }

    public static <T> boolean assertEqual(TagKey<T> a, TagKey<T> b) {

        return Objects.equals(a, b) || Objects.equals(a.location(), b.location());
    }

    public static boolean assertEqual(Ingredient original, Ingredient result) {

        if (Objects.equals(original, result)) {

            return true;
        }

        final ItemStack[] originalStacks = original.getItems();
        final ItemStack[] resultStacks = result.getItems();

        if (originalStacks.length != resultStacks.length) {

            return false;
        }

        for (int index = 0; index < originalStacks.length; index++) {

            if (!ItemStackHelper.areStacksEquivalent(originalStacks[index], resultStacks[index])) {

                return false;
            }
        }

        return true;
    }

    public static <T> void assertEqual(GameTestHelper helper, WeightedEntry.Wrapper<T> original, WeightedEntry.Wrapper<T> result, BiPredicate<T, T> equality) {

        if (Objects.equals(original, result)) {
            helper.succeed();
        }

        else if (original.getWeight().asInt() != result.getWeight().asInt()) {
            helper.fail("Weights do not match. a=" + original.getWeight() + " b=" + result.getWeight());
        }

        else if (!equality.test(original.getData(), result.getData())) {
            helper.fail("Weighted entries do not match. a=" + original.getData() + " b=" + result.getData());
        }

        else {
            helper.succeed();
        }
    }

    public static <T> void assertEqual(GameTestHelper helper, SimpleWeightedRandomList<T> original, SimpleWeightedRandomList<T> result) {

        assertEqual(helper, original, result, Objects::equals);
    }

    public static <T> void assertEqual(GameTestHelper helper, SimpleWeightedRandomList<T> original, SimpleWeightedRandomList<T> result, BiPredicate<T, T> equality) {

        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessOriginal = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) original);
        final AccessorWeightedRandomList<WeightedEntry.Wrapper<T>> accessResult = ((AccessorWeightedRandomList<WeightedEntry.Wrapper<T>>) result);

        if (accessOriginal.bookshelf$getEntries().size() != accessResult.bookshelf$getEntries().size()) {

            helper.fail("Weighted lists do not have the same size. Original=" + accessOriginal.bookshelf$getEntries().size() + " Result=" + accessResult.bookshelf$getEntries().size());
        }

        else {

            for (int index = 0; index < accessOriginal.bookshelf$getEntries().size(); index++) {

                final WeightedEntry.Wrapper<T> a = accessOriginal.bookshelf$getEntries().get(index);
                final WeightedEntry.Wrapper<T> b = accessResult.bookshelf$getEntries().get(index);

                if (a.getWeight().asInt() != b.getWeight().asInt()) {

                    helper.fail("Weighted list entry weights do not match! index=" + index + " a=" + a.getWeight().asInt() + " b=" + b.getWeight().asInt());
                    return;
                }

                if (!equality.test(a.getData(), b.getData())) {

                    helper.fail("Weighted list entry values do not match! index=" + index + " a=" + a.getData() + " b=" + b.getData());
                    return;
                }
            }

            helper.succeed();
        }
    }

    public static <T> void assertEqual(GameTestHelper helper, Iterable<T> original, Iterable<T> result) {

        assertEqual(helper, original, result, Objects::equals);
    }

    public static <T> void assertEqual(GameTestHelper helper, Iterable<T> original, Iterable<T> result, BiPredicate<T, T> equality) {

        final int originalSize = Iterables.size(original);
        final int resultSize = Iterables.size(result);

        if (originalSize != resultSize) {

            helper.fail("Lists do not have the same size. Original=" + originalSize + " Result=" + resultSize);
        }

        else {

            for (int index = 0; index < originalSize; index++) {

                final T a = Iterables.get(original, index);
                final T b = Iterables.get(result, index);

                if (!equality.test(a, b)) {

                    helper.fail("List entry does not match! index=" + index + " a=" + a + " b=" + b);
                    return;
                }
            }

            helper.succeed();
        }
    }
}
