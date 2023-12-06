package net.darkhax.bookshelf.impl.gametest.tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.Optional;

public class RegistryCodecTests<T> extends CodecTests<T> {

    public RegistryCodecTests(String type, CodecHelper<T> codecHelper, T... collection) {

        super(type, codecHelper, collection);
    }

    @GameTest
    public void test_missing_gives_null(GameTestHelper helper) {

        final JsonElement jsonValue = new JsonPrimitive("bookshelf:non_existent_key_that_does_not_exist");
        final Optional<Pair<T, JsonElement>> result = this.codecHelper.get().decode(JsonOps.INSTANCE, jsonValue).result();

        if (result.isPresent()) {

            helper.fail("Expected value to be null. Got " + result.get().getFirst());
            return;
        }

        helper.succeed();
    }
}