package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IGameRegistries;
import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class GameRegistriesForge implements IGameRegistries {

    private final IRegistryReader<Block> blockRegistry = new RegistryReaderForge<>(ForgeRegistries.BLOCKS);
    private final IRegistryReader<Item> itemRegistry = new RegistryReaderForge<>(ForgeRegistries.ITEMS);
    private final IRegistryReader<Enchantment> enchantmentRegistry = new RegistryReaderForge<>(ForgeRegistries.ENCHANTMENTS);
    private final IRegistryReader<Motive> paintingRegistry = new RegistryReaderForge<>(ForgeRegistries.PAINTING_TYPES);
    private final IRegistryReader<MobEffect> mobEffectRegistry = new RegistryReaderForge<>(ForgeRegistries.MOB_EFFECTS);
    private final IRegistryReader<Potion> potionRegistry = new RegistryReaderForge<>(ForgeRegistries.POTIONS);
    private final IRegistryReader<Attribute> attributeRegistry = new RegistryReaderForge<>(ForgeRegistries.ATTRIBUTES);
    private final IRegistryReader<VillagerProfession> professionRegistry = new RegistryReaderForge<>(ForgeRegistries.PROFESSIONS);
    private final IRegistryReader<SoundEvent> soundRegistry = new RegistryReaderForge<>(ForgeRegistries.SOUND_EVENTS);
    private final IRegistryReader<MenuType<?>> menuRegistry = new RegistryReaderForge<>(ForgeRegistries.CONTAINERS);
    private final IRegistryReader<ParticleType<?>> particleRegistry = new RegistryReaderForge<>(ForgeRegistries.PARTICLE_TYPES);
    private final IRegistryReader<EntityType<?>> entityRegistry = new RegistryReaderForge<>(ForgeRegistries.ENTITIES);
    private final IRegistryReader<BlockEntityType<?>> blockEntityRegistry = new RegistryReaderForge<>(ForgeRegistries.BLOCK_ENTITIES);

    // No native Forge Registry
    @Deprecated
    private final IRegistryReader<GameEvent> gameEventRegistry = new RegistryReaderVanilla<>(Registry.GAME_EVENT);

    @Deprecated
    private final IRegistryReader<VillagerType> villagerTypeRegistry = new RegistryReaderVanilla<>(Registry.VILLAGER_TYPE);

    @Override
    public IRegistryReader<Block> blocks() {

        return this.blockRegistry;
    }

    @Override
    public IRegistryReader<Item> items() {

        return this.itemRegistry;
    }

    @Override
    public IRegistryReader<Enchantment> enchantments() {

        return this.enchantmentRegistry;
    }

    @Override
    public IRegistryReader<Motive> paintings() {

        return this.paintingRegistry;
    }

    @Override
    public IRegistryReader<MobEffect> mobEffects() {

        return this.mobEffectRegistry;
    }

    @Override
    public IRegistryReader<Potion> potions() {
        return this.potionRegistry;
    }

    @Override
    public IRegistryReader<Attribute> attributes() {
        return this.attributeRegistry;
    }

    @Override
    public IRegistryReader<VillagerProfession> villagerProfessions() {
        return this.professionRegistry;
    }

    @Override
    public IRegistryReader<VillagerType> villagerTypes() {
        return this.villagerTypeRegistry;
    }

    @Override
    public IRegistryReader<SoundEvent> sounds() {
        return this.soundRegistry;
    }

    @Override
    public IRegistryReader<MenuType<?>> menuTypes() {
        return this.menuRegistry;
    }

    @Override
    public IRegistryReader<ParticleType<?>> particles() {
        return this.particleRegistry;
    }

    @Override
    public IRegistryReader<EntityType<?>> entities() {
        return this.entityRegistry;
    }

    @Override
    public IRegistryReader<BlockEntityType<?>> blockEntities() {
        return this.blockEntityRegistry;
    }

    @Override
    public IRegistryReader<GameEvent> gameEvents() {
        return this.gameEventRegistry;
    }
}
