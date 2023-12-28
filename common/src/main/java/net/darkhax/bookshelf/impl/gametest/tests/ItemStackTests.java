package net.darkhax.bookshelf.impl.gametest.tests;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.impl.gametest.ITestable;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemStackTests implements ITestable {

    public String getDefaultBatch() {

        return "bookshelf.itemstack_tests";
    }

    @GameTest
    public void test_stack(GameTestHelper helper) {

        final JsonObject object = new JsonObject();
        object.addProperty("item", "minecraft:stone");
        assertStackIsBasic(helper, CodecTests.fromJson(helper, BookshelfCodecs.ITEM_STACK.get(), object));
        assertStackIsBasic(helper, CodecTests.fromJson(helper, BookshelfCodecs.ITEM_STACK_FLEXIBLE.get(), object));
        assertStackIsBasic(helper, CodecTests.fromJson(helper, BookshelfCodecs.ITEM_STACK_FLEXIBLE.get(), new JsonPrimitive("minecraft:stone")));
    }

    @GameTest
    public void test_stack_tag(GameTestHelper helper) {

        final JsonObject object = new JsonObject();
        object.addProperty("item", "minecraft:stone");
        final JsonObject nbt = new JsonObject();
        nbt.addProperty("test", "hello_world");
        object.add("nbt", nbt);

        final ItemStack readStack = CodecTests.fromJson(helper, BookshelfCodecs.ITEM_STACK.get(), object);

        if (readStack == null || readStack.isEmpty() || !readStack.is(Items.STONE) || readStack.getCount() != 1 || !readStack.hasTag() || !readStack.getTag().contains("test", Tag.TAG_STRING)) {

            helper.fail("Unexpected ItemStack read! " + readStack);
        }

        else {

            helper.succeed();
        }
    }

    private static void assertStackIsBasic(GameTestHelper helper, ItemStack stack) {

        if (stack == null || stack.isEmpty() || !stack.is(Items.STONE) || stack.hasTag() || stack.getCount() != 1) {

            helper.fail("Unexpected ItemStack read! " + stack);
        }

        else {

            helper.succeed();
        }
    }
}
