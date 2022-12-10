package net.darkhax.bookshelf.impl.data;

import net.darkhax.bookshelf.api.data.ITagHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public class TagHelperVanilla implements ITagHelper {

    @Override
    public TagKey<SoundEvent> soundTag(ResourceLocation tag) {
        return TagKey.create(Registries.SOUND_EVENT, tag);
    }

    @Override
    public TagKey<Fluid> fluidTag(ResourceLocation tag) {
        return TagKey.create(Registries.FLUID, tag);
    }

    @Override
    public TagKey<MobEffect> effectTag(ResourceLocation tag) {
        return TagKey.create(Registries.MOB_EFFECT, tag);
    }

    @Override
    public TagKey<Block> blockTag(ResourceLocation tag) {
        return TagKey.create(Registries.BLOCK, tag);
    }

    @Override
    public TagKey<Enchantment> enchantmentTag(ResourceLocation tag) {
        return TagKey.create(Registries.ENCHANTMENT, tag);
    }

    @Override
    public TagKey<EntityType<?>> entityTag(ResourceLocation tag) {
        return TagKey.create(Registries.ENTITY_TYPE, tag);
    }

    @Override
    public TagKey<Item> itemTag(ResourceLocation tag) {
        return TagKey.create(Registries.ITEM, tag);
    }

    @Override
    public TagKey<BannerPattern> bannerPatternTag(ResourceLocation tag) {
        return TagKey.create(Registries.BANNER_PATTERN, tag);
    }

    @Override
    public TagKey<Potion> potionTag(ResourceLocation tag) {
        return TagKey.create(Registries.POTION, tag);
    }

    @Override
    public TagKey<ParticleType<?>> particleTag(ResourceLocation tag) {
        return TagKey.create(Registries.PARTICLE_TYPE, tag);
    }

    @Override
    public TagKey<BlockEntityType<?>> blockEntityTag(ResourceLocation tag) {
        return TagKey.create(Registries.BLOCK_ENTITY_TYPE, tag);
    }

    @Override
    public TagKey<PaintingVariant> paintingTag(ResourceLocation tag) {
        return TagKey.create(Registries.PAINTING_VARIANT, tag);
    }

    @Override
    public TagKey<ResourceLocation> statTag(ResourceLocation tag) {
        return TagKey.create(Registries.CUSTOM_STAT, tag);
    }

    @Override
    public TagKey<MenuType<?>> menuTag(ResourceLocation tag) {
        return TagKey.create(Registries.MENU, tag);
    }

    @Override
    public TagKey<RecipeType<?>> recipeTypeTag(ResourceLocation tag) {
        return TagKey.create(Registries.RECIPE_TYPE, tag);
    }

    @Override
    public TagKey<RecipeSerializer<?>> recipeSerializerTag(ResourceLocation tag) {
        return TagKey.create(Registries.RECIPE_SERIALIZER, tag);
    }

    @Override
    public TagKey<Attribute> attributeTag(ResourceLocation tag) {
        return TagKey.create(Registries.ATTRIBUTE, tag);
    }

    @Override
    public TagKey<GameEvent> gameEventTag(ResourceLocation tag) {
        return TagKey.create(Registries.GAME_EVENT, tag);
    }

    @Override
    public TagKey<VillagerType> villagerTypeTag(ResourceLocation tag) {
        return TagKey.create(Registries.VILLAGER_TYPE, tag);
    }

    @Override
    public TagKey<VillagerProfession> villagerProfessionTag(ResourceLocation tag) {
        return TagKey.create(Registries.VILLAGER_PROFESSION, tag);
    }

    @Override
    public TagKey<DimensionType> dimensionTypeTag(ResourceLocation tag) {
        return TagKey.create(Registries.DIMENSION_TYPE, tag);
    }

    @Override
    public TagKey<Level> dimensionTag(ResourceLocation tag) {
        return TagKey.create(Registries.DIMENSION, tag);
    }

    @Override
    public TagKey<Biome> biomeTag(ResourceLocation tag) {
        return TagKey.create(Registries.BIOME, tag);
    }

    @Override
    public <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation tag) {
        return TagKey.create(registryKey, tag);
    }
}
