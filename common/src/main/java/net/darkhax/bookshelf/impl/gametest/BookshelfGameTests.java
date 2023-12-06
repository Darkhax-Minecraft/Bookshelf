package net.darkhax.bookshelf.impl.gametest;

import com.google.common.base.CaseFormat;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.bytebuf.BookshelfByteBufs;
import net.darkhax.bookshelf.api.data.bytebuf.ByteBufHelper;
import net.darkhax.bookshelf.api.data.bytebuf.RegistryByteBufHelper;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.darkhax.bookshelf.api.data.codecs.RegistryCodecHelper;
import net.darkhax.bookshelf.api.data.sound.Sound;
import net.darkhax.bookshelf.api.item.ItemStackBuilder;
import net.darkhax.bookshelf.api.util.ItemStackHelper;
import net.darkhax.bookshelf.impl.gametest.tests.ByteBufTests;
import net.darkhax.bookshelf.impl.gametest.tests.CodecTests;
import net.darkhax.bookshelf.impl.gametest.tests.RegistryCodecTests;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiPredicate;

public class BookshelfGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> generate() {

        final Collection<TestFunction> testFunctions = new ArrayList<>();

        // Test Java Type Serializers
        testFrom(testFunctions, BookshelfByteBufs.BOOLEAN, BookshelfCodecs.BOOLEAN, false, true, true, false);
        testFrom(testFunctions, BookshelfByteBufs.BYTE, BookshelfCodecs.BYTE, (byte) 1, (byte) 32, (byte) 44, (byte) 0);
        testFrom(testFunctions, BookshelfByteBufs.SHORT, BookshelfCodecs.SHORT, (short) 800, (short) 1337);
        testFrom(testFunctions, BookshelfByteBufs.INT, BookshelfCodecs.INT, 54, 23, Integer.MAX_VALUE, 234234, Integer.MIN_VALUE);
        testFrom(testFunctions, BookshelfByteBufs.LONG, BookshelfCodecs.LONG, 99L, 23441322L, Long.MIN_VALUE, 93249L - 234L, Long.MAX_VALUE);
        testFrom(testFunctions, BookshelfByteBufs.FLOAT, BookshelfCodecs.FLOAT, 8f, -23.456f, 789.01f, Float.MAX_VALUE, -11f, Float.MIN_VALUE);
        testFrom(testFunctions, BookshelfByteBufs.DOUBLE, BookshelfCodecs.DOUBLE, 24.92d, Double.MAX_VALUE, -922321.12345d, Double.MIN_VALUE);
        testFrom(testFunctions, BookshelfByteBufs.STRING, BookshelfCodecs.STRING, "one", "two", "3", "IV", ".....", "/I", "!@#$%^&*()_-");
        testFrom(testFunctions, BookshelfByteBufs.UUID, BookshelfCodecs.UUID, UUID.randomUUID(), UUID.fromString("da0317d2-e550-11ec-8fea-0242ac120002"), UUID.randomUUID());

