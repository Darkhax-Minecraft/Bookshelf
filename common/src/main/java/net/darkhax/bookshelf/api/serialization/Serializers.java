package net.darkhax.bookshelf.api.serialization;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.data.sound.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.material.Fluid;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.UUID;

@Deprecated
public final class Serializers {

    // JAVA TYPES
    public static final ISerializer<Boolean> BOOLEAN = SerializerBoolean.SERIALIZER;
    public static final ISerializer<Byte> BYTE = SerializerByte.SERIALIZER;
    public static final ISerializer<Short> SHORT = SerializerShort.SERIALIZER;
    public static final ISerializer<Integer> INT = SerializerInteger.SERIALIZER;
    public static final ISerializer<Long> LONG = SerializerLong.SERIALIZER;
    public static final ISerializer<Float> FLOAT = SerializerFloat.SERIALIZER;
    public static final ISerializer<Double> DOUBLE = SerializerDouble.SERIALIZER;
    public static final ISerializer<String> STRING = SerializerString.SERIALIZER;
    public static final ISerializer<UUID> UUID = SerializerUUID.SERIALIZER;

    // MINECRAFT TYPES
    public static final ISerializer<ResourceLocation> RESOURCE_LOCATION = SerializerResourceLocation.SERIALIZER;
    public static final ISerializer<ItemStack> ITEM_STACK = SerializerItemStack.SERIALIZER;
    public static final ISerializer<CompoundTag> COMPOUND_TAG = SerializerCompoundTag.SERIALIZER;
    public static final ISerializer<Component> TEXT = SerializerText.SERIALIZER;
    public static final ISerializer<BlockPos> BLOCK_POS = SerializerBlockPos.SERIALIZER;
    public static final ISerializer<Ingredient> INGREDIENT = SerializerIngredient.SERIALIZER;
    public static final ISerializer<BlockState> BLOCK_STATE = SerializerBlockState.SERIALIZER;
    public static final ISerializer<AttributeModifier> ATTRIBUTE_MODIFIER = SerializerAttributeModifier.SERIALIZER;
    public static final ISerializer<MobEffectInstance> EFFECT_INSTANCE = new SerializerEffectInstance();
    public static final ISerializer<EnchantmentInstance> ENCHANTMENT_INSTANCE = SerializerEnchantmentInstance.SERIALIZER;
    public static final ISerializer<Vector3f> VECTOR_3F = SerializerVector3f.SERIALIZER;
    public static final ISerializer<Vector4f> VECTOR_4F = SerializerVector4f.SERIALIZER;
    public static final ISerializer<Sound> SOUND = Sound.SERIALIZER;
    public static final ISerializer<StructurePoolElement> STRUCTURE_POOL_ELEMENT = new SerializerCodec<>(StructurePoolElement.CODEC);

    // ENUMS
    public static final ISerializer<Rarity> ITEM_RARITY = new SerializerEnum<>(Rarity.class);
    public static final ISerializer<Enchantment.Rarity> ENCHANTMENT_RARITY = new SerializerEnum<>(Enchantment.Rarity.class);
    public static final ISerializer<AttributeModifier.Operation> ATTRIBUTE_OPERATION = new SerializerEnum<>(AttributeModifier.Operation.class);
    public static final ISerializer<Direction> DIRECTION = new SerializerEnum<>(Direction.class);
    public static final ISerializer<Direction.Axis> AXIS = new SerializerEnum<>(Direction.Axis.class);
    public static final ISerializer<Direction.Plane> PLANE = new SerializerEnum<>(Direction.Plane.class);
    public static final ISerializer<MobCategory> MOB_CATEGORY = new SerializerEnum<>(MobCategory.class);
    public static final ISerializer<EnchantmentCategory> ENCHANTMENT_CATEGORY = new SerializerEnum<>(EnchantmentCategory.class);
    public static final ISerializer<DyeColor> DYE_COLOR = new SerializerEnum<>(DyeColor.class);
    public static final ISerializer<SoundSource> SOUND_CATEGORY = new SerializerEnum<>(SoundSource.class);

