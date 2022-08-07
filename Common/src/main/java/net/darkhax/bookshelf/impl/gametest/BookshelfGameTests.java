package net.darkhax.bookshelf.impl.gametest;

import com.google.common.base.CaseFormat;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.sound.Sound;
import net.darkhax.bookshelf.api.serialization.ISerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.bookshelf.api.util.ItemStackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class BookshelfGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> generate() {

        final Collection<TestFunction> testFunctions = new ArrayList<>();

        // Test Java Type Serializers
        testFrom(testFunctions, new TestSerialization<>("boolean", Serializers.BOOLEAN, false, true, true, false));
        testFrom(testFunctions, new TestSerialization<>("byte", Serializers.BYTE, (byte) 1, (byte) 32, (byte) 44, (byte) 0));
        testFrom(testFunctions, new TestSerialization<>("short", Serializers.SHORT, (short) 800, (short) 1337));
        testFrom(testFunctions, new TestSerialization<>("int", Serializers.INT, 54, 23, Integer.MAX_VALUE, 234234, Integer.MIN_VALUE));
        testFrom(testFunctions, new TestSerialization<>("long", Serializers.LONG, 99L, 23441322L, Long.MIN_VALUE, 93249L - 234L, Long.MAX_VALUE));
        testFrom(testFunctions, new TestSerialization<>("float", Serializers.FLOAT, 8f, -23.456f, 789.01f, Float.MAX_VALUE, -11f, Float.MIN_VALUE));
        testFrom(testFunctions, new TestSerialization<>("double", Serializers.DOUBLE, 24.92d, Double.MAX_VALUE, -922321.12345d, Double.MIN_VALUE));
        testFrom(testFunctions, new TestSerialization<>("string", Serializers.STRING, "one", "two", "3", "IV", ".....", "/I", "!@#$%^&*()_-"));
        testFrom(testFunctions, new TestSerialization<>("uuid", Serializers.UUID, UUID.randomUUID(), UUID.fromString("da0317d2-e550-11ec-8fea-0242ac120002"), UUID.randomUUID()));

        // Test Minecraft Type Serializers
        testFrom(testFunctions, new TestSerialization<>("resource_location", Serializers.RESOURCE_LOCATION, new ResourceLocation("hello_world"), new ResourceLocation("test", "two"), new ResourceLocation("test_from", "stuff/things/okay_stuff")));
        testFrom(testFunctions, new TestSerialization<>("item_stack", Serializers.ITEM_STACK, ItemStackHelper::areStacksEquivalent, ItemStackHelper.getTabItems(CreativeModeTab.TAB_COMBAT)));
        testFrom(testFunctions, new TestSerialization<>("nbt_compound_tag", Serializers.COMPOUND_TAG, Arrays.stream(ItemStackHelper.getTabItems(CreativeModeTab.TAB_COMBAT)).filter(ItemStack::hasTag).map(ItemStack::getTag).toArray(CompoundTag[]::new)));
        testFrom(testFunctions, new TestSerialization<>("text_component", Serializers.TEXT, Component.translatable("moon.phase.full").withStyle(ChatFormatting.DARK_AQUA), Component.literal("Hello World"), Component.literal("okay").withStyle(s -> s.withFont(new ResourceLocation("minecraft:alt")))));
        testFrom(testFunctions, new TestSerialization<>("block_pos", Serializers.BLOCK_POS, new BlockPos(1, 2, 3), new BlockPos(0, 0, 0), BlockPos.of(123456L)));
        testFrom(testFunctions, new TestSerialization<>("ingredient", Serializers.INGREDIENT, BookshelfGameTests::assertEqual, new Ingredient[]{Ingredient.of(Items.STONE_AXE), Ingredient.EMPTY, Ingredient.of(Items.COAL), Ingredient.of(new ItemStack(Items.ACACIA_BOAT)), Ingredient.of(ItemTags.BEDS)}));
        testFrom(testFunctions, new TestSerialization<>("block_state", Serializers.BLOCK_STATE, Blocks.STONE.defaultBlockState(), Blocks.CHEST.defaultBlockState(), Blocks.ACACIA_PRESSURE_PLATE.defaultBlockState().setValue(PressurePlateBlock.POWERED, true)));
        testFrom(testFunctions, new TestSerialization<>("attribute_modifier", Serializers.ATTRIBUTE_MODIFIER, new AttributeModifier("test", 15d, AttributeModifier.Operation.MULTIPLY_BASE), new AttributeModifier(UUID.randomUUID(), "test_2", 9.55d, AttributeModifier.Operation.ADDITION), new AttributeModifier("test3", 35d, AttributeModifier.Operation.MULTIPLY_TOTAL)));
        testFrom(testFunctions, new TestSerialization<>("effect_instance", Serializers.EFFECT_INSTANCE, new MobEffectInstance(MobEffects.ABSORPTION, 100, 10), new MobEffectInstance(MobEffects.BAD_OMEN, 10)));
        testFrom(testFunctions, new TestSerialization<>("enchantment_instance", Serializers.ENCHANTMENT_INSTANCE, BookshelfGameTests::assertEncantmentInstanceEqual, new EnchantmentInstance[]{new EnchantmentInstance(Enchantments.ALL_DAMAGE_PROTECTION, 5), new EnchantmentInstance(Enchantments.BINDING_CURSE, 15), new EnchantmentInstance(Enchantments.IMPALING, 2)}));
        testFrom(testFunctions, new TestSerialization<>("Vector3f", Serializers.VECTOR_3F, new Vector3f(1f, 2f, 3f), new Vector3f(-5f, -2f, 44f), new Vector3f(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)));
        testFrom(testFunctions, new TestSerialization<>("Vector4f", Serializers.VECTOR_4F, new Vector4f(1f, 2f, 3f, 4f), new Vector4f(0f, -22f, -2222f, 0f), new Vector4f(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE)));
        testFrom(testFunctions, new TestSerialization<>("sound", Serializers.SOUND, new Sound(SoundEvents.DOLPHIN_JUMP, SoundSource.AMBIENT, 1f, 1f), new Sound(SoundEvents.AMBIENT_CAVE, SoundSource.NEUTRAL, 0.5f, 0.222f), new Sound(SoundEvents.SQUID_AMBIENT, SoundSource.PLAYERS, 0.1f, 0.2f)));

        // Test Minecraft Enum Serializers
        testFrom(testFunctions, new TestSerialization<>("item_rarity", Serializers.ITEM_RARITY, Rarity.COMMON, Rarity.EPIC, Rarity.RARE, Rarity.RARE));
        testFrom(testFunctions, new TestSerialization<>("enchantment_rarity", Serializers.ENCHANTMENT_RARITY, Enchantment.Rarity.COMMON, Enchantment.Rarity.COMMON, Enchantment.Rarity.RARE, Enchantment.Rarity.UNCOMMON));
        testFrom(testFunctions, new TestSerialization<>("attribute_modifier", Serializers.ATTRIBUTE_OPERATION, AttributeModifier.Operation.ADDITION, AttributeModifier.Operation.ADDITION, AttributeModifier.Operation.MULTIPLY_BASE, AttributeModifier.Operation.MULTIPLY_TOTAL, AttributeModifier.Operation.MULTIPLY_TOTAL));
        testFrom(testFunctions, new TestSerialization<>("direction", Serializers.DIRECTION, Direction.UP, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.SOUTH));
        testFrom(testFunctions, new TestSerialization<>("axis", Serializers.AXIS, Direction.Axis.X, Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z, Direction.Axis.Y));
        testFrom(testFunctions, new TestSerialization<>("plane", Serializers.PLANE, Direction.Plane.HORIZONTAL, Direction.Plane.VERTICAL, Direction.Plane.VERTICAL, Direction.Plane.VERTICAL, Direction.Plane.HORIZONTAL));
        testFrom(testFunctions, new TestSerialization<>("mob_category", Serializers.MOB_CATEGORY, MobCategory.AMBIENT, MobCategory.AXOLOTLS, MobCategory.CREATURE, MobCategory.AXOLOTLS, MobCategory.MONSTER, MobCategory.MISC));
        testFrom(testFunctions, new TestSerialization<>("enchantment_category", Serializers.ENCHANTMENT_CATEGORY, EnchantmentCategory.ARMOR, EnchantmentCategory.BREAKABLE, EnchantmentCategory.BREAKABLE));
        testFrom(testFunctions, new TestSerialization<>("dye_color", Serializers.DYE_COLOR, DyeColor.BLACK, DyeColor.RED, DyeColor.BLUE));
        testFrom(testFunctions, new TestSerialization<>("sound_category", Serializers.SOUND_CATEGORY, SoundSource.AMBIENT, SoundSource.HOSTILE, SoundSource.PLAYERS));

        // Test Game Registry Serializers
        testFrom(testFunctions, new TestSerialization<>("registry_block", Serializers.BLOCK, Blocks.SAND, Blocks.STONE, Blocks.KELP, Blocks.SAND));
        testFrom(testFunctions, new TestSerialization<>("registry_item", Serializers.ITEM, Items.APPLE, Items.STICK, Items.STICK, Items.COOKED_PORKCHOP));
        testFrom(testFunctions, new TestSerialization<>("registry_enchantment", Serializers.ENCHANTMENT, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.SILK_TOUCH));
        testFrom(testFunctions, new TestSerialization<>("registry_painting", Serializers.PAINTING, Services.REGISTRIES.paintings().get(PaintingVariants.COURBET.location()), Services.REGISTRIES.paintings().get(PaintingVariants.AZTEC.location())));
        testFrom(testFunctions, new TestSerialization<>("registry_potion", Serializers.POTION, Potions.EMPTY, Potions.AWKWARD, Potions.AWKWARD, Potions.FIRE_RESISTANCE, Potions.HEALING));
        testFrom(testFunctions, new TestSerialization<>("registry_attribute", Serializers.ATTRIBUTE, Attributes.ARMOR, Attributes.ATTACK_SPEED, Attributes.MAX_HEALTH, Attributes.SPAWN_REINFORCEMENTS_CHANCE));
        testFrom(testFunctions, new TestSerialization<>("registry_villager_profession", Serializers.VILLAGER_PROFESSION, VillagerProfession.ARMORER, VillagerProfession.ARMORER, VillagerProfession.BUTCHER, VillagerProfession.LEATHERWORKER, VillagerProfession.WEAPONSMITH));
        testFrom(testFunctions, new TestSerialization<>("registry_villager_type", Serializers.VILLAGER_TYPE, VillagerType.SWAMP, VillagerType.JUNGLE, VillagerType.JUNGLE, VillagerType.PLAINS));
        testFrom(testFunctions, new TestSerialization<>("registry_sound_event", Serializers.SOUND_EVENT, SoundEvents.ANVIL_STEP, SoundEvents.BAMBOO_STEP, SoundEvents.AXE_STRIP, SoundEvents.DOLPHIN_JUMP));
        testFrom(testFunctions, new TestSerialization<>("registry_menu", Serializers.MENU, MenuType.ANVIL, MenuType.GENERIC_9x2, MenuType.ENCHANTMENT, MenuType.CARTOGRAPHY_TABLE));
        testFrom(testFunctions, new TestSerialization<>("registry_particle_type", Serializers.PARTICLE, ParticleTypes.BUBBLE_POP, ParticleTypes.BUBBLE_COLUMN_UP, ParticleTypes.CLOUD, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, ParticleTypes.DRIPPING_OBSIDIAN_TEAR));
        testFrom(testFunctions, new TestSerialization<>("registry_entity_type", Serializers.ENTITY, EntityType.BAT, EntityType.ARROW, EntityType.AXOLOTL, EntityType.DRAGON_FIREBALL));
        testFrom(testFunctions, new TestSerialization<>("registry_block_entity_type", Serializers.BLOCK_ENTITY, BlockEntityType.BARREL, BlockEntityType.BEACON, BlockEntityType.BEEHIVE, BlockEntityType.JUKEBOX));
        testFrom(testFunctions, new TestSerialization<>("registry_game_event", Serializers.GAME_EVENT, GameEvent.FLAP, GameEvent.FLUID_PICKUP, GameEvent.PRIME_FUSE, GameEvent.LIGHTNING_STRIKE));

        // Test Game Registry Tags
        final ResourceLocation tagOne = new ResourceLocation("test", "one");
        final ResourceLocation tagTwo = new ResourceLocation("test", "two");

        testFromTags(testFunctions, "block_tag", Serializers.BLOCK_TAG, BlockTags.ANVIL, BlockTags.BEDS, BlockTags.BASE_STONE_NETHER);
        testFromTags(testFunctions, "item_tag", Serializers.ITEM_TAG, ItemTags.ANVIL, ItemTags.AXOLOTL_TEMPT_ITEMS, ItemTags.BIRCH_LOGS);
        testFromTags(testFunctions, "banner_pattern_tag", Serializers.BANNER_PATTERN_TAG, BannerPatternTags.NO_ITEM_REQUIRED, BannerPatternTags.PATTERN_ITEM_GLOBE, BannerPatternTags.PATTERN_ITEM_CREEPER);
        testFromTags(testFunctions, "enchantment_tag", Serializers.ENCHANTMENT_TAG, Services.TAGS.enchantmentTag(tagOne), Services.TAGS.enchantmentTag(tagTwo));
        testFromTags(testFunctions, "painting_tag", Serializers.MOTIVE_TAG, PaintingVariantTags.PLACEABLE, PaintingVariantTags.PLACEABLE);
        testFromTags(testFunctions, "mob_effect_tag", Serializers.MOB_EFFECT_TAG, Services.TAGS.effectTag(tagOne), Services.TAGS.effectTag(tagTwo));
        testFromTags(testFunctions, "potion_tag", Serializers.POTION_TAG, Services.TAGS.potionTag(tagOne), Services.TAGS.potionTag(tagTwo));
        testFromTags(testFunctions, "attribute_tag", Serializers.ATTRIBUTE_TAG, Services.TAGS.attributeTag(tagOne), Services.TAGS.attributeTag(tagTwo));
        testFromTags(testFunctions, "villager_profession_tag", Serializers.VILLAGER_PROFESSION_TAG, Services.TAGS.villagerProfessionTag(tagOne), Services.TAGS.villagerProfessionTag(tagTwo));
        testFromTags(testFunctions, "villager_type_tag", Serializers.VILLAGER_TYPE_TAG, Services.TAGS.villagerTypeTag(tagOne), Services.TAGS.villagerTypeTag(tagTwo));
        testFromTags(testFunctions, "sound_event_tag", Serializers.SOUND_EVENT_TAG, Services.TAGS.soundTag(tagOne), Services.TAGS.soundTag(tagTwo));
        testFromTags(testFunctions, "menu_tag", Serializers.MENU_TAG, Services.TAGS.menuTag(tagOne), Services.TAGS.menuTag(tagTwo));
        testFromTags(testFunctions, "particle_type_tag", Serializers.PARTICLE_TAG, Services.TAGS.particleTag(tagOne), Services.TAGS.particleTag(tagTwo));
        testFromTags(testFunctions, "entity_type_tag", Serializers.ENTITY_TAG, EntityTypeTags.ARROWS, EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES, EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS);
        testFromTags(testFunctions, "block_entity_type_tag", Serializers.BLOCK_ENTITY_TAG, Services.TAGS.blockEntityTag(tagOne), Services.TAGS.blockEntityTag(tagTwo));
        testFromTags(testFunctions, "game_event_tag", Serializers.GAME_EVENT_TAG, GameEventTags.IGNORE_VIBRATIONS_SNEAKING, GameEventTags.VIBRATIONS, GameEventTags.VIBRATIONS);
        testFromTags(testFunctions, "fluid_tag", Serializers.FLUID_TAG, FluidTags.LAVA, FluidTags.WATER);
        testFromTags(testFunctions, "stat_tag", Serializers.STAT_TAG, Services.TAGS.statTag(tagOne), Services.TAGS.statTag(tagTwo));
        testFromTags(testFunctions, "recipe_type_tag", Serializers.RECIPE_TYPE_TAG, Services.TAGS.recipeTypeTag(tagOne), Services.TAGS.recipeTypeTag(tagTwo));
        testFromTags(testFunctions, "recipe_serializer_tag", Serializers.RECIPE_SERIALIZER_TAG, Services.TAGS.recipeSerializerTag(tagOne), Services.TAGS.recipeSerializerTag(tagTwo));
        testFromTags(testFunctions, "dimension_type_tag", Serializers.DIMENSION_TYPE_TAG, Services.TAGS.dimensionTypeTag(tagOne), Services.TAGS.dimensionTypeTag(tagTwo));
        testFromTags(testFunctions, "dimension_tag", Serializers.DIMENSION_TAG, Services.TAGS.dimensionTag(tagOne), Services.TAGS.dimensionTag(tagTwo));
        testFromTags(testFunctions, "biome_tag", Serializers.BIOME_TAG, Services.TAGS.biomeTag(tagOne), Services.TAGS.biomeTag(tagTwo));

        return testFunctions;
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
        for (Method method : testImpl.getClass().getDeclaredMethods()) {

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

    /**
     * A generally unnecessary helper for adding tests for a tag serializer. This primarily exists to simplify generic
     * vararg array issues.
     *
     * @param functions  The collection to register newly generated functions into.
     * @param name       The name of the type being serialized. This is used for debug output.
     * @param serializer The serializer that handles this tag type.
     * @param tags       Arbitrary tags to test serialization with.
     * @param <T>        The type of tag being serialized.
     */
    private static <T> void testFromTags(Collection<TestFunction> functions, String name, ISerializer<TagKey<T>> serializer, TagKey<T>... tags) {

        testFrom(functions, new TestSerialization<>(name, serializer, BookshelfGameTests::assertTagEqual, tags));
    }

    /**
     * Checks if two enchantment instance are equal.
     *
     * @param a The original instance to test.
     * @param b The instance that resulted from deserialization.
     * @return Whether the enchantment instances were equal or not.
     */
    private static boolean assertEncantmentInstanceEqual(EnchantmentInstance a, EnchantmentInstance b) {

        if (Objects.equals(a, b)) {

            return true;
        }

        if (a.enchantment != b.enchantment) {

            Constants.LOG.error("Enchantment {} != {}", a.enchantment, b.enchantment);
            return false;
        }

        if (a.level != b.level) {

            Constants.LOG.error("Level {} != {}", a.level, b.level);
            return false;
        }

        return true;
    }

    /**
     * Internal code to test if two tag keys are equal.
     *
     * @param a   The original tag to test.
     * @param b   The tag that resulted from deserialization.
     * @param <T> The type of the tag.
     * @return Whether the tags were equal or not.
     */
    private static <T> boolean assertTagEqual(TagKey<T> a, TagKey<T> b) {

        return Objects.equals(a, b) || Objects.equals(a.location(), b.location());
    }

    /**
     * Internal code to test if two ingredients are equal. This approach is good enough for vanilla ingredients which is
     * all our test cares about, however this is not robust enough for general use, or for use with modded ingredient
     * specs.
     *
     * @param original The original ingredient to test.
     * @param result   The ingredient that resulted from deserialization.
     * @return Whether the ingredients were equal enough or not.
     */
    private static boolean assertEqual(Ingredient original, Ingredient result) {

        if (Objects.equals(original, result)) {

            return true;
        }

        final ItemStack[] originalStacks = original.getItems();
        final ItemStack[] resultStacks = result.getItems();

        if (originalStacks.length != resultStacks.length) {

            Constants.LOG.error("Size mismatch. original={} result={}", originalStacks.length, resultStacks.length);
            return false;
        }

        for (int index = 0; index < originalStacks.length; index++) {

            if (!ItemStackHelper.areStacksEquivalent(originalStacks[index], resultStacks[index])) {

                Constants.LOG.error("Mismatch at index {}. original={} result={}", index, originalStacks[index], resultStacks[index]);

                return false;
            }
        }

        return true;
    }
}