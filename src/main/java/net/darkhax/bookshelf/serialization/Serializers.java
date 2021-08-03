package net.darkhax.bookshelf.serialization;

import java.util.UUID;

import net.darkhax.bookshelf.block.DisplayableBlockState;
import net.darkhax.bookshelf.crafting.block.BlockIngredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.stats.StatType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.village.poi.PoiType;
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
    public static final ISerializer<Component> TEXT = SerializerText.SERIALIZER;
    public static final ISerializer<ResourceLocation> RESOURCE_LOCATION = SerializerResourceLocation.SERIALIZER;
    public static final ISerializer<EnchantmentInstance> ENCHANTMENT_DATA = SerializerEnchantmentData.SERIALIZER;
    public static final ISerializer<BlockState> BLOCK_STATE = SerializerBlockState.SERIALIZER;
    public static final ISerializer<Ingredient> INGREDIENT = SerializerIngredient.SERIALIZER;
    public static final ISerializer<CompoundTag> NBT = SerializerNBT.SERIALIZER;
    public static final ISerializer<ItemStack> ITEMSTACK = SerializerItemStack.SERIALIZER;
    public static final ISerializer<Vector3f> VEC3F = SerializerVec3f.SERIALIZER;
    public static final ISerializer<Vec3> VEC3D = SerializerVec3d.SERIALIZER;
    public static final ISerializer<DisplayableBlockState> DISPLAY_STATE = DisplayableBlockState.SERIALIZER;
    public static final ISerializer<BlockIngredient> BLOCK_INGREDIENT = BlockIngredientSerializer.SERIALIZER;

    public static final ISerializer<Named<Block>> BLOCK_TAG = new SerializerINamedTag<>(BlockTags::createOptional);
    public static final ISerializer<Named<Item>> ITEM_TAG = new SerializerINamedTag<>(ItemTags::createOptional);
    public static final ISerializer<Named<EntityType<?>>> ENTITY_TAG = new SerializerINamedTag<>(EntityTypeTags::createOptional);
    public static final ISerializer<Named<Fluid>> FLUID_TAG = new SerializerINamedTag<>(FluidTags::createOptional);
    public static final ISerializer<Named<Enchantment>> ENCHANTMENT_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.ENCHANTMENTS, rl));
    public static final ISerializer<Named<Potion>> POTION_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.POTIONS, rl));
    public static final ISerializer<Named<BlockEntityType<?>>> TILE_ENTITY_TAG = new SerializerINamedTag<>(rl -> ForgeTagHandler.createOptionalTag(ForgeRegistries.BLOCK_ENTITIES, rl));

    public static final ISerializer<Block> BLOCK = new SerializerForgeRegistry<>(ForgeRegistries.BLOCKS);
    public static final ISerializer<Fluid> FLUID = new SerializerForgeRegistry<>(ForgeRegistries.FLUIDS);
    public static final ISerializer<Item> ITEM = new SerializerForgeRegistry<>(ForgeRegistries.ITEMS);
    public static final ISerializer<MobEffect> EFFECT = new SerializerForgeRegistry<>(ForgeRegistries.MOB_EFFECTS);
    public static final ISerializer<SoundEvent> SOUND = new SerializerForgeRegistry<>(ForgeRegistries.SOUND_EVENTS);
    public static final ISerializer<Potion> POTION = new SerializerForgeRegistry<>(ForgeRegistries.POTIONS);
    public static final ISerializer<Enchantment> ENCHANTMENT = new SerializerForgeRegistry<>(ForgeRegistries.ENCHANTMENTS);
    public static final ISerializer<EntityType<?>> ENTITY = new SerializerForgeRegistry<>(ForgeRegistries.ENTITIES);
    public static final ISerializer<BlockEntityType<?>> BLOCK_ENTITY = new SerializerForgeRegistry<>(ForgeRegistries.BLOCK_ENTITIES);
    public static final ISerializer<ParticleType<?>> PARTICLE = new SerializerForgeRegistry<>(ForgeRegistries.PARTICLE_TYPES);
    public static final ISerializer<MenuType<?>> CONTAINER = new SerializerForgeRegistry<>(ForgeRegistries.CONTAINERS);
    public static final ISerializer<Motive> PAINTING = new SerializerForgeRegistry<>(ForgeRegistries.PAINTING_TYPES);
    public static final ISerializer<RecipeSerializer<?>> RECIPE_SERIALIZER = new SerializerForgeRegistry<>(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final ISerializer<Attribute> ATTRIBUTE = new SerializerForgeRegistry<>(ForgeRegistries.ATTRIBUTES);
    public static final ISerializer<StatType<?>> STAT = new SerializerForgeRegistry<>(ForgeRegistries.STAT_TYPES);
    public static final ISerializer<VillagerProfession> PROFESSION = new SerializerForgeRegistry<>(ForgeRegistries.PROFESSIONS);
    public static final ISerializer<PoiType> POINT_OF_INTEREST = new SerializerForgeRegistry<>(ForgeRegistries.POI_TYPES);
}