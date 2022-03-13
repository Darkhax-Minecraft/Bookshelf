package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.api.function.QuadConsumer;
import net.darkhax.bookshelf.api.function.TriConsumer;
import net.darkhax.bookshelf.api.registry.IGameRegistries;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.util.ForgeEventHelper;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    private final IRegistryReader<GameEvent> gameEventRegistry = new RegistryReaderVanilla<>(Registry.GAME_EVENT);
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

    // Content loading
    @Override
    public void loadContent(RegistryDataProvider content) {

        this.consumeRegistry(content.blocks, Block.class);
        this.consumeRegistry(content.fluids, Fluid.class);
        this.consumeRegistry(content.items, Item.class);
        this.consumeRegistry(content.mobEffects, MobEffect.class);
        this.consumeRegistry(content.sounds, SoundEvent.class);
        this.consumeRegistry(content.potions, Potion.class);
        this.consumeRegistry(content.enchantments, Enchantment.class);
        this.consumeRegistry(content.entities, EntityType.class);
        this.consumeRegistry(content.blockEntities, BlockEntityType.class);
        this.consumeRegistry(content.particleTypes, ParticleType.class);
        this.consumeRegistry(content.menus, MenuType.class);
        this.consumeRegistry(content.recipeSerializers, RecipeSerializer.class);
        this.consumeRegistry(content.paintings, Motive.class);
        this.consumeRegistry(content.attributes, Attribute.class);
        this.consumeRegistry(content.stats, StatType.class);
        this.consumeRegistry(content.villagerProfessions, VillagerProfession.class);

        this.consumeWithModEvent(content.commandArguments, FMLCommonSetupEvent.class, (event, id, arg) -> ArgumentTypes.register(id.toString(), arg.getA(), arg.getB()));
        this.consumeWithForgeEvent(content.commands, RegisterCommandsEvent.class, (event, id, builder) -> builder.build(event.getDispatcher(), event.getEnvironment() == Commands.CommandSelection.DEDICATED));

        ForgeEventHelper.addContextListener(VillagerTradesEvent.class, content.trades.getVillagerTrades(), this::registerVillagerTrades);
        ForgeEventHelper.addContextListener(WandererTradesEvent.class, content.trades.getCommonWanderingTrades(), content.trades.getRareWanderingTrades(), this::registerWanderingTrades);
    }

    private void registerVillagerTrades(VillagerTradesEvent event, Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> trades) {

        final Multimap<Integer, VillagerTrades.ItemListing> newTrades = trades.get(event.getType());

        if (newTrades != null) {

            final Int2ObjectMap<List<VillagerTrades.ItemListing>> tradeData = event.getTrades();

            for (Map.Entry<Integer, VillagerTrades.ItemListing> entry : newTrades.entries()) {

                tradeData.computeIfAbsent(entry.getKey(), ArrayList::new).add(entry.getValue());
            }
        }
    }

    private void registerWanderingTrades(WandererTradesEvent event, List<VillagerTrades.ItemListing> common, List<VillagerTrades.ItemListing> rare) {

        event.getGenericTrades().addAll(common);
        event.getRareTrades().addAll(rare);
    }

    private <ET extends Event, RT> void consumeWithForgeEvent(IRegistryEntries<RT> registry, Class<ET> eventType, EventConsumer<ET, RT> func) {

        final Consumer<ET> listener = event -> registry.build((id, value) -> func.apply(event, id, value));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, eventType, listener);
    }

    private <ET extends Event & IModBusEvent, RT> void consumeWithModEvent(IRegistryEntries<RT> registry, Class<ET> eventType, EventConsumer<ET, RT> func) {

        final Consumer<ET> listener = event -> {

            registry.build((id, value) -> func.apply(event, id, value));
        };

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, eventType, listener);
    }

    private <T extends IForgeRegistryEntry<T>> void consumeRegistry(IRegistryEntries<? extends T> registry, Class clazz) {

        final Consumer<RegistryEvent.Register<T>> listener = event -> {

            registry.build((id, value) -> {

                if (value.getRegistryName() == null) {

                    value.setRegistryName(id);
                }

                event.getRegistry().register(value);
            });
        };

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(clazz, listener);
    }

    interface EventConsumer<E extends Event, V> {

        void apply(E event, ResourceLocation id, V value);
    }
}
