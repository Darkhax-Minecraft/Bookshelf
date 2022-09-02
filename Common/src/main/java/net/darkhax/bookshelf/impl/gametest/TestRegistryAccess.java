package net.darkhax.bookshelf.impl.gametest;

import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;

public class TestRegistryAccess<T> implements ITestable {

    private final IRegistryReader<T> registryReader;
    private final T lastValue;

    public TestRegistryAccess(IRegistryReader<T> registryReader) {

        this.registryReader = registryReader;
        this.lastValue = registryReader.streamValues().skip(registryReader.streamValues().count() - 1).findFirst().orElseThrow();
    }

    @GameTest
    public void testRealIdLookup(GameTestHelper helper) {

        final ResourceLocation id = this.registryReader.getId(this.lastValue);

        if (id != null) {

            helper.succeed();
        }

        else {

            helper.fail("Id for lookup was null.");
        }
    }

    @GameTest
    public void testNullableLookup(GameTestHelper helper) {

        final T value = this.registryReader.get(new ResourceLocation("fake_mc_fake", "im_not_a_real_boy"));

        if (value != null) {

            helper.fail("Expected null. Got " + value);
        }

        else {

            helper.succeed();
        }
    }

    @GameTest
    public void testRealValueLookup(GameTestHelper helper) {

        final ResourceLocation id = this.registryReader.getId(this.lastValue);
        final T lookup = this.registryReader.get(id);

        if (id == null) {

            helper.fail("Lookup ID was null!");
        }

        else if (lookup == null) {

            helper.fail("Lookup was null for ID " + id + " was null");
        }

        else if (lookup != this.lastValue) {

            helper.fail("Incorrect match. Expected " + this.lastValue + " Got " + lookup);
        }

        else {

            helper.succeed();
        }
    }

    @Override
    public String getDefaultBatch() {

        return "bookshelf_serialization_" + this.registryReader.getRegistryName().toString();
    }
}