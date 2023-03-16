package net.darkhax.bookshelf.api.data;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
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

public interface ITagHelper {

    TagKey<SoundEvent> soundTag(ResourceLocation tag);

    TagKey<Fluid> fluidTag(ResourceLocation tag);

    TagKey<MobEffect> effectTag(ResourceLocation tag);

    TagKey<Block> blockTag(ResourceLocation tag);

    TagKey<Enchantment> enchantmentTag(ResourceLocation tag);

    TagKey<EntityType<?>> entityTag(ResourceLocation tag);

    TagKey<Item> itemTag(ResourceLocation tag);

    TagKey<BannerPattern> bannerPatternTag(ResourceLocation tag);

    TagKey<Potion> potionTag(ResourceLocation tag);

    TagKey<ParticleType<?>> particleTag(ResourceLocation tag);

    TagKey<BlockEntityType<?>> blockEntityTag(ResourceLocation tag);

    TagKey<PaintingVariant> paintingTag(ResourceLocation tag);

    TagKey<ResourceLocation> statTag(ResourceLocation tag);

    TagKey<MenuType<?>> menuTag(ResourceLocation tag);

    TagKey<RecipeType<?>> recipeTypeTag(ResourceLocation tag);

    TagKey<RecipeSerializer<?>> recipeSerializerTag(ResourceLocation tag);

    TagKey<Attribute> attributeTag(ResourceLocation tag);

    TagKey<GameEvent> gameEventTag(ResourceLocation tag);

    TagKey<VillagerType> villagerTypeTag(ResourceLocation tag);

    TagKey<VillagerProfession> villagerProfessionTag(ResourceLocation tag);

    TagKey<DimensionType> dimensionTypeTag(ResourceLocation tag);

    TagKey<Level> dimensionTag(ResourceLocation tag);

    TagKey<Biome> biomeTag(ResourceLocation tag);

    TagKey<DamageType> damageTag(ResourceLocation tag);

    <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation tag);
}
