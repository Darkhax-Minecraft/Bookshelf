package net.darkhax.bookshelf.api.data.bytebuf;

import net.darkhax.bookshelf.api.data.sound.Sound;
import net.darkhax.bookshelf.mixin.accessors.effect.AccessorMobEffectInstance;
import net.darkhax.bookshelf.mixin.accessors.entity.AccessorAttributeModifier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatType;
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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BookshelfByteBufs {

    // JAVA TYPES
    public static final ByteBufHelper<Boolean> BOOLEAN = new ByteBufHelper<>(FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
    public static final ByteBufHelper<Byte> BYTE = new ByteBufHelper<>(FriendlyByteBuf::readByte, (buf, val) -> buf.writeByte(val));
    public static final ByteBufHelper<Short> SHORT = new ByteBufHelper<>(FriendlyByteBuf::readShort, (buf, val) -> buf.writeShort(val));
    public static final ByteBufHelper<Integer> INT = new ByteBufHelper<>(FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
    public static final ByteBufHelper<Long> LONG = new ByteBufHelper<>(FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
    public static final ByteBufHelper<Float> FLOAT = new ByteBufHelper<>(FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
    public static final ByteBufHelper<Double> DOUBLE = new ByteBufHelper<>(FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
    public static final ByteBufHelper<String> STRING = new ByteBufHelper<>(FriendlyByteBuf::readUtf, FriendlyByteBuf::writeUtf);
    public static final ByteBufHelper<java.util.UUID> UUID = new ByteBufHelper<>(FriendlyByteBuf::readUUID, FriendlyByteBuf::writeUUID);

    // REGISTRIES
    public static final RegistryByteBufHelper<GameEvent> GAME_EVENT = new RegistryByteBufHelper<>(BuiltInRegistries.GAME_EVENT);
    public static final RegistryByteBufHelper<SoundEvent> SOUND_EVENT = new RegistryByteBufHelper<>(BuiltInRegistries.SOUND_EVENT);
    public static final RegistryByteBufHelper<Fluid> FLUID = new RegistryByteBufHelper<>(BuiltInRegistries.FLUID);
    public static final RegistryByteBufHelper<MobEffect> MOB_EFFECT = new RegistryByteBufHelper<>(BuiltInRegistries.MOB_EFFECT);
    public static final RegistryByteBufHelper<Block> BLOCK = new RegistryByteBufHelper<>(BuiltInRegistries.BLOCK);
    public static final RegistryByteBufHelper<Enchantment> ENCHANTMENT = new RegistryByteBufHelper<>(BuiltInRegistries.ENCHANTMENT);
    public static final RegistryByteBufHelper<EntityType<?>> ENTITY_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.ENTITY_TYPE);
    public static final RegistryByteBufHelper<Item> ITEM = new RegistryByteBufHelper<>(BuiltInRegistries.ITEM);
    public static final RegistryByteBufHelper<Potion> POTION = new RegistryByteBufHelper<>(BuiltInRegistries.POTION);
    public static final RegistryByteBufHelper<ParticleType<?>> PARTICLE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.PARTICLE_TYPE);
    public static final RegistryByteBufHelper<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final RegistryByteBufHelper<PaintingVariant> PAINTING_VARIANT = new RegistryByteBufHelper<>(BuiltInRegistries.PAINTING_VARIANT);
    public static final RegistryByteBufHelper<ResourceLocation> CUSTOM_STAT = new RegistryByteBufHelper<>(BuiltInRegistries.CUSTOM_STAT);
    public static final RegistryByteBufHelper<ChunkStatus> CHUNK_STATUS = new RegistryByteBufHelper<>(BuiltInRegistries.CHUNK_STATUS);
    public static final RegistryByteBufHelper<RuleTestType<?>> RULE_TEST = new RegistryByteBufHelper<>(BuiltInRegistries.RULE_TEST);
    public static final RegistryByteBufHelper<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = new RegistryByteBufHelper<>(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
    public static final RegistryByteBufHelper<PosRuleTestType<?>> POS_RULE_TEST = new RegistryByteBufHelper<>(BuiltInRegistries.POS_RULE_TEST);
    public static final RegistryByteBufHelper<MenuType<?>> MENU = new RegistryByteBufHelper<>(BuiltInRegistries.MENU);
    public static final RegistryByteBufHelper<RecipeType<?>> RECIPE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.RECIPE_TYPE);
    public static final RegistryByteBufHelper<RecipeSerializer<?>> RECIPE_SERIALIZER = new RegistryByteBufHelper<>(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final RegistryByteBufHelper<Attribute> ATTRIBUTE = new RegistryByteBufHelper<>(BuiltInRegistries.ATTRIBUTE);
    public static final RegistryByteBufHelper<PositionSourceType<?>> POSITION_SOURCE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.POSITION_SOURCE_TYPE);
    public static final RegistryByteBufHelper<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_INFO_CODEC_HELPER = new RegistryByteBufHelper<>(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
    public static final RegistryByteBufHelper<StatType<?>> STAT_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.STAT_TYPE);
    public static final RegistryByteBufHelper<VillagerType> VILLAGER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.VILLAGER_TYPE);
    public static final RegistryByteBufHelper<VillagerProfession> VILLAGER_PROFESSION = new RegistryByteBufHelper<>(BuiltInRegistries.VILLAGER_PROFESSION);
    public static final RegistryByteBufHelper<PoiType> POINT_OF_INTEREST_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.POINT_OF_INTEREST_TYPE);
    public static final RegistryByteBufHelper<MemoryModuleType<?>> MEMORY_MODULE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.MEMORY_MODULE_TYPE);
    public static final RegistryByteBufHelper<SensorType<?>> SENSOR_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.SENSOR_TYPE);
    public static final RegistryByteBufHelper<Schedule> SCHEDULE = new RegistryByteBufHelper<>(BuiltInRegistries.SCHEDULE);
    public static final RegistryByteBufHelper<Activity> ACTIVITY = new RegistryByteBufHelper<>(BuiltInRegistries.ACTIVITY);
    public static final RegistryByteBufHelper<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
    public static final RegistryByteBufHelper<LootItemFunctionType> LOOT_FUNCTION_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_FUNCTION_TYPE);
    public static final RegistryByteBufHelper<LootItemConditionType> LOOT_CONDITION_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_CONDITION_TYPE);
    public static final RegistryByteBufHelper<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.FLOAT_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<IntProviderType<?>> INT_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.INT_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.BLOCK_PREDICATE_TYPE);
    public static final RegistryByteBufHelper<WorldCarver<?>> CARVER = new RegistryByteBufHelper<>(BuiltInRegistries.CARVER);
    public static final RegistryByteBufHelper<Feature<?>> FEATURE = new RegistryByteBufHelper<>(BuiltInRegistries.FEATURE);
    public static final RegistryByteBufHelper<StructurePlacementType<?>> STRUCTURE_PLACEMENT = new RegistryByteBufHelper<>(BuiltInRegistries.STRUCTURE_PLACEMENT);
    public static final RegistryByteBufHelper<StructurePieceType> STRUCTURE_PIECE = new RegistryByteBufHelper<>(BuiltInRegistries.STRUCTURE_PIECE);
    public static final RegistryByteBufHelper<StructureType<?>> STRUCTURE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.STRUCTURE_TYPE);
    public static final RegistryByteBufHelper<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
    public static final RegistryByteBufHelper<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
    public static final RegistryByteBufHelper<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.FOLIAGE_PLACER_TYPE);
    public static final RegistryByteBufHelper<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.TRUNK_PLACER_TYPE);
    public static final RegistryByteBufHelper<RootPlacerType<?>> ROOT_PLACER_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.ROOT_PLACER_TYPE);
    public static final RegistryByteBufHelper<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.TREE_DECORATOR_TYPE);
    public static final RegistryByteBufHelper<FeatureSizeType<?>> FEATURE_SIZE_TYPE = new RegistryByteBufHelper<>(BuiltInRegistries.FEATURE_SIZE_TYPE);
    public static final RegistryByteBufHelper<StructureProcessorType<?>> STRUCTURE_PROCESSOR = new RegistryByteBufHelper<>(BuiltInRegistries.STRUCTURE_PROCESSOR);
    public static final RegistryByteBufHelper<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = new RegistryByteBufHelper<>(BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
    public static final RegistryByteBufHelper<CatVariant> CAT_VARIANT = new RegistryByteBufHelper<>(BuiltInRegistries.CAT_VARIANT);
    public static final RegistryByteBufHelper<FrogVariant> FROG_VARIANT = new RegistryByteBufHelper<>(BuiltInRegistries.FROG_VARIANT);
    public static final RegistryByteBufHelper<BannerPattern> BANNER_PATTERN = new RegistryByteBufHelper<>(BuiltInRegistries.BANNER_PATTERN);
    public static final RegistryByteBufHelper<Instrument> INSTRUMENT = new RegistryByteBufHelper<>(BuiltInRegistries.INSTRUMENT);
    public static final RegistryByteBufHelper<String> DECORATED_POT_PATTERNS = new RegistryByteBufHelper<>(BuiltInRegistries.DECORATED_POT_PATTERNS);
    public static final RegistryByteBufHelper<CreativeModeTab> CREATIVE_MODE_TAB = new RegistryByteBufHelper<>(BuiltInRegistries.CREATIVE_MODE_TAB);

    // ENUMS
    public static final ByteBufHelper<Rarity> ITEM_RARITY = enumerable(Rarity.class);
    public static final ByteBufHelper<Enchantment.Rarity> ENCHANTMENT_RARITY = enumerable(Enchantment.Rarity.class);
    public static final ByteBufHelper<AttributeModifier.Operation> ATTRIBUTE_OPERATION = enumerable(AttributeModifier.Operation.class);
    public static final ByteBufHelper<Direction> DIRECTION = enumerable(Direction.class);
    public static final ByteBufHelper<Direction.Axis> AXIS = enumerable(Direction.Axis.class);
    public static final ByteBufHelper<Direction.Plane> PLANE = enumerable(Direction.Plane.class);
    public static final ByteBufHelper<MobCategory> MOB_CATEGORY = enumerable(MobCategory.class);
    public static final ByteBufHelper<EnchantmentCategory> ENCHANTMENT_CATEGORY = enumerable(EnchantmentCategory.class);
    public static final ByteBufHelper<DyeColor> DYE_COLOR = enumerable(DyeColor.class);
    public static final ByteBufHelper<SoundSource> SOUND_SOURCE = enumerable(SoundSource.class);
    public static final ByteBufHelper<ArmorMaterials> ARMOR_MATERIAL = enumerable(ArmorMaterials.class);
    public static final ByteBufHelper<Difficulty> DIFFICULTY = enumerable(Difficulty.class);
    public static final ByteBufHelper<EquipmentSlot> EQUIPMENT_SLOT = enumerable(EquipmentSlot.class);
    public static final ByteBufHelper<Mirror> MIRROR = enumerable(Mirror.class);
    public static final ByteBufHelper<Rotation> ROTATION = enumerable(Rotation.class);

    // MINECRAFT TYPES
    public static final ByteBufHelper<ResourceLocation> RESOURCE_LOCATION = new ByteBufHelper<>(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);
    public static final ByteBufHelper<ItemStack> ITEM_STACK = new ByteBufHelper<>(FriendlyByteBuf::readItem, FriendlyByteBuf::writeItem);
    public static final ByteBufHelper<CompoundTag> COMPOUND_TAG = new ByteBufHelper<>(FriendlyByteBuf::readNbt, FriendlyByteBuf::writeNbt);
    public static final ByteBufHelper<Component> TEXT = new ByteBufHelper<>(FriendlyByteBuf::readComponent, FriendlyByteBuf::writeComponent);
    public static final ByteBufHelper<BlockPos> BLOCK_POS = new ByteBufHelper<>(FriendlyByteBuf::readBlockPos, FriendlyByteBuf::writeBlockPos);
    public static final ByteBufHelper<Ingredient> INGREDIENT = new ByteBufHelper<>(Ingredient::fromNetwork, (buf, val) -> val.toNetwork(buf));
    public static final ByteBufHelper<BlockState> BLOCK_STATE = new ByteBufHelper<>(buf -> Block.stateById(buf.readInt()), (buf, val) -> buf.writeInt(Block.getId(val)));
    public static final ByteBufHelper<AttributeModifier> ATTRIBUTE_MODIFIER = new ByteBufHelper<>(
            buf -> {
                final java.util.UUID uuid = UUID.read(buf);
                final String name = STRING.read(buf);
                final double amount = DOUBLE.read(buf);
                final AttributeModifier.Operation operation = ATTRIBUTE_OPERATION.read(buf);
                return new AttributeModifier(uuid, name, amount, operation);
            },
            (buf, val) -> {
                UUID.write(buf, val.getId());
                STRING.write(buf, ((AccessorAttributeModifier) val).bookshelf$getName());
                DOUBLE.write(buf, val.getAmount());
                ATTRIBUTE_OPERATION.write(buf, val.getOperation());
            }
    );
    public static final ByteBufHelper<MobEffectInstance> EFFECT_INSTANCE = new ByteBufHelper<>(
            buf -> {
                final MobEffect effect = MOB_EFFECT.read(buf);
                final int duration = INT.read(buf);
                final int amplifier = INT.read(buf);
                final boolean ambient = BOOLEAN.read(buf);
                final boolean visible = BOOLEAN.read(buf);
                final boolean showIcon = BOOLEAN.read(buf);
                final MobEffectInstance hiddenEffect = mobEffect().get().readNullable(buf);
                return new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect, effect.createFactorData());
            },
            (buf, val) -> {
                MOB_EFFECT.write(buf, val.getEffect());
                INT.write(buf, val.getDuration());
                INT.write(buf, val.getAmplifier());
                BOOLEAN.write(buf, val.isAmbient());
                BOOLEAN.write(buf, val.isVisible());
                BOOLEAN.write(buf, val.showIcon());
                mobEffect().get().writeOptional(buf, Optional.ofNullable(((AccessorMobEffectInstance) val).bookshelf$getHiddenEffect()));
            }
    );
    public static final ByteBufHelper<EnchantmentInstance> ENCHANTMENT_INSTANCE = new ByteBufHelper<>(
            buf -> {
                final Enchantment enchantment = ENCHANTMENT.read(buf);
                final int level = INT.read(buf);
                return new EnchantmentInstance(enchantment, level);
            },
            (buf, val) -> {
                ENCHANTMENT.write(buf, val.enchantment);
                INT.write(buf, val.level);
            }
    );
    public static ByteBufHelper<Vector3f> VECTOR_3F = new ByteBufHelper<>(
            buf -> new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()),
            (buf, val) -> {
                buf.writeFloat(val.x());
                buf.writeFloat(val.y());
                buf.writeFloat(val.z());
            }
    );

    // Bookshelf Types
    public static ByteBufHelper<Sound> SOUND = new ByteBufHelper<>(Sound::fromByteBuf, Sound::toByteBuf);

    public static <T extends Enum<T>> ByteBufHelper<T> enumerable(Class<T> enumClass) {

        final Map<String, T> enumValues = EnumUtils.getEnumMap(enumClass);
        return new ByteBufHelper<>(buf -> enumValues.get(buf.readUtf()), (buf, val) -> buf.writeUtf(val.name()));
    }

    private static Supplier<ByteBufHelper<MobEffectInstance>> mobEffect() {

        return () -> BookshelfByteBufs.EFFECT_INSTANCE;
    }
}