package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.registry.CommandArgumentEntry;
import net.darkhax.bookshelf.api.registry.IGameRegistries;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.IRegistryReader;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.util.ForgeEventHelper;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class GameRegistriesForge implements IGameRegistries {

    private final IRegistryReader<Block> blockRegistry = new RegistryReaderForge<>(ForgeRegistries.BLOCKS);
    private final IRegistryReader<Item> itemRegistry = new RegistryReaderForge<>(ForgeRegistries.ITEMS);
    private final IRegistryReader<BannerPattern> bannerPatternRegistry = new RegistryReaderVanilla<>(Registry.BANNER_PATTERN);
    private final IRegistryReader<Enchantment> enchantmentRegistry = new RegistryReaderForge<>(ForgeRegistries.ENCHANTMENTS);
    private final IRegistryReader<PaintingVariant> paintingRegistry = new RegistryReaderForge<>(ForgeRegistries.PAINTING_VARIANTS);
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
    public IRegistryReader<BannerPattern> bannerPatterns() {

        return this.bannerPatternRegistry;
    }

    @Override
    public IRegistryReader<Enchantment> enchantments() {

        return this.enchantmentRegistry;
    }

    @Override
    public IRegistryReader<PaintingVariant> paintings() {

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

        this.consumeRegistry(content.blocks, Registry.BLOCK_REGISTRY);
        this.consumeRegistry(content.fluids, Registry.FLUID_REGISTRY);
        this.consumeRegistry(content.items, Registry.ITEM_REGISTRY);
        this.consumeRegistry(content.bannerPatterns, Registry.BANNER_PATTERN_REGISTRY);
        this.consumeRegistry(content.mobEffects, Registry.MOB_EFFECT_REGISTRY);
        this.consumeRegistry(content.sounds, Registry.SOUND_EVENT_REGISTRY);
        this.consumeRegistry(content.potions, Registry.POTION_REGISTRY);
        this.consumeRegistry(content.enchantments, Registry.ENCHANTMENT_REGISTRY);
        this.consumeRegistry(content.entities, Registry.ENTITY_TYPE_REGISTRY);
        this.consumeRegistry(content.blockEntities, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
        this.consumeRegistry(content.particleTypes, Registry.PARTICLE_TYPE_REGISTRY);
        this.consumeRegistry(content.menus, Registry.MENU_REGISTRY);
        this.consumeRegistry(content.recipeSerializers, Registry.RECIPE_SERIALIZER_REGISTRY);
        this.consumeRegistry(content.paintings, Registry.PAINTING_VARIANT_REGISTRY);
        this.consumeRegistry(content.attributes, Registry.ATTRIBUTE_REGISTRY);
        this.consumeRegistry(content.stats, Registry.STAT_TYPE_REGISTRY);
        this.consumeRegistry(content.villagerProfessions, Registry.VILLAGER_PROFESSION_REGISTRY);
        this.consumeArgumentTypes(content.commandArguments);

        // TODO Forge does not provide registry access?
        this.consumeWithForgeEvent(content.commands, RegisterCommandsEvent.class, (event, id, builder) -> builder.build(event.getDispatcher(), null, event.getEnvironment()));

        ForgeEventHelper.addContextListener(VillagerTradesEvent.class, content.trades.getVillagerTrades(), this::registerVillagerTrades);
        ForgeEventHelper.addContextListener(WandererTradesEvent.class, content.trades.getCommonWanderingTrades(), content.trades.getRareWanderingTrades(), this::registerWanderingTrades);

        this.consumeWithForgeEvent(content.dataListeners, AddReloadListenerEvent.class, (event, id, arg) -> event.addListener(arg));

        if (Services.PLATFORM.isPhysicalClient()) {

            this.loadClient(content);
        }
    }

    private void loadClient(RegistryDataProvider content) {

        this.consumeWithModEvent(content.resourceListeners, RegisterClientReloadListenersEvent.class, (event, id, arg) -> event.registerReloadListener(arg));
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

    private <T> void consumeRegistry(IRegistryEntries<T> registry, ResourceKey<? extends Registry<T>> registryKey) {

        this.consumeRegistry(registry, registryKey, v -> v);
    }

    private <T> void consumeRegistry(IRegistryEntries<T> registry, ResourceKey<? extends Registry<T>> registryKey, Function<T, ? extends T> wrapper) {

        if (registry.isEmpty()) {

            return;
        }

        final Consumer<RegisterEvent> listener = event -> {

            if (event.getRegistryKey().equals(registryKey)) {

                registry.build((id, value) -> event.register(registryKey, id, () -> wrapper.apply(value)));
            }
        };

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, RegisterEvent.class, listener);
    }

    private void consumeArgumentTypes(IRegistryEntries<CommandArgumentEntry<?,?,?>> registry) {

        if (registry.isEmpty()) {

            return;
        }

        final Consumer<RegisterEvent> listener = event -> {

            if (event.getRegistryKey().equals(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY)) {

                registry.build((id, value) -> {

                    event.register(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY,id, () -> {

                        final ArgumentTypeInfo serializer = value.getSerializer().get();
                        ArgumentTypeInfos.registerByClass((Class) value.getType(), serializer);
                        return serializer;
                    });
                });
            }
        };

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, RegisterEvent.class, listener);
    }

    interface EventConsumer<E extends Event, V> {

        void apply(E event, ResourceLocation id, V value);
    }
}