        // Test Minecraft Type Serializers
        testFrom(testFunctions, BookshelfByteBufs.RESOURCE_LOCATION, BookshelfCodecs.RESOURCE_LOCATION, new ResourceLocation("hello_world"), new ResourceLocation("test", "two"), new ResourceLocation("test_from", "stuff/things/okay_stuff"));
        testFrom(testFunctions, BookshelfByteBufs.ITEM_STACK, BookshelfCodecs.ITEM_STACK, ItemStackHelper::areStacksEquivalent, getTestStacks());
        testFrom(testFunctions, BookshelfByteBufs.COMPOUND_TAG, BookshelfCodecs.COMPOUND_TAG, ItemStackHelper::areTagsEquivalent, getTestTags());
        testFrom(testFunctions, BookshelfByteBufs.TEXT, BookshelfCodecs.TEXT, Component.translatable("moon.phase.full").withStyle(ChatFormatting.DARK_AQUA), Component.literal("Hello World"), Component.literal("okay").withStyle(s -> s.withFont(new ResourceLocation("minecraft:alt"))));
        testFrom(testFunctions, BookshelfByteBufs.BLOCK_POS, BookshelfCodecs.BLOCK_POS, new BlockPos(1, 2, 3), new BlockPos(0, 0, 0), BlockPos.of(123456L));
        testFrom(testFunctions, BookshelfByteBufs.INGREDIENT, BookshelfCodecs.INGREDIENT, TestHelper::assertEqual, new Ingredient[]{Ingredient.of(Items.STONE_AXE), Ingredient.EMPTY, Ingredient.of(Items.COAL), Ingredient.of(new ItemStack(Items.ACACIA_BOAT)), Ingredient.of(ItemTags.BEDS)});
        testFrom(testFunctions, BookshelfByteBufs.BLOCK_STATE, BookshelfCodecs.BLOCK_STATE, Blocks.STONE.defaultBlockState(), Blocks.CHEST.defaultBlockState(), Blocks.ACACIA_PRESSURE_PLATE.defaultBlockState().setValue(PressurePlateBlock.POWERED, true));
        testFrom(testFunctions, BookshelfByteBufs.ATTRIBUTE_MODIFIER, BookshelfCodecs.ATTRIBUTE_MODIFIER, new AttributeModifier("test", 15d, AttributeModifier.Operation.MULTIPLY_BASE), new AttributeModifier(UUID.randomUUID(), "test_2", 9.55d, AttributeModifier.Operation.ADDITION), new AttributeModifier("test3", 35d, AttributeModifier.Operation.MULTIPLY_TOTAL));
        testFrom(testFunctions, BookshelfByteBufs.EFFECT_INSTANCE, BookshelfCodecs.EFFECT_INSTANCE, new MobEffectInstance(MobEffects.ABSORPTION, 100, 10), new MobEffectInstance(MobEffects.BAD_OMEN, 10));
        testFrom(testFunctions, BookshelfByteBufs.ENCHANTMENT_INSTANCE, BookshelfCodecs.ENCHANTMENT_INSTANCE, TestHelper::assertEqual, new EnchantmentInstance[]{new EnchantmentInstance(Enchantments.ALL_DAMAGE_PROTECTION, 5), new EnchantmentInstance(Enchantments.BINDING_CURSE, 15), new EnchantmentInstance(Enchantments.IMPALING, 2)});
        testFrom(testFunctions, BookshelfByteBufs.VECTOR_3F, BookshelfCodecs.VECTOR_3F, new Vector3f(1f, 2f, 3f), new Vector3f(-5f, -2f, 44f), new Vector3f(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE));
        testFrom(testFunctions, BookshelfByteBufs.SOUND, BookshelfCodecs.SOUND, new Sound(SoundEvents.DOLPHIN_JUMP, SoundSource.AMBIENT, 1f, 1f), new Sound(SoundEvents.ALLAY_HURT, SoundSource.NEUTRAL, 0.5f, 0.222f), new Sound(SoundEvents.SQUID_AMBIENT, SoundSource.PLAYERS, 0.1f, 0.2f));

