package net.darkhax.bookshelf.common.api.data.codecs.map;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.util.TextHelper;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.Optionull;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.numbers.NumberFormatType;
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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
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
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
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
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
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
public class MapCodecs {

    // JAVA TYPES
    public static final MapCodecHelper<Boolean> BOOLEAN = new MapCodecHelper<>(Codec.BOOL);
    public static final MapCodecHelper<Byte> BYTE = new MapCodecHelper<>(Codec.BYTE);
    public static final MapCodecHelper<Short> SHORT = new MapCodecHelper<>(Codec.SHORT);
    public static final MapCodecHelper<Integer> INT = new MapCodecHelper<>(Codec.INT);
    public static final MapCodecHelper<Float> FLOAT = new MapCodecHelper<>(Codec.FLOAT);
    public static final MapCodecHelper<Long> LONG = new MapCodecHelper<>(Codec.LONG);
    public static final MapCodecHelper<Double> DOUBLE = new MapCodecHelper<>(Codec.DOUBLE);
    public static final MapCodecHelper<String> STRING = new MapCodecHelper<>(Codec.STRING);
    public static final MapCodecHelper<UUID> UUID = new MapCodecHelper<>(UUIDUtil.CODEC);

    // REGISTRIES
    public static final MapCodecHelper<Holder<GameEvent>> GAME_EVENT = RegistryMapCodecHelper.create(BuiltInRegistries.GAME_EVENT);
    public static final MapCodecHelper<Holder<SoundEvent>> SOUND_EVENT = RegistryMapCodecHelper.create(BuiltInRegistries.SOUND_EVENT);
    public static final MapCodecHelper<Holder<Fluid>> FLUID = RegistryMapCodecHelper.create(BuiltInRegistries.FLUID);
    public static final MapCodecHelper<Holder<MobEffect>> MOB_EFFECT = RegistryMapCodecHelper.create(BuiltInRegistries.MOB_EFFECT);
    public static final MapCodecHelper<Holder<Block>> BLOCK = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK);
    public static final MapCodecHelper<Holder<EntityType<?>>> ENTITY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENTITY_TYPE);
    public static final MapCodecHelper<Holder<Item>> ITEM = RegistryMapCodecHelper.create(BuiltInRegistries.ITEM);
    public static final MapCodecHelper<Holder<Potion>> POTION = RegistryMapCodecHelper.create(BuiltInRegistries.POTION);
    public static final MapCodecHelper<Holder<ParticleType<?>>> PARTICLE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.PARTICLE_TYPE);
    public static final MapCodecHelper<Holder<BlockEntityType<?>>> BLOCK_ENTITY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final MapCodecHelper<Holder<ResourceLocation>> CUSTOM_STAT = RegistryMapCodecHelper.create(BuiltInRegistries.CUSTOM_STAT);
    public static final MapCodecHelper<Holder<ChunkStatus>> CHUNK_STATUS = RegistryMapCodecHelper.create(BuiltInRegistries.CHUNK_STATUS);
    public static final MapCodecHelper<Holder<RuleTestType<?>>> RULE_TEST = RegistryMapCodecHelper.create(BuiltInRegistries.RULE_TEST);
    public static final MapCodecHelper<Holder<RuleBlockEntityModifierType<?>>> RULE_BLOCK_ENTITY_MODIFIER = RegistryMapCodecHelper.create(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
    public static final MapCodecHelper<Holder<PosRuleTestType<?>>> POS_RULE_TEST = RegistryMapCodecHelper.create(BuiltInRegistries.POS_RULE_TEST);
    public static final MapCodecHelper<Holder<MenuType<?>>> MENU = RegistryMapCodecHelper.create(BuiltInRegistries.MENU);
    public static final MapCodecHelper<Holder<RecipeType<?>>> RECIPE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.RECIPE_TYPE);
    public static final MapCodecHelper<Holder<RecipeSerializer<?>>> RECIPE_SERIALIZER = RegistryMapCodecHelper.create(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final MapCodecHelper<Holder<Attribute>> ATTRIBUTE = RegistryMapCodecHelper.create(BuiltInRegistries.ATTRIBUTE);
    public static final MapCodecHelper<Holder<PositionSourceType<?>>> POSITION_SOURCE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.POSITION_SOURCE_TYPE);
    public static final MapCodecHelper<Holder<ArgumentTypeInfo<?, ?>>> COMMAND_ARGUMENT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
    public static final MapCodecHelper<Holder<StatType<?>>> STAT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.STAT_TYPE);
    public static final MapCodecHelper<Holder<VillagerType>> VILLAGER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.VILLAGER_TYPE);
    public static final MapCodecHelper<Holder<VillagerProfession>> VILLAGER_PROFESSION = RegistryMapCodecHelper.create(BuiltInRegistries.VILLAGER_PROFESSION);
    public static final MapCodecHelper<Holder<PoiType>> POINT_OF_INTEREST_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE);
    public static final MapCodecHelper<Holder<MemoryModuleType<?>>> MEMORY_MODULE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.MEMORY_MODULE_TYPE);
    public static final MapCodecHelper<Holder<SensorType<?>>> SENSOR_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.SENSOR_TYPE);
    public static final MapCodecHelper<Holder<Schedule>> SCHEDULE = RegistryMapCodecHelper.create(BuiltInRegistries.SCHEDULE);
    public static final MapCodecHelper<Holder<Activity>> ACTIVITY = RegistryMapCodecHelper.create(BuiltInRegistries.ACTIVITY);
    public static final MapCodecHelper<Holder<LootPoolEntryType>> LOOT_POOL_ENTRY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
    public static final MapCodecHelper<Holder<LootItemFunctionType<?>>> LOOT_FUNCTION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_FUNCTION_TYPE);
    public static final MapCodecHelper<Holder<LootItemConditionType>> LOOT_CONDITION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_CONDITION_TYPE);
    public static final MapCodecHelper<Holder<LootNumberProviderType>> LOOT_NUMBER_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<LootNbtProviderType>> LOOT_NBT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<LootScoreProviderType>> LOOT_SCORE_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<FloatProviderType<?>>> FLOAT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FLOAT_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<IntProviderType<?>>> INT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.INT_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<HeightProviderType<?>>> HEIGHT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<BlockPredicateType<?>>> BLOCK_PREDICATE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK_PREDICATE_TYPE);
    public static final MapCodecHelper<Holder<WorldCarver<?>>> CARVER = RegistryMapCodecHelper.create(BuiltInRegistries.CARVER);
    public static final MapCodecHelper<Holder<Feature<?>>> FEATURE = RegistryMapCodecHelper.create(BuiltInRegistries.FEATURE);
    public static final MapCodecHelper<Holder<StructurePlacementType<?>>> STRUCTURE_PLACEMENT = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PLACEMENT);
    public static final MapCodecHelper<Holder<StructurePieceType>> STRUCTURE_PIECE = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PIECE);
    public static final MapCodecHelper<Holder<StructureType<?>>> STRUCTURE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_TYPE);
    public static final MapCodecHelper<Holder<PlacementModifierType<?>>> PLACEMENT_MODIFIER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
    public static final MapCodecHelper<Holder<BlockStateProviderType<?>>> BLOCKSTATE_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
    public static final MapCodecHelper<Holder<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE);
    public static final MapCodecHelper<Holder<TrunkPlacerType<?>>> TRUNK_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.TRUNK_PLACER_TYPE);
    public static final MapCodecHelper<Holder<RootPlacerType<?>>> ROOT_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ROOT_PLACER_TYPE);
    public static final MapCodecHelper<Holder<TreeDecoratorType<?>>> TREE_DECORATOR_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.TREE_DECORATOR_TYPE);
    public static final MapCodecHelper<Holder<FeatureSizeType<?>>> FEATURE_SIZE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FEATURE_SIZE_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends BiomeSource>>> BIOME_SOURCE = RegistryMapCodecHelper.create(BuiltInRegistries.BIOME_SOURCE);
    public static final MapCodecHelper<Holder<MapCodec<? extends ChunkGenerator>>> CHUNK_GENERATOR = RegistryMapCodecHelper.create(BuiltInRegistries.CHUNK_GENERATOR);
    public static final MapCodecHelper<Holder<MapCodec<? extends SurfaceRules.ConditionSource>>> MATERIAL_CONDITION = RegistryMapCodecHelper.create(BuiltInRegistries.MATERIAL_CONDITION);
    public static final MapCodecHelper<Holder<MapCodec<? extends SurfaceRules.RuleSource>>> MATERIAL_RULE = RegistryMapCodecHelper.create(BuiltInRegistries.MATERIAL_RULE);
    public static final MapCodecHelper<Holder<MapCodec<? extends DensityFunction>>> DENSITY_FUNCTION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends Block>>> BLOCK_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK_TYPE);
    public static final MapCodecHelper<Holder<StructureProcessorType<?>>> STRUCTURE_PROCESSOR = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PROCESSOR);
    public static final MapCodecHelper<Holder<StructurePoolElementType<?>>> STRUCTURE_POOL_ELEMENT = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
    public static final MapCodecHelper<Holder<MapCodec<? extends PoolAliasBinding>>> POOL_ALIAS_BINDING_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.POOL_ALIAS_BINDING_TYPE);
    public static final MapCodecHelper<Holder<CatVariant>> CAT_VARIANT = RegistryMapCodecHelper.create(BuiltInRegistries.CAT_VARIANT);
    public static final MapCodecHelper<Holder<FrogVariant>> FROG_VARIANT = RegistryMapCodecHelper.create(BuiltInRegistries.FROG_VARIANT);
    public static final MapCodecHelper<Holder<Instrument>> INSTRUMENT = RegistryMapCodecHelper.create(BuiltInRegistries.INSTRUMENT);
    public static final MapCodecHelper<Holder<DecoratedPotPattern>> DECORATED_POT_PATTERN = RegistryMapCodecHelper.create(BuiltInRegistries.DECORATED_POT_PATTERN);
    public static final MapCodecHelper<Holder<CreativeModeTab>> CREATIVE_MODE_TAB = RegistryMapCodecHelper.create(BuiltInRegistries.CREATIVE_MODE_TAB);
    public static final MapCodecHelper<Holder<CriterionTrigger<?>>> TRIGGER_TYPES = RegistryMapCodecHelper.create(BuiltInRegistries.TRIGGER_TYPES);
    public static final MapCodecHelper<Holder<NumberFormatType<?>>> NUMBER_FORMAT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.NUMBER_FORMAT_TYPE);
    public static final MapCodecHelper<Holder<ArmorMaterial>> ARMOR_MATERIAL = RegistryMapCodecHelper.create(BuiltInRegistries.ARMOR_MATERIAL);
    public static final MapCodecHelper<Holder<DataComponentType<?>>> DATA_COMPONENT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.DATA_COMPONENT_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends EntitySubPredicate>>> ENTITY_SUB_PREDICATE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE);
    public static final MapCodecHelper<Holder<ItemSubPredicate.Type<?>>> ITEM_SUB_PREDICATE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE);
    public static final MapCodecHelper<Holder<MapDecorationType>> MAP_DECORATION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.MAP_DECORATION_TYPE);
    public static final MapCodecHelper<Holder<DataComponentType<?>>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends LevelBasedValue>>> ENCHANTMENT_LEVEL_BASED_VALUE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends EnchantmentEntityEffect>>> ENCHANTMENT_ENTITY_EFFECT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends EnchantmentLocationBasedEffect>>> ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends EnchantmentValueEffect>>> ENCHANTMENT_VALUE_EFFECT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_VALUE_EFFECT_TYPE);
    public static final MapCodecHelper<Holder<MapCodec<? extends EnchantmentProvider>>> ENCHANTMENT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE);

    // ENUMS
    public static final MapCodecHelper<Rarity> ITEM_RARITY = new MapCodecHelper<>(enumerable(Rarity.class));
    public static final MapCodecHelper<AttributeModifier.Operation> ATTRIBUTE_OPERATION = new MapCodecHelper<>(AttributeModifier.Operation.CODEC);
    public static final MapCodecHelper<Direction> DIRECTION = new MapCodecHelper<>(enumerable(Direction.class));
    public static final MapCodecHelper<Direction.Axis> AXIS = new MapCodecHelper<>(enumerable(Direction.Axis.class));
    public static final MapCodecHelper<Direction.Plane> PLANE = new MapCodecHelper<>(enumerable(Direction.Plane.class));
    public static final MapCodecHelper<MobCategory> MOB_CATEGORY = new MapCodecHelper<>(enumerable(MobCategory.class));
    public static final MapCodecHelper<DyeColor> DYE_COLOR = new MapCodecHelper<>(enumerable(DyeColor.class));
    public static final MapCodecHelper<SoundSource> SOUND_SOURCE = new MapCodecHelper<>(enumerable(SoundSource.class));
    public static final MapCodecHelper<Difficulty> DIFFICULTY = new MapCodecHelper<>(enumerable(Difficulty.class));
    public static final MapCodecHelper<EquipmentSlot> EQUIPMENT_SLOT = new MapCodecHelper<>(enumerable(EquipmentSlot.class));
    public static final MapCodecHelper<Mirror> MIRROR = new MapCodecHelper<>(enumerable(Mirror.class));
    public static final MapCodecHelper<Rotation> ROTATION = new MapCodecHelper<>(enumerable(Rotation.class));

    // MINECRAFT TYPES
    public static final MapCodecHelper<ResourceLocation> RESOURCE_LOCATION = new MapCodecHelper<>(ResourceLocation.CODEC);
    public static final MapCodecHelper<CompoundTag> COMPOUND_TAG = new MapCodecHelper<>(CompoundTag.CODEC);
    public static final MapCodecHelper<ItemStack> ITEM_STACK = new MapCodecHelper<>(ItemStack.CODEC);
    public static final MapCodecHelper<ItemStack> ITEM_STACK_STRICT = new MapCodecHelper<>(ItemStack.STRICT_CODEC);
    public static final MapCodecHelper<Component> TEXT = new MapCodecHelper<>(ComponentSerialization.CODEC);
    public static final MapCodecHelper<BlockPos> BLOCK_POS = new MapCodecHelper<>(BlockPos.CODEC);
    public static final MapCodecHelper<Ingredient> INGREDIENT = new MapCodecHelper<>(Ingredient.CODEC);
    public static final MapCodecHelper<Ingredient> INGREDIENT_NONEMPTY = new MapCodecHelper<>(Ingredient.CODEC_NONEMPTY);
    public static final MapCodec<BlockState> BLOCK_STATE_MAP_CODEC = Codec.mapPair(BLOCK.get().fieldOf("block"), Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties")).flatXmap(MapCodecs::decodeBlockState, MapCodecs::encodeBlockState);
    public static final MapCodecHelper<BlockState> BLOCK_STATE = new MapCodecHelper<>(BLOCK_STATE_MAP_CODEC.codec());
    public static final MapCodecHelper<AttributeModifier> ATTRIBUTE_MODIFIER = new MapCodecHelper<>(AttributeModifier.CODEC);
    public static final MapCodecHelper<MobEffectInstance> EFFECT_INSTANCE = new MapCodecHelper<>(MobEffectInstance.CODEC);
    public static final MapCodecHelper<Vector3f> VECTOR_3F = new MapCodecHelper<>(ExtraCodecs.VECTOR3F);

    // Bookshelf Types
    public static final MapCodecHelper<ILoadCondition> LOAD_CONDITION = LoadConditions.CODEC_HELPER;

    /**
     * Creates a Codec that can flexibly read individual values as a list in addition to traditional lists.
     *
     * @param codec The codec for reading an individual value.
     * @param <T>   The type of value handled by the codec.
     * @return A Codec that can flexibly read individual values as a list in addition to traditional lists.
     */
    public static <T> Codec<List<T>> flexibleList(Codec<T> codec) {

        return Codec.either(codec.listOf(), codec).xmap(either -> either.map(Function.identity(), List::of), list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list));
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

        return Codec.optionalField(name, codec, false).xmap(o -> o.isPresent() ? o : fallback, a -> a.isEmpty() || (Objects.equals(a.get(), fallback.orElse(null)) && !writesDefault) ? Optional.empty() : a);
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

        return Codec.optionalField(fieldName, codec, false).xmap(optional -> optional.orElse(null), Optional::ofNullable);
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

        return Codec.optionalField(name, codec, false).xmap(value -> value.orElse(fallbackSupplier.get()), value -> {
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
    private static DataResult<BlockState> decodeBlockState(Pair<Holder<Block>, Optional<Map<String, String>>> props) {

        final Block block = props.getFirst().value();
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
    private static DataResult<Pair<Holder<Block>, Optional<Map<String, String>>>> encodeBlockState(BlockState state) {

        final Map<String, String> propertyMap = new HashMap<>();

        for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {

            propertyMap.put(entry.getKey().getName(), ((Property) entry.getKey()).getName(entry.getValue()));
        }

        return DataResult.success(new Pair<>(state.getBlock().builtInRegistryHolder(), Optional.ofNullable(propertyMap.isEmpty() ? null : propertyMap)));
    }
}