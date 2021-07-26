package net.darkhax.bookshelf.serialization;

import java.util.UUID;

import net.darkhax.bookshelf.block.DisplayableBlockState;
import net.darkhax.bookshelf.crafting.block.BlockIngredient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class Serializers {

    public static final ISerializer<Boolean> BOOLEAN = SerializerBoolean.SERIALIZER;
    public static final ISerializer<Byte> BYTE = SerializerByte.SERIALIZER;
    public static final ISerializer<Short> SHORT = SerializerShort.SERIALIZER;
    public static final ISerializer<Integer> INT = SerializerInteger.SERIALIZER;
    public static final ISerializer<Long> LONG = SerializerLong.SERIALIZER;
    public static final ISerializer<Float> FLOAT = SerializerFloat.SERIALIZER;
    public static final ISerializer<Double> DOUBLE = SerializerDouble.SERIALIZER;
    public static final ISerializer<String> STRING = SerializerString.SERIALIZER;
    public static final ISerializer<UUID> UUID = SerializerUUID.SERIALIZER;
    public static final ISerializer<ITextComponent> TEXT = SerializerText.SERIALIZER;
    public static final ISerializer<ResourceLocation> RESOURCE_LOCATION = SerializerResourceLocation.SERIALIZER;
    public static final ISerializer<EnchantmentData> ENCHANTMENT_DATA = SerializerEnchantmentData.SERIALIZER;
    public static final ISerializer<BlockState> BLOCK_STATE = SerializerBlockState.SERIALIZER;
    public static final ISerializer<Ingredient> INGREDIENT = SerializerIngredient.SERIALIZER;
    public static final ISerializer<CompoundNBT> NBT = SerializerNBT.SERIALIZER;
    public static final ISerializer<ItemStack> ITEMSTACK = SerializerItemStack.SERIALIZER;
    public static final ISerializer<Vector3f> VEC3F = SerializerVec3f.SERIALIZER;
    public static final ISerializer<Vector3d> VEC3D = SerializerVec3d.SERIALIZER;
    public static final ISerializer<DisplayableBlockState> DISPLAY_STATE = DisplayableBlockState.SERIALIZER;
    public static final ISerializer<BlockIngredient> BLOCK_INGREDIENT = BlockIngredientSerializer.SERIALIZER;

    public static final ISerializer<INamedTag<Block>> BLOCK_TAG = new SerializerINamedTag<>(BlockTags::createOptional);
    public static final ISerializer<INamedTag<Item>> ITEM_TAG = new SerializerINamedTag<>(ItemTags::createOptional);
    public static final ISerializer<INamedTag<EntityType<?>>> ENTITY_TAG = new SerializerINamedTag<>(EntityTypeTags::createOptional);
    public static final ISerializer<INamedTag<Fluid>> FLUID_TAG = new SerializerINamedTag<>(FluidTags::createOptional);
    public static final ISerializer<INamedTag<Enchantment>> ENCHANTMENT_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.ENCHANTMENTS, rl));
    public static final ISerializer<INamedTag<Potion>> POTION_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.POTION_TYPES, rl));
    public static final ISerializer<INamedTag<TileEntityType<?>>> TILE_ENTITY_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.TILE_ENTITIES, rl));

    public static final ISerializer<Block> BLOCK = new SerializerForgeRegistry<>(ForgeRegistries.BLOCKS);
    public static final ISerializer<Fluid> FLUID = new SerializerForgeRegistry<>(ForgeRegistries.FLUIDS);
    public static final ISerializer<Item> ITEM = new SerializerForgeRegistry<>(ForgeRegistries.ITEMS);
    public static final ISerializer<Effect> EFFECT = new SerializerForgeRegistry<>(ForgeRegistries.POTIONS);
    public static final ISerializer<SoundEvent> SOUND = new SerializerForgeRegistry<>(ForgeRegistries.SOUND_EVENTS);
    public static final ISerializer<Potion> POTION = new SerializerForgeRegistry<>(ForgeRegistries.POTION_TYPES);
    public static final ISerializer<Enchantment> ENCHANTMENT = new SerializerForgeRegistry<>(ForgeRegistries.ENCHANTMENTS);
    public static final ISerializer<EntityType<?>> ENTITY = new SerializerForgeRegistry<>(ForgeRegistries.ENTITIES);
    public static final ISerializer<TileEntityType<?>> TILE_ENTITY = new SerializerForgeRegistry<>(ForgeRegistries.TILE_ENTITIES);
    public static final ISerializer<ParticleType<?>> PARTICLE = new SerializerForgeRegistry<>(ForgeRegistries.PARTICLE_TYPES);
    public static final ISerializer<ContainerType<?>> CONTAINER = new SerializerForgeRegistry<>(ForgeRegistries.CONTAINERS);
    public static final ISerializer<PaintingType> PAINTING = new SerializerForgeRegistry<>(ForgeRegistries.PAINTING_TYPES);
    public static final ISerializer<IRecipeSerializer<?>> RECIPE_SERIALIZER = new SerializerForgeRegistry<>(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final ISerializer<Attribute> ATTRIBUTE = new SerializerForgeRegistry<>(ForgeRegistries.ATTRIBUTES);
    public static final ISerializer<StatType<?>> STAT = new SerializerForgeRegistry<>(ForgeRegistries.STAT_TYPES);
    public static final ISerializer<VillagerProfession> PROFESSION = new SerializerForgeRegistry<>(ForgeRegistries.PROFESSIONS);
    public static final ISerializer<PointOfInterestType> POINT_OF_INTEREST = new SerializerForgeRegistry<>(ForgeRegistries.POI_TYPES);
}