        // Test Minecraft Enum Serializers
        testFrom(testFunctions, BookshelfByteBufs.ITEM_RARITY, BookshelfCodecs.ITEM_RARITY, Rarity.COMMON, Rarity.EPIC, Rarity.RARE, Rarity.RARE);
        testFrom(testFunctions, BookshelfByteBufs.ENCHANTMENT_RARITY, BookshelfCodecs.ENCHANTMENT_RARITY, Enchantment.Rarity.COMMON, Enchantment.Rarity.COMMON, Enchantment.Rarity.RARE, Enchantment.Rarity.UNCOMMON);
        testFrom(testFunctions, BookshelfByteBufs.ATTRIBUTE_OPERATION, BookshelfCodecs.ATTRIBUTE_OPERATION, AttributeModifier.Operation.ADDITION, AttributeModifier.Operation.ADDITION, AttributeModifier.Operation.MULTIPLY_BASE, AttributeModifier.Operation.MULTIPLY_TOTAL, AttributeModifier.Operation.MULTIPLY_TOTAL);
        testFrom(testFunctions, BookshelfByteBufs.DIRECTION, BookshelfCodecs.DIRECTION, Direction.UP, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.SOUTH);
        testFrom(testFunctions, BookshelfByteBufs.AXIS, BookshelfCodecs.AXIS, Direction.Axis.X, Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z, Direction.Axis.Y);
        testFrom(testFunctions, BookshelfByteBufs.PLANE, BookshelfCodecs.PLANE, Direction.Plane.HORIZONTAL, Direction.Plane.VERTICAL, Direction.Plane.VERTICAL, Direction.Plane.VERTICAL, Direction.Plane.HORIZONTAL);
        testFrom(testFunctions, BookshelfByteBufs.MOB_CATEGORY, BookshelfCodecs.MOB_CATEGORY, MobCategory.AMBIENT, MobCategory.AXOLOTLS, MobCategory.CREATURE, MobCategory.AXOLOTLS, MobCategory.MONSTER, MobCategory.MISC);
        testFrom(testFunctions, BookshelfByteBufs.ENCHANTMENT_CATEGORY, BookshelfCodecs.ENCHANTMENT_CATEGORY, EnchantmentCategory.ARMOR, EnchantmentCategory.BREAKABLE, EnchantmentCategory.BREAKABLE);
        testFrom(testFunctions, BookshelfByteBufs.DYE_COLOR, BookshelfCodecs.DYE_COLOR, DyeColor.BLACK, DyeColor.RED, DyeColor.BLUE);
        testFrom(testFunctions, BookshelfByteBufs.SOUND_SOURCE, BookshelfCodecs.SOUND_SOURCE, SoundSource.AMBIENT, SoundSource.HOSTILE, SoundSource.PLAYERS);
        testFrom(testFunctions, BookshelfByteBufs.ARMOR_MATERIAL, BookshelfCodecs.ARMOR_MATERIAL, ArmorMaterials.CHAIN, ArmorMaterials.DIAMOND, ArmorMaterials.IRON, ArmorMaterials.TURTLE);
        testFrom(testFunctions, BookshelfByteBufs.DIFFICULTY, BookshelfCodecs.DIFFICULTY, Difficulty.EASY, Difficulty.EASY, Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD, Difficulty.PEACEFUL);
        testFrom(testFunctions, BookshelfByteBufs.EQUIPMENT_SLOT, BookshelfCodecs.EQUIPMENT_SLOT, EquipmentSlot.CHEST, EquipmentSlot.CHEST, EquipmentSlot.MAINHAND, EquipmentSlot.FEET, EquipmentSlot.MAINHAND);
        testFrom(testFunctions, BookshelfByteBufs.MIRROR, BookshelfCodecs.MIRROR, Mirror.FRONT_BACK, Mirror.LEFT_RIGHT, Mirror.NONE, Mirror.LEFT_RIGHT);
        testFrom(testFunctions, BookshelfByteBufs.ROTATION, BookshelfCodecs.ROTATION, Rotation.NONE, Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.CLOCKWISE_90);