    // REGISTRY TYPES
    public static final ISerializer<Block> BLOCK = new SerializerRegistryEntry<>(BuiltInRegistries.BLOCK);
    public static final ISerializer<Item> ITEM = new SerializerRegistryEntry<>(BuiltInRegistries.ITEM);
    public static final ISerializer<BannerPattern> BANNER_PATTERN = new SerializerRegistryEntry<>(BuiltInRegistries.BANNER_PATTERN);
    public static final ISerializer<Enchantment> ENCHANTMENT = new SerializerRegistryEntry<>(BuiltInRegistries.ENCHANTMENT);
    public static final ISerializer<PaintingVariant> PAINTING = new SerializerRegistryEntry<>(BuiltInRegistries.PAINTING_VARIANT);
    public static final ISerializer<MobEffect> MOB_EFFECT = new SerializerRegistryEntry<>(BuiltInRegistries.MOB_EFFECT);
    public static final ISerializer<Potion> POTION = new SerializerRegistryEntry<>(BuiltInRegistries.POTION);
    public static final ISerializer<Attribute> ATTRIBUTE = new SerializerRegistryEntry<>(BuiltInRegistries.ATTRIBUTE);
    public static final ISerializer<VillagerProfession> VILLAGER_PROFESSION = new SerializerRegistryEntry<>(BuiltInRegistries.VILLAGER_PROFESSION);
    public static final ISerializer<VillagerType> VILLAGER_TYPE = new SerializerRegistryEntry<>(BuiltInRegistries.VILLAGER_TYPE);
    public static final ISerializer<SoundEvent> SOUND_EVENT = new SerializerRegistryEntry<>(BuiltInRegistries.SOUND_EVENT);
    public static final ISerializer<MenuType<?>> MENU = new SerializerRegistryEntry<>(BuiltInRegistries.MENU);
    public static final ISerializer<ParticleType<?>> PARTICLE = new SerializerRegistryEntry<>(BuiltInRegistries.PARTICLE_TYPE);
    public static final ISerializer<EntityType<?>> ENTITY = new SerializerRegistryEntry<>(BuiltInRegistries.ENTITY_TYPE);
    public static final ISerializer<BlockEntityType<?>> BLOCK_ENTITY = new SerializerRegistryEntry<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final ISerializer<GameEvent> GAME_EVENT = new SerializerRegistryEntry<>(BuiltInRegistries.GAME_EVENT);

    // Tag Types
    public static final ISerializer<TagKey<Block>> BLOCK_TAG = new SerializerTagKey<>(Services.TAGS::blockTag);
    public static final ISerializer<TagKey<Item>> ITEM_TAG = new SerializerTagKey<>(Services.TAGS::itemTag);
    public static final ISerializer<TagKey<BannerPattern>> BANNER_PATTERN_TAG = new SerializerTagKey<>(Services.TAGS::bannerPatternTag);
    public static final ISerializer<TagKey<Enchantment>> ENCHANTMENT_TAG = new SerializerTagKey<>(Services.TAGS::enchantmentTag);
    public static final ISerializer<TagKey<PaintingVariant>> MOTIVE_TAG = new SerializerTagKey<>(Services.TAGS::paintingTag);
    public static final ISerializer<TagKey<MobEffect>> MOB_EFFECT_TAG = new SerializerTagKey<>(Services.TAGS::effectTag);
    public static final ISerializer<TagKey<Potion>> POTION_TAG = new SerializerTagKey<>(Services.TAGS::potionTag);
    public static final ISerializer<TagKey<Attribute>> ATTRIBUTE_TAG = new SerializerTagKey<>(Services.TAGS::attributeTag);
    public static final ISerializer<TagKey<VillagerProfession>> VILLAGER_PROFESSION_TAG = new SerializerTagKey<>(Services.TAGS::villagerProfessionTag);
    public static final ISerializer<TagKey<VillagerType>> VILLAGER_TYPE_TAG = new SerializerTagKey<>(Services.TAGS::villagerTypeTag);
    public static final ISerializer<TagKey<SoundEvent>> SOUND_EVENT_TAG = new SerializerTagKey<>(Services.TAGS::soundTag);
    public static final ISerializer<TagKey<MenuType<?>>> MENU_TAG = new SerializerTagKey<>(Services.TAGS::menuTag);
    public static final ISerializer<TagKey<ParticleType<?>>> PARTICLE_TAG = new SerializerTagKey<>(Services.TAGS::particleTag);
    public static final ISerializer<TagKey<EntityType<?>>> ENTITY_TAG = new SerializerTagKey<>(Services.TAGS::entityTag);
    public static final ISerializer<TagKey<BlockEntityType<?>>> BLOCK_ENTITY_TAG = new SerializerTagKey<>(Services.TAGS::blockEntityTag);
    public static final ISerializer<TagKey<GameEvent>> GAME_EVENT_TAG = new SerializerTagKey<>(Services.TAGS::gameEventTag);
    public static final ISerializer<TagKey<Fluid>> FLUID_TAG = new SerializerTagKey<>(Services.TAGS::fluidTag);
    public static final ISerializer<TagKey<ResourceLocation>> STAT_TAG = new SerializerTagKey<>(Services.TAGS::statTag);
    public static final ISerializer<TagKey<RecipeType<?>>> RECIPE_TYPE_TAG = new SerializerTagKey<>(Services.TAGS::recipeTypeTag);
    public static final ISerializer<TagKey<RecipeSerializer<?>>> RECIPE_SERIALIZER_TAG = new SerializerTagKey<>(Services.TAGS::recipeSerializerTag);
    public static final ISerializer<TagKey<DimensionType>> DIMENSION_TYPE_TAG = new SerializerTagKey<>(Services.TAGS::dimensionTypeTag);
    public static final ISerializer<TagKey<Level>> DIMENSION_TAG = new SerializerTagKey<>(Services.TAGS::dimensionTag);
    public static final ISerializer<TagKey<Biome>> BIOME_TAG = new SerializerTagKey<>(Services.TAGS::biomeTag);
}