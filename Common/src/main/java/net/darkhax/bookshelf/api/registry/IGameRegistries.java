package net.darkhax.bookshelf.api.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;

public interface IGameRegistries extends IContentLoader {

    IRegistryReader<Block> blocks();

    IRegistryReader<Item> items();

    IRegistryReader<BannerPattern> bannerPatterns();
    
    IRegistryReader<Enchantment> enchantments();

    IRegistryReader<PaintingVariant> paintings();

    IRegistryReader<MobEffect> mobEffects();

    IRegistryReader<Potion> potions();

    IRegistryReader<Attribute> attributes();

    IRegistryReader<VillagerProfession> villagerProfessions();

    IRegistryReader<VillagerType> villagerTypes();

    IRegistryReader<SoundEvent> sounds();

    IRegistryReader<MenuType<?>> menuTypes();

    IRegistryReader<ParticleType<?>> particles();

    IRegistryReader<EntityType<?>> entities();

    IRegistryReader<BlockEntityType<?>> blockEntities();

    IRegistryReader<GameEvent> gameEvents();
}
