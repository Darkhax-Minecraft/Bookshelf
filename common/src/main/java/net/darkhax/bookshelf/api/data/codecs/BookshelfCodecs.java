package net.darkhax.bookshelf.api.data.codecs;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.api.data.sound.Sound;
import net.darkhax.bookshelf.api.util.TextHelper;
import net.darkhax.bookshelf.mixin.accessors.effect.AccessorMobEffectInstance;
import net.minecraft.Optionull;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatType;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.apache.commons.lang3.EnumUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BookshelfCodecs {

    // JAVA TYPES
    public static final CodecHelper<Boolean> BOOLEAN = new CodecHelper<>(Codec.BOOL);
    public static final CodecHelper<Byte> BYTE = new CodecHelper<>(Codec.BYTE);
    public static final CodecHelper<Short> SHORT = new CodecHelper<>(Codec.SHORT);
    public static final CodecHelper<Integer> INT = new CodecHelper<>(Codec.INT);
    public static final CodecHelper<Float> FLOAT = new CodecHelper<>(Codec.FLOAT);
    public static final CodecHelper<Long> LONG = new CodecHelper<>(Codec.LONG);
    public static final CodecHelper<Double> DOUBLE = new CodecHelper<>(Codec.DOUBLE);
    public static final CodecHelper<String> STRING = new CodecHelper<>(Codec.STRING);
    public static final CodecHelper<UUID> UUID = new CodecHelper<>(UUIDUtil.CODEC);

    // REGISTRIES
    public static final RegistryCodecHelper<GameEvent> GAME_EVENT = new RegistryCodecHelper<>(BuiltInRegistries.GAME_EVENT);
    public static final RegistryCodecHelper<SoundEvent> SOUND_EVENT = new RegistryCodecHelper<>(BuiltInRegistries.SOUND_EVENT);
    public static final RegistryCodecHelper<Fluid> FLUID = new RegistryCodecHelper<>(BuiltInRegistries.FLUID);
    public static final RegistryCodecHelper<MobEffect> MOB_EFFECT = new RegistryCodecHelper<>(BuiltInRegistries.MOB_EFFECT);
    public static final RegistryCodecHelper<Block> BLOCK = new RegistryCodecHelper<>(BuiltInRegistries.BLOCK);
    public static final RegistryCodecHelper<Enchantment> ENCHANTMENT = new RegistryCodecHelper<>(BuiltInRegistries.ENCHANTMENT);
    public static final RegistryCodecHelper<EntityType<?>> ENTITY_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.ENTITY_TYPE);
    public static final RegistryCodecHelper<Item> ITEM = new RegistryCodecHelper<>(BuiltInRegistries.ITEM);
    public static final RegistryCodecHelper<Potion> POTION = new RegistryCodecHelper<>(BuiltInRegistries.POTION);
    public static final RegistryCodecHelper<ParticleType<?>> PARTICLE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.PARTICLE_TYPE);
    public static final RegistryCodecHelper<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final RegistryCodecHelper<PaintingVariant> PAINTING_VARIANT = new RegistryCodecHelper<>(BuiltInRegistries.PAINTING_VARIANT);
    public static final RegistryCodecHelper<ResourceLocation> CUSTOM_STAT = new RegistryCodecHelper<>(BuiltInRegistries.CUSTOM_STAT);
    public static final RegistryCodecHelper<ChunkStatus> CHUNK_STATUS = new RegistryCodecHelper<>(BuiltInRegistries.CHUNK_STATUS);
    public static final RegistryCodecHelper<RuleTestType<?>> RULE_TEST = new RegistryCodecHelper<>(BuiltInRegistries.RULE_TEST);
    public static final RegistryCodecHelper<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = new RegistryCodecHelper<>(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
    public static final RegistryCodecHelper<PosRuleTestType<?>> POS_RULE_TEST = new RegistryCodecHelper<>(BuiltInRegistries.POS_RULE_TEST);
    public static final RegistryCodecHelper<MenuType<?>> MENU = new RegistryCodecHelper<>(BuiltInRegistries.MENU);
    public static final RegistryCodecHelper<RecipeType<?>> RECIPE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.RECIPE_TYPE);
    public static final RegistryCodecHelper<RecipeSerializer<?>> RECIPE_SERIALIZER = new RegistryCodecHelper<>(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final RegistryCodecHelper<Attribute> ATTRIBUTE = new RegistryCodecHelper<>(BuiltInRegistries.ATTRIBUTE);
    public static final RegistryCodecHelper<PositionSourceType<?>> POSITION_SOURCE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.POSITION_SOURCE_TYPE);
    public static final RegistryCodecHelper<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_INFO_CODEC_HELPER = new RegistryCodecHelper<>(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
    public static final RegistryCodecHelper<StatType<?>> STAT_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.STAT_TYPE);
    public static final RegistryCodecHelper<VillagerType> VILLAGER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.VILLAGER_TYPE);
    public static final RegistryCodecHelper<VillagerProfession> VILLAGER_PROFESSION = new RegistryCodecHelper<>(BuiltInRegistries.VILLAGER_PROFESSION);
    public static final RegistryCodecHelper<PoiType> POINT_OF_INTEREST_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.POINT_OF_INTEREST_TYPE);
    public static final RegistryCodecHelper<MemoryModuleType<?>> MEMORY_MODULE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.MEMORY_MODULE_TYPE);
    public static final RegistryCodecHelper<SensorType<?>> SENSOR_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.SENSOR_TYPE);
    public static final RegistryCodecHelper<Schedule> SCHEDULE = new RegistryCodecHelper<>(BuiltInRegistries.SCHEDULE);
    public static final RegistryCodecHelper<Activity> ACTIVITY = new RegistryCodecHelper<>(BuiltInRegistries.ACTIVITY);
    public static final RegistryCodecHelper<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
    public static final RegistryCodecHelper<LootItemFunctionType> LOOT_FUNCTION_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_FUNCTION_TYPE);
    public static final RegistryCodecHelper<LootItemConditionType> LOOT_CONDITION_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_CONDITION_TYPE);
    public static final RegistryCodecHelper<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
    public static final RegistryCodecHelper<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
    public static final RegistryCodecHelper<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
    public static final RegistryCodecHelper<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.FLOAT_PROVIDER_TYPE);
    public static final RegistryCodecHelper<IntProviderType<?>> INT_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.INT_PROVIDER_TYPE);
    public static final RegistryCodecHelper<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
    public static final RegistryCodecHelper<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.BLOCK_PREDICATE_TYPE);
    public static final RegistryCodecHelper<WorldCarver<?>> CARVER = new RegistryCodecHelper<>(BuiltInRegistries.CARVER);
    public static final RegistryCodecHelper<Feature<?>> FEATURE = new RegistryCodecHelper<>(BuiltInRegistries.FEATURE);
    public static final RegistryCodecHelper<StructurePlacementType<?>> STRUCTURE_PLACEMENT = new RegistryCodecHelper<>(BuiltInRegistries.STRUCTURE_PLACEMENT);
    public static final RegistryCodecHelper<StructurePieceType> STRUCTURE_PIECE = new RegistryCodecHelper<>(BuiltInRegistries.STRUCTURE_PIECE);
    public static final RegistryCodecHelper<StructureType<?>> STRUCTURE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.STRUCTURE_TYPE);
    public static final RegistryCodecHelper<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
    public static final RegistryCodecHelper<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
    public static final RegistryCodecHelper<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.FOLIAGE_PLACER_TYPE);
    public static final RegistryCodecHelper<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.TRUNK_PLACER_TYPE);
    public static final RegistryCodecHelper<RootPlacerType<?>> ROOT_PLACER_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.ROOT_PLACER_TYPE);
    public static final RegistryCodecHelper<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.TREE_DECORATOR_TYPE);
    public static final RegistryCodecHelper<FeatureSizeType<?>> FEATURE_SIZE_TYPE = new RegistryCodecHelper<>(BuiltInRegistries.FEATURE_SIZE_TYPE);
    public static final RegistryCodecHelper<StructureProcessorType<?>> STRUCTURE_PROCESSOR = new RegistryCodecHelper<>(BuiltInRegistries.STRUCTURE_PROCESSOR);
    public static final RegistryCodecHelper<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = new RegistryCodecHelper<>(BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
    public static final RegistryCodecHelper<CatVariant> CAT_VARIANT = new RegistryCodecHelper<>(BuiltInRegistries.CAT_VARIANT);
    public static final RegistryCodecHelper<FrogVariant> FROG_VARIANT = new RegistryCodecHelper<>(BuiltInRegistries.FROG_VARIANT);
    public static final RegistryCodecHelper<BannerPattern> BANNER_PATTERN = new RegistryCodecHelper<>(BuiltInRegistries.BANNER_PATTERN);
    public static final RegistryCodecHelper<Instrument> INSTRUMENT = new RegistryCodecHelper<>(BuiltInRegistries.INSTRUMENT);
    public static final RegistryCodecHelper<String> DECORATED_POT_PATTERNS = new RegistryCodecHelper<>(BuiltInRegistries.DECORATED_POT_PATTERNS);
    public static final RegistryCodecHelper<CreativeModeTab> CREATIVE_MODE_TAB = new RegistryCodecHelper<>(BuiltInRegistries.CREATIVE_MODE_TAB);

    // ENUMS
    public static final CodecHelper<Rarity> ITEM_RARITY = new CodecHelper<>(enumerable(Rarity.class));
    public static final CodecHelper<Enchantment.Rarity> ENCHANTMENT_RARITY = new CodecHelper<>(enumerable(Enchantment.Rarity.class));
    public static final CodecHelper<AttributeModifier.Operation> ATTRIBUTE_OPERATION = new CodecHelper<>(enumerable(AttributeModifier.Operation.class));
    public static final CodecHelper<Direction> DIRECTION = new CodecHelper<>(enumerable(Direction.class));
    public static final CodecHelper<Direction.Axis> AXIS = new CodecHelper<>(enumerable(Direction.Axis.class));
    public static final CodecHelper<Direction.Plane> PLANE = new CodecHelper<>(enumerable(Direction.Plane.class));
    public static final CodecHelper<MobCategory> MOB_CATEGORY = new CodecHelper<>(enumerable(MobCategory.class));
    public static final CodecHelper<EnchantmentCategory> ENCHANTMENT_CATEGORY = new CodecHelper<>(enumerable(EnchantmentCategory.class));
    public static final CodecHelper<DyeColor> DYE_COLOR = new CodecHelper<>(enumerable(DyeColor.class));
    public static final CodecHelper<SoundSource> SOUND_SOURCE = new CodecHelper<>(enumerable(SoundSource.class));
    public static final CodecHelper<ArmorMaterials> ARMOR_MATERIAL = new CodecHelper<>(enumerable(ArmorMaterials.class));
    public static final CodecHelper<Difficulty> DIFFICULTY = new CodecHelper<>(enumerable(Difficulty.class));
    public static final CodecHelper<EquipmentSlot> EQUIPMENT_SLOT = new CodecHelper<>(enumerable(EquipmentSlot.class));
    public static final CodecHelper<Mirror> MIRROR = new CodecHelper<>(enumerable(Mirror.class));
    public static final CodecHelper<Rotation> ROTATION = new CodecHelper<>(enumerable(Rotation.class));

    // MINECRAFT TYPES
    public static final CodecHelper<ResourceLocation> RESOURCE_LOCATION = new CodecHelper<>(ResourceLocation.CODEC);
    public static final CodecHelper<ItemStack> ITEM_STACK = new CodecHelper<>(ItemStack.CODEC);
    public static final CodecHelper<CompoundTag> COMPOUND_TAG = new CodecHelper<>(CompoundTag.CODEC);
    public static final CodecHelper<Component> TEXT = new CodecHelper<>(ExtraCodecs.COMPONENT);
    public static final CodecHelper<BlockPos> BLOCK_POS = new CodecHelper<>(BlockPos.CODEC);
    public static final CodecHelper<Ingredient> INGREDIENT = new CodecHelper<>(Ingredient.CODEC);
    public static final CodecHelper<Ingredient> INGREDIENT_NONEMPTY = new CodecHelper<>(Ingredient.CODEC_NONEMPTY);
    public static final CodecHelper<BlockState> BLOCK_STATE = new CodecHelper<>(Codec.mapPair(BLOCK.get().fieldOf("block"), Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties")).flatXmap(BookshelfCodecs::decodeBlockState, BookshelfCodecs::encodeBlockState).codec());
    public static final CodecHelper<AttributeModifier> ATTRIBUTE_MODIFIER = new CodecHelper<>(RecordCodecBuilder.create(instance -> instance.group(
            UUID.get("uuid", AttributeModifier::getId),
            STRING.get("name", AttributeModifier::getName),
            DOUBLE.get("amount", AttributeModifier::getAmount),
            ATTRIBUTE_OPERATION.get("operation", AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new)));
    public static final CodecHelper<MobEffectInstance> EFFECT_INSTANCE = new CodecHelper<>(RecordCodecBuilder.create(instance -> instance.group(
            MOB_EFFECT.get("effect", MobEffectInstance::getEffect),
            INT.get("duration", MobEffectInstance::getDuration, 20),
            INT.get("amplifier", MobEffectInstance::getAmplifier, 0),
            BOOLEAN.get("ambient", MobEffectInstance::isAmbient, false),
            BOOLEAN.get("visible", MobEffectInstance::isVisible, true),
            BOOLEAN.get("showIcon", MobEffectInstance::showIcon, true)
    ).apply(instance, MobEffectInstance::new)));
    public static final CodecHelper<MobEffectInstance> EFFECT_INSTANCE_WITH_HIDDEN = new CodecHelper<>(RecordCodecBuilder.create(instance -> instance.group(
            MOB_EFFECT.get("effect", MobEffectInstance::getEffect),
            INT.get("duration", MobEffectInstance::getDuration, 20),
            INT.get("amplifier", MobEffectInstance::getAmplifier, 0),
            BOOLEAN.get("ambient", MobEffectInstance::isAmbient, false),
            BOOLEAN.get("visible", MobEffectInstance::isVisible, true),
            BOOLEAN.get("showIcon", MobEffectInstance::showIcon, true),
            EFFECT_INSTANCE.getOptional("hiddenEffect", toWrite -> Optional.ofNullable(((AccessorMobEffectInstance) toWrite).bookshelf$getHiddenEffect()))
    ).apply(instance, (effect, duration, amplifier, ambient, visible, showIcon, hidden) -> new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hidden.orElse(null), effect.createFactorData()))));
    public static final CodecHelper<EnchantmentInstance> ENCHANTMENT_INSTANCE = new CodecHelper<>(RecordCodecBuilder.create(instance -> instance.group(
            ENCHANTMENT.get("enchantment", enchInst -> enchInst.enchantment),
            INT.get("level", enchInst -> enchInst.level, 1)
    ).apply(instance, EnchantmentInstance::new)));
    public static CodecHelper<Vector3f> VECTOR_3F = new CodecHelper<>(ExtraCodecs.VECTOR3F);

    // Bookshelf Types
    public static CodecHelper<ILoadCondition> LOAD_CONDITION = LoadConditions.CODEC_HELPER;
    public static CodecHelper<Sound> SOUND = new CodecHelper<>(Sound.CODEC);

    /**
     * Creates a codec for a registry that will error when no value can be found rather than passing a default value
     * like air.
     *
     * @param registry The registry to read values from.
     * @param <T>      The registry type.
     * @return A Codec for finding registry values by name.
     */
    public static <T> Codec<T> registry(Registry<T> registry) {

        return ResourceLocation.CODEC.flatXmap(
                id -> Optional.ofNullable(registry.containsKey(id) ? registry.get(id) : null).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + registry.key() + ": " + id)),
                value -> registry.getResourceKey(value).map(ResourceKey::location).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + registry.key() + ": " + value))
        );
    }

    /**
     * Creates a Codec that can flexibly read individual values as a list in addition to traditional lists.
     *
     * @param codec The codec for reading an individual value.
     * @param <T>   The type of value handled by the codec.
     * @return A Codec that can flexibly read individual values as a list in addition to traditional lists.
     */
    public static <T> Codec<List<T>> flexibleList(Codec<T> codec) {

        return Codec.either(codec.listOf(), codec).xmap(either -> either.map(Function.identity(), List::of), list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list));
    }

    /**
     * Creates a Codec that can flexibly read both individual values and arrays of values as a set.
     *
     * @param codec The Codec for reading an individual value.
     * @param <T>   The type of value handled by the codec.
     * @return A Codec that can flexibly read both individual values and arrays of values as a set.
     */
    public static <T> Codec<Set<T>> flexibleSet(Codec<T> codec) {

        return flexibleList(codec).xmap(LinkedHashSet::new, ArrayList::new);
    }

    /**
     * Creates a Codec that can flexibly read both individual values and arrays of values as an array.
     *
     * @param codec        The Codec for reading an individual value.
     * @param arrayBuilder A function that creates new arrays of the required type. The function is given the size of
     *                     the list.
     * @param <T>          The type of value handled by the codec.
     * @return A Codec that can flexibly read both individual values and arrays of values as an array.
     */
    public static <T> Codec<T[]> flexibleArray(Codec<T> codec, IntFunction<T[]> arrayBuilder) {

        return flexibleList(codec).xmap(list -> list.toArray(arrayBuilder.apply(list.size())), List::of);
    }

    /**
     * Creates a Codec that will use a fallback value if no other value is specified. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that the fallback value is provided by a supplier.
     *
     * @param codec            The base Codec to use.
     * @param name             The name of the field to read from.
     * @param fallbackSupplier A supplier that produces the default value. You should probably memoize this.
     * @param <T>              The type of value handled by the codec.
     * @return A Codec that will use a fallback value if the field is not specified.
     */
    public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier) {

        return fallback(codec, name, fallbackSupplier, true);
    }

    /**
     * Creates a Codec that handles optional values. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that it keeps the type as an Optional.
     *
     * @param codec         The base Codec to use.
     * @param name          The name of the field to read from.
     * @param fallback      The fallback optional value.
     * @param writesDefault Should the default value be written or left blank?
     * @param <T>           The type of the optional value handled by the coded.
     * @return A Codec that handles optional values.
     */
    public static <T> MapCodec<Optional<T>> optional(Codec<T> codec, String name, Optional<T> fallback, boolean writesDefault) {

        return Codec.optionalField(name, codec).xmap(o -> o.isPresent() ? o : fallback, a -> a.isEmpty() || (Objects.equals(a.get(), fallback.orElse(null)) && !writesDefault) ? Optional.empty() : a);
    }

    /**
     * Creates a Codec that can handle nullable values.
     *
     * @param codec     The base Codec to use.
     * @param fieldName The name of the field to read from.
     * @param <T>       The type of value handled by the codec.
     * @return A Codec that handles nullable values.
     */
    public static <T> MapCodec<T> nullable(Codec<T> codec, String fieldName) {

        return Codec.optionalField(fieldName, codec).xmap(optional -> optional.orElse(null), Optional::ofNullable);
    }

    /**
     * Creates a Codec that will use a fallback value if no other value is specified. This is different from
     * {@link Codec#optionalFieldOf(String, Object)} in that the fallback value is provided by a supplier. It also
     * allows you to control if the default value should be written when or left blank.
     *
     * @param codec            The base Codec to use.
     * @param name             The name of the field to read from.
     * @param fallbackSupplier A supplier that produces the default value. You should probably memoize this.
     * @param writesDefault    Should the default value be written or left blank?
     * @param <T>              The type of value handled by the codec.
     * @return A Codec that will use a fallback value if the field is not specified.
     */
    public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier, boolean writesDefault) {

        return Codec.optionalField(name, codec).xmap(value -> value.orElse(fallbackSupplier.get()), value -> {
            final T fallback = fallbackSupplier.get();
            return Objects.equals(value, fallback) && !writesDefault ? Optional.empty() : Optional.of(value);
        });
    }

    /**
     * Creates a Codec that handles enum values by using their enum constant names.
     * <br>
     * If the codec can not read an enum value from the provided name it will try again using an all uppercase version
     * of the input. This allows users to write values in all lowercase which may feel more natural for some users and
     * adds extra flexibility.
     * <br>
     * If the codec can not read any enum value from the provided name it will create an error. The error message will
     * try to help the user by recommending a nearby match and including all possible values.
     *
     * @param enumClass The class of the enum to get values for.
     * @param <T>       The type of the enum.
     * @return A codec that can read and write enum values using their enum constant name.
     */
    public static <T extends Enum<T>> Codec<T> enumerable(Class<T> enumClass) {

        final Map<String, T> enumValues = EnumUtils.getEnumMap(enumClass);

        final Function<String, T> fromString = name -> {

            T value = enumValues.get(name);

            if (value == null) {

                value = enumValues.get(name.toUpperCase(Locale.ROOT));
            }

            return value;
        };

        final UnaryOperator<String> errorMessage = name -> {

            final StringJoiner message = new StringJoiner(" ");
            message.add("Unable to find " + enumClass.getSimpleName() + " entry \"" + name + "\".");

            final Set<String> similarMatches = TextHelper.getPossibleMatches(name, enumValues.keySet(), 2);

            if (!similarMatches.isEmpty()) {
                message.add("Did you mean \"" + similarMatches.stream().findFirst().get() + "\"?");
            }

            message.add("Available Options are " + TextHelper.formatCollection(enumValues.keySet()));

            return message.toString();
        };

        return Codec.STRING.flatXmap(string -> Optionull.mapOrElse(fromString.apply(string), DataResult::success, () -> DataResult.error(() -> errorMessage.apply(string))), object -> DataResult.success(object.name()));
    }

    // INTERNAL HELPERS
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static DataResult<BlockState> decodeBlockState(Pair<Block, Optional<Map<String, String>>> props) {

        final Block block = props.getFirst();
        final Map<String, String> properties = props.getSecond().orElse(new HashMap<>());
        BlockState state = block.defaultBlockState();

        if (!properties.isEmpty()) {

            final StateDefinition<Block, BlockState> definition = block.getStateDefinition();

            for (Map.Entry<String, String> entry : properties.entrySet()) {

                final Property<? extends Comparable<?>> property = definition.getProperty(entry.getKey());

                if (property != null) {

                    final Optional<?> value = property.getValue(entry.getValue());

                    if (value.isPresent()) {

                        try {

                            state = state.setValue((Property) property, (Comparable) value.get());
                        }

                        catch (final Exception e) {

                            Constants.LOG.error("Failed to update state for block {} with valid value {}={}. The mod that adds this block may have a serious issue.", BuiltInRegistries.BLOCK.getKey(block), entry.getKey(), entry.getValue());
                            return DataResult.error(e::getMessage);
                        }
                    }

                    else {

                        return DataResult.error(() -> "\"" + entry.getValue() + "\" is not a valid value for property \"" + property.getName() + "\" on block \"" + BuiltInRegistries.BLOCK.getKey(block) + "\". Available values: " + property.getAllValues().map(propVal -> ((Property) property).getName(propVal.value())).collect(Collectors.joining()));
                    }
                }

                else {

                    return DataResult.error(() -> "The property \"" + entry.getKey() + "\" is not valid for block \"" + BuiltInRegistries.BLOCK.getKey(block) + "\". Available properties: " + definition.getProperties().stream().map(Property::getName).collect(Collectors.joining()));
                }
            }
        }

        return DataResult.success(state);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static DataResult<Pair<Block, Optional<Map<String, String>>>> encodeBlockState(BlockState state) {

        final Map<String, String> propertyMap = new HashMap<>();

        for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {

            propertyMap.put(entry.getKey().getName(), ((Property) entry.getKey()).getName(entry.getValue()));
        }

        return DataResult.success(new Pair<>(state.getBlock(), Optional.ofNullable(propertyMap.isEmpty() ? null : propertyMap)));
    }
}