        // Test Game Registry Serializers
        testFrom(testFunctions, BookshelfByteBufs.GAME_EVENT, BookshelfCodecs.GAME_EVENT);
        testFrom(testFunctions, BookshelfByteBufs.SOUND_EVENT, BookshelfCodecs.SOUND_EVENT);
        testFrom(testFunctions, BookshelfByteBufs.FLUID, BookshelfCodecs.FLUID);
        testFrom(testFunctions, BookshelfByteBufs.MOB_EFFECT, BookshelfCodecs.MOB_EFFECT);
        testFrom(testFunctions, BookshelfByteBufs.BLOCK, BookshelfCodecs.BLOCK);
        testFrom(testFunctions, BookshelfByteBufs.ENCHANTMENT, BookshelfCodecs.ENCHANTMENT);
        testFrom(testFunctions, BookshelfByteBufs.ENTITY_TYPE, BookshelfCodecs.ENTITY_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.ITEM, BookshelfCodecs.ITEM);
        testFrom(testFunctions, BookshelfByteBufs.POTION, BookshelfCodecs.POTION);
        testFrom(testFunctions, BookshelfByteBufs.PARTICLE_TYPE, BookshelfCodecs.PARTICLE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.BLOCK_ENTITY_TYPE, BookshelfCodecs.BLOCK_ENTITY_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.PAINTING_VARIANT, BookshelfCodecs.PAINTING_VARIANT);
        testFrom(testFunctions, BookshelfByteBufs.CUSTOM_STAT, BookshelfCodecs.CUSTOM_STAT);
        testFrom(testFunctions, BookshelfByteBufs.CHUNK_STATUS, BookshelfCodecs.CHUNK_STATUS);
        testFrom(testFunctions, BookshelfByteBufs.RULE_TEST, BookshelfCodecs.RULE_TEST);
        testFrom(testFunctions, BookshelfByteBufs.RULE_BLOCK_ENTITY_MODIFIER, BookshelfCodecs.RULE_BLOCK_ENTITY_MODIFIER);
        testFrom(testFunctions, BookshelfByteBufs.POS_RULE_TEST, BookshelfCodecs.POS_RULE_TEST);
        testFrom(testFunctions, BookshelfByteBufs.MENU, BookshelfCodecs.MENU);
        testFrom(testFunctions, BookshelfByteBufs.RECIPE_TYPE, BookshelfCodecs.RECIPE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.RECIPE_SERIALIZER, BookshelfCodecs.RECIPE_SERIALIZER);
        testFrom(testFunctions, BookshelfByteBufs.ATTRIBUTE, BookshelfCodecs.ATTRIBUTE);
        testFrom(testFunctions, BookshelfByteBufs.POSITION_SOURCE_TYPE, BookshelfCodecs.POSITION_SOURCE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.ARGUMENT_TYPE_INFO_CODEC_HELPER, BookshelfCodecs.ARGUMENT_TYPE_INFO_CODEC_HELPER);
        testFrom(testFunctions, BookshelfByteBufs.STAT_TYPE, BookshelfCodecs.STAT_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.VILLAGER_TYPE, BookshelfCodecs.VILLAGER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.VILLAGER_PROFESSION, BookshelfCodecs.VILLAGER_PROFESSION);
        testFrom(testFunctions, BookshelfByteBufs.POINT_OF_INTEREST_TYPE, BookshelfCodecs.POINT_OF_INTEREST_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.MEMORY_MODULE_TYPE, BookshelfCodecs.MEMORY_MODULE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.SENSOR_TYPE, BookshelfCodecs.SENSOR_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.SCHEDULE, BookshelfCodecs.SCHEDULE);
        testFrom(testFunctions, BookshelfByteBufs.ACTIVITY, BookshelfCodecs.ACTIVITY);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_POOL_ENTRY_TYPE, BookshelfCodecs.LOOT_POOL_ENTRY_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_FUNCTION_TYPE, BookshelfCodecs.LOOT_FUNCTION_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_CONDITION_TYPE, BookshelfCodecs.LOOT_CONDITION_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_NUMBER_PROVIDER_TYPE, BookshelfCodecs.LOOT_NUMBER_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_NBT_PROVIDER_TYPE, BookshelfCodecs.LOOT_NBT_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.LOOT_SCORE_PROVIDER_TYPE, BookshelfCodecs.LOOT_SCORE_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.FLOAT_PROVIDER_TYPE, BookshelfCodecs.FLOAT_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.INT_PROVIDER_TYPE, BookshelfCodecs.INT_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.HEIGHT_PROVIDER_TYPE, BookshelfCodecs.HEIGHT_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.BLOCK_PREDICATE_TYPE, BookshelfCodecs.BLOCK_PREDICATE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.CARVER, BookshelfCodecs.CARVER);
        testFrom(testFunctions, BookshelfByteBufs.FEATURE, BookshelfCodecs.FEATURE);
        testFrom(testFunctions, BookshelfByteBufs.STRUCTURE_PLACEMENT, BookshelfCodecs.STRUCTURE_PLACEMENT);
        testFrom(testFunctions, BookshelfByteBufs.STRUCTURE_PIECE, BookshelfCodecs.STRUCTURE_PIECE);
        testFrom(testFunctions, BookshelfByteBufs.STRUCTURE_TYPE, BookshelfCodecs.STRUCTURE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.PLACEMENT_MODIFIER_TYPE, BookshelfCodecs.PLACEMENT_MODIFIER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.BLOCKSTATE_PROVIDER_TYPE, BookshelfCodecs.BLOCKSTATE_PROVIDER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.FOLIAGE_PLACER_TYPE, BookshelfCodecs.FOLIAGE_PLACER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.TRUNK_PLACER_TYPE, BookshelfCodecs.TRUNK_PLACER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.ROOT_PLACER_TYPE, BookshelfCodecs.ROOT_PLACER_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.TREE_DECORATOR_TYPE, BookshelfCodecs.TREE_DECORATOR_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.FEATURE_SIZE_TYPE, BookshelfCodecs.FEATURE_SIZE_TYPE);
        testFrom(testFunctions, BookshelfByteBufs.STRUCTURE_PROCESSOR, BookshelfCodecs.STRUCTURE_PROCESSOR);
        testFrom(testFunctions, BookshelfByteBufs.STRUCTURE_POOL_ELEMENT, BookshelfCodecs.STRUCTURE_POOL_ELEMENT);
        testFrom(testFunctions, BookshelfByteBufs.CAT_VARIANT, BookshelfCodecs.CAT_VARIANT);
        testFrom(testFunctions, BookshelfByteBufs.FROG_VARIANT, BookshelfCodecs.FROG_VARIANT);
        testFrom(testFunctions, BookshelfByteBufs.BANNER_PATTERN, BookshelfCodecs.BANNER_PATTERN);
        testFrom(testFunctions, BookshelfByteBufs.INSTRUMENT, BookshelfCodecs.INSTRUMENT);
        testFrom(testFunctions, BookshelfByteBufs.DECORATED_POT_PATTERNS, BookshelfCodecs.DECORATED_POT_PATTERNS);
        testFrom(testFunctions, BookshelfByteBufs.CREATIVE_MODE_TAB, BookshelfCodecs.CREATIVE_MODE_TAB);

        return testFunctions;
    }

    private static <T> void testFrom(Collection<TestFunction> functions, ByteBufHelper<T> bufferHelper, CodecHelper<T> codecHelper, T... collection) {

        testFrom(functions, bufferHelper, codecHelper, Objects::equals, collection);
    }

    private static <T> void testFrom(Collection<TestFunction> functions, RegistryByteBufHelper<T> bufferHelper, RegistryCodecHelper<T> codecHelper, T... collection) {

        final T[] testArray = codecHelper.getRegistry().stream().limit(5).toArray(size -> (T[]) Array.newInstance(collection.getClass().getComponentType(), size));
        testFrom(functions, bufferHelper, codecHelper, Objects::equals, testArray);
    }

    private static <T> void testFrom(Collection<TestFunction> functions, ByteBufHelper<T> bufferHelper, CodecHelper<T> codecHelper, BiPredicate<T, T> equality, T... collection) {

        final String typeName = toSnakeCase(collection.getClass().getComponentType().getSimpleName());

        if (codecHelper instanceof RegistryCodecHelper<T> registryCodecHelper && bufferHelper instanceof RegistryByteBufHelper<T> registryBufHelper) {

            final ResourceLocation registryId = registryCodecHelper.getRegistry().key().location();
            final String registryTypeName = registryId.getNamespace() + "_" + registryId.getNamespace();

            final T[] testArray = registryCodecHelper.getRegistry().stream().limit(5).toArray(size -> (T[]) Array.newInstance(collection.getClass().getComponentType(), size));
            final TagKey<T>[] tagExamples = new TagKey[] {TagKey.create(registryCodecHelper.getRegistry().key(), new ResourceLocation(Constants.MOD_ID, "test_one")), TagKey.create(registryCodecHelper.getRegistry().key(), new ResourceLocation(Constants.MOD_ID, "test_two")), TagKey.create(registryCodecHelper.getRegistry().key(), new ResourceLocation("test_three"))};

            testFrom(functions, new RegistryCodecTests<>("registry_" + registryTypeName, registryCodecHelper, testArray));
            testFrom(functions, new ByteBufTests<>("registry_" + registryTypeName, registryBufHelper, testArray));
            testFrom(functions, new CodecTests<>("tags_" + registryTypeName, registryCodecHelper.tag(), tagExamples));
            testFrom(functions, new ByteBufTests<>("tags_" + registryTypeName, registryBufHelper.tag(), tagExamples));
        }

        else {

            testFrom(functions, new CodecTests<>(typeName, codecHelper, equality, collection));
            testFrom(functions, new ByteBufTests<>(typeName, bufferHelper, equality, collection));
        }
    }

    /**
     * Generates tests from a pre-instantiated instance of a class.
     *
     * @param functions The collection to register newly generated functions into.
     * @param testImpl  The implementation of the test class.
     * @param <T>       The type of class to load classes from.
     */
    public static <T> void testFrom(Collection<TestFunction> functions, T testImpl) {

        // I like to batch tests from the same instance together in a separate batch. To allow for
        // this I use a new ITestable interface which allows the testImpl to optionally supply a
        // name for the batch. If a test defines its own non-default batch this will be overridden.
        final String parentBatch = testImpl instanceof ITestable testObj ? testObj.getDefaultBatch() : null;

        // TODO This code can not see inherited methods yet.
        for (Method method : testImpl.getClass().getMethods()) {

            // Only methods annotated with the vanilla GameTest annotation can be used.
            if (method.isAnnotationPresent(GameTest.class)) {

                // Metadata defined by the annotation on the method.
                final GameTest annotation = method.getAnnotation(GameTest.class);

                // The namespaced ID of the template to spawn in the world. An empty structure is
                // usually preferred if the test does not interact with the world.
                final String template = annotation.template().isEmpty() ? "bookshelf:empty" : annotation.template();
                final String batch = parentBatch != null && annotation.batch().equalsIgnoreCase("defaultBatch") ? parentBatch : annotation.batch();

                // I prefer lower snake case for test names but this is not required.
                final String testName = batch + "." + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, method.getName());
                final Rotation rotation = StructureUtils.getRotationForRotationSteps(annotation.rotationSteps());

                functions.add(new TestFunction(batch, testName, template, rotation, annotation.timeoutTicks(), annotation.setupTicks(), annotation.required(), annotation.requiredSuccesses(), annotation.attempts(), gameTestHelper -> {

                    try {

                        method.invoke(testImpl, gameTestHelper);
                    }

                    // The scanned method could not be executed.
                    catch (IllegalAccessException e) {

                        throw new RuntimeException("Failed to invoke test method (%s) in (%s) because %s".formatted(method.getName(), method.getDeclaringClass().getCanonicalName(), e.getMessage()), e);
                    }

                    // The scanned method was executed but thre an exception.
                    catch (InvocationTargetException e) {

                        // Convert the exception into a RuntimeException, so we can rethrow it.
                        final RuntimeException rte = (e.getCause() instanceof RuntimeException runtimeException) ? runtimeException : new RuntimeException(e.getCause());

                        // The vanilla system likes to swallow these errors, so making an external
                        // paper trail for the exception can be very helpful.
                        Constants.LOG.error("The test {} failed to run!", testName, e);
                        throw rte;
                    }
                }));
            }
        }
    }

    private static ItemStack[] getTestStacks() {

        return new ItemStack[]{
                new ItemStack(Items.STONE),
                new ItemStack(Items.STICK).setHoverName(Component.literal("test")),
                new ItemStack(Items.STONE_AXE),
                ItemStack.EMPTY,
                new ItemStackBuilder(Items.GLOW_ITEM_FRAME).enchant(Enchantments.ALL_DAMAGE_PROTECTION).build(),
                new ItemStackBuilder(Items.BOOKSHELF).name(Component.literal("HELP")).lore(Component.literal("hello world")).build()
        };
    }

    private static CompoundTag[] getTestTags() {

        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        tag2.put("one", IntTag.valueOf(33));
        tag2.put("two", StringTag.valueOf("hello"));
        CompoundTag tag3 = new CompoundTag();
        tag3.put("one", tag2);
        tag3.put("two", new ListTag());
        return new CompoundTag[]{
                tag1, tag2, tag3
        };
    }

    private static String toSnakeCase(String str) {

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));

        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            result.append(Character.isUpperCase(ch) ? result.toString() + '_' + Character.toLowerCase(ch) : ch);
        }

        // return the result
        return result.toString();
    }
}