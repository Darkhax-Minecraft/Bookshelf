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

public abstract class GameRegistriesVanilla implements IGameRegistries {

    private final IRegistryReader<Block> blockRegistry = new RegistryReaderVanilla<>(Registry.BLOCK);
    private final IRegistryReader<Item> itemRegistry = new RegistryReaderVanilla<>(Registry.ITEM);
    private final IRegistryReader<Enchantment> enchantmentRegistry = new RegistryReaderVanilla<>(Registry.ENCHANTMENT);
    private final IRegistryReader<Motive> paintingRegistry = new RegistryReaderVanilla<>(Registry.MOTIVE);
    private final IRegistryReader<MobEffect> mobEffectRegistry = new RegistryReaderVanilla<>(Registry.MOB_EFFECT);
    private final IRegistryReader<Potion> potionRegistry = new RegistryReaderVanilla<>(Registry.POTION);
    private final IRegistryReader<Attribute> attributeRegistry = new RegistryReaderVanilla<>(Registry.ATTRIBUTE);
    private final IRegistryReader<VillagerProfession> professionRegistry = new RegistryReaderVanilla<>(Registry.VILLAGER_PROFESSION);
    private final IRegistryReader<VillagerType> villagerTypeRegistry = new RegistryReaderVanilla<>(Registry.VILLAGER_TYPE);
    private final IRegistryReader<SoundEvent> soundRegistry = new RegistryReaderVanilla<>(Registry.SOUND_EVENT);
    private final IRegistryReader<MenuType<?>> menuRegistry = new RegistryReaderVanilla<>(Registry.MENU);
    private final IRegistryReader<ParticleType<?>> particleRegistry = new RegistryReaderVanilla<>(Registry.PARTICLE_TYPE);
    private final IRegistryReader<EntityType<?>> entityRegistry = new RegistryReaderVanilla<>(Registry.ENTITY_TYPE);
    private final IRegistryReader<BlockEntityType<?>> blockEntityRegistry = new RegistryReaderVanilla<>(Registry.BLOCK_ENTITY_TYPE);
    private final IRegistryReader<GameEvent> gameEventRegistry = new RegistryReaderVanilla<>(Registry.GAME_EVENT);

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
