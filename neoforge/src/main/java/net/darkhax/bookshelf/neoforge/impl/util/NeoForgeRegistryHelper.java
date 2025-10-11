package net.darkhax.bookshelf.neoforge.impl.util;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockEntityRendererAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockRenderTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.CommandArgumentAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.CreativeModeTabAdapter;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry.adapters.GenericRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.IngredientTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.LootEntryTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.MenuScreenAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.MenuTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.PacketAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.PotPatternAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.RecipeTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.VillagerTradeAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.darkhax.bookshelf.common.mixin.access.client.AccessorItemBlockRenderTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NeoForgeRegistryHelper {

    private final ContentProvider content;
    private final RegistrationContext context;
    private final IEventBus modBus;

    public NeoForgeRegistryHelper(ContentProvider content) {
        this.content = content;
        this.context = new RegistrationContext(content.namespace());
        this.modBus = getModBus(content.namespace());
        if (this.content.canLoad()) {
            this.content.defineLoadConditions(new GenericRegistryAdapter<>(this.context, (id, val) -> LoadConditions.register(id, val.get())));
            this.modBus.addListener(this::registerContent);
            this.setupTradeRegistration();
            this.setupCommandRegistration();
            this.content.definePackets(new PacketAdapter(this.context, Services.NETWORK::register));
            if (Services.PLATFORM.isPhysicalClient()) {
                this.modBus.addListener(this::bindMenuScreens);
                this.modBus.addListener(this::registerRenderers);
            }
        }
        else {
            Constants.LOG.debug("Content provider {} is disabled.", content);
        }
    }

    private void registerContent(RegisterEvent event) {
        event.register(Registries.BLOCK, helper -> {
            this.content.defineBlocks(new BlockRegistryAdapter(this.context, Registries.BLOCK, adapt(helper)));
            if (Services.PLATFORM.isPhysicalClient()) {
                final Map<Block, RenderType> blockRenderTypes = AccessorItemBlockRenderTypes.bookshelf$getBlockTypes();
                this.content.defineBlockRenderTypes(new BlockRenderTypeAdapter(blockRenderTypes::put));
            }
        });
        event.register(Registries.ITEM, helper -> {
            this.context.getPlaceableBlocks().forEach((blockRef, builder) -> helper.register(blockRef.key().location(), builder.apply(blockRef.value().get())));
            this.content.defineItems(new GameRegistryAdapter<>(this.context, Registries.ITEM, adapt(helper)));
        });
        this.adaptRegistry(event, Registries.CREATIVE_MODE_TAB, this.content::defineCreativeTabs, CreativeModeTabAdapter::new);
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES, helper -> this.content.defineIngredientTypes(new IngredientTypeAdapter(this.context, (id, value) -> helper.register(id, adaptType(value.get())))));
        this.adaptRegistry(event, Registries.RECIPE_TYPE, this.content::defineRecipeTypes, RecipeTypeAdapter::new);
        this.adaptRegistry(event, Registries.ATTRIBUTE, this.content::defineAttributes);
        this.adaptRegistry(event, Registries.MOB_EFFECT, this.content::defineMobEffects);
        this.adaptRegistry(event, Registries.TRIGGER_TYPE, this.content::defineCriteriaTriggers);
        this.adaptRegistry(event, Registries.ITEM_SUB_PREDICATE_TYPE, this.content::defineItemSubPredicates);
        this.adaptRegistry(event, Registries.ENTITY_TYPE, this.content::defineEntities);
        this.adaptRegistry(event, Registries.CAT_VARIANT, this.content::defineCatVariants);
        this.adaptRegistry(event, Registries.POTION, this.content::definePotions);
        this.adaptRegistry(event, Registries.DECORATED_POT_PATTERN, this.content::definePotPatterns, PotPatternAdapter::new);
        this.adaptRegistry(event, Registries.DATA_COMPONENT_TYPE, this.content::defineItemComponents);
        this.adaptRegistry(event, Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, this.content::defineEnchantmentComponents);
        this.adaptRegistry(event, Registries.LOOT_CONDITION_TYPE, this.content::defineLootConditions);
        this.adaptRegistry(event, Registries.LOOT_FUNCTION_TYPE, this.content::defineLootFunctions);
        this.adaptRegistry(event, Registries.BLOCK_ENTITY_TYPE, this.content::defineBlockEntities);
        event.register(Registries.COMMAND_ARGUMENT_TYPE, helper -> this.content.defineCommandArguments(new CommandArgumentAdapter(this.context, (key, argType) -> registerCommandArgument(helper, key, argType.get()))));
        this.adaptRegistry(event, Registries.RECIPE_SERIALIZER, this.content::defineRecipeSerializers);
        this.adaptRegistry(event, Registries.LOOT_POOL_ENTRY_TYPE, this.content::defineLootEntryTypes, LootEntryTypeAdapter::new);
        event.register(Registries.MENU, helper -> this.content.defineMenuType(new MenuTypeAdapter(this.context, (key, factory) -> helper.register(key, new MenuType<>(factory.get()::create, FeatureFlags.VANILLA_SET)))));
    }

    private void setupCommandRegistration() {
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> this.content.defineCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));
    }

    private void setupTradeRegistration() {
        final CachedSupplier<VillagerTradeAdapter> trades = CachedSupplier.cache(() -> {
            final VillagerTradeAdapter adapter = new VillagerTradeAdapter();
            this.content.defineTrades(adapter);
            return adapter;
        });
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, event -> {
            final Multimap<Integer, VillagerTrades.ItemListing> newTrades = trades.get().getVillagerTrades().get(event.getType());
            if (newTrades != null && !newTrades.isEmpty()) {
                final Int2ObjectMap<List<VillagerTrades.ItemListing>> tradeData = event.getTrades();
                for (Map.Entry<Integer, VillagerTrades.ItemListing> entry : newTrades.entries()) {
                    tradeData.computeIfAbsent(entry.getKey(), ArrayList::new).add(entry.getValue());
                }
            }
        });
        NeoForge.EVENT_BUS.addListener(WandererTradesEvent.class, event -> {
            trades.get().getCommonWanderingTrades().forEach(event.getGenericTrades()::add);
            trades.get().getRareWanderingTrades().forEach(event.getRareTrades()::add);
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void bindMenuScreens(RegisterMenuScreensEvent event) {
        final MenuScreenAdapter adapter = new MenuScreenAdapter((type, factory) -> event.register(type, (MenuScreens.ScreenConstructor) factory::create));
        Services.CONTENT.get().forEach(provider -> provider.defineMenuScreens(adapter));
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        this.content.defineBlockRenderers(new BlockEntityRendererAdapter(event::registerBlockEntityRenderer));
    }

    private <T> void adaptRegistry(RegisterEvent event, ResourceKey<Registry<T>> registry, Consumer<GameRegistryAdapter<T>> contentProvider) {
        this.adaptRegistry(event, registry, contentProvider, (GameRegistryAdapterFactory<T, GameRegistryAdapter<T>>) GameRegistryAdapter::new);
    }

    private <T, A extends GameRegistryAdapter<T>> void adaptRegistry(RegisterEvent event, ResourceKey<Registry<T>> registry, Consumer<A> contentProvider, GameRegistryAdapterFactory<T, A> adapterFactory) {
        event.register(registry, helper -> contentProvider.accept(adapterFactory.build(this.context, registry, adapt(helper))));
    }

    private static <T> BiConsumer<ResourceKey<T>, Supplier<T>> adapt(RegisterEvent.RegisterHelper<T> helper) {
        return (key, value) -> helper.register(key, value.get());
    }

    private static <T> BiConsumer<ResourceLocation, Supplier<T>> adaptGeneric(RegisterEvent.RegisterHelper<T> helper) {
        return (key, value) -> helper.register(key, value.get());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static IngredientType adaptType(IngredientTypeAdapter.IngredientType type) {
        return new IngredientType(type.codec(), type.stream());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerCommandArgument(RegisterEvent.RegisterHelper<ArgumentTypeInfo<?, ?>> helper, ResourceLocation key, CommandArgumentAdapter.TypeInfo type) {
        helper.register(key, type.typeIfo());
        ArgumentTypeInfos.registerByClass(type.argType(), type.typeIfo());
    }

    private static IEventBus getModBus(String modid) {
        final ModContainer container = ModList.get().getModContainerById(modid).orElseThrow(() -> new IllegalArgumentException("Could not find mod '" + modid + "'."));
        if (container instanceof FMLModContainer fmlContainer) {
            final IEventBus modEventBus = fmlContainer.getEventBus();
            if (modEventBus != null) {
                return modEventBus;
            }
            throw new IllegalStateException("Mod '" + modid + "' does not have an event bus!");
        }
        throw new IllegalStateException("Mod '" + modid + "' is not an FML mod!");
    }

    @FunctionalInterface
    public interface GameRegistryAdapterFactory<T, A extends GameRegistryAdapter<T>> {
        A build(RegistrationContext context, ResourceKey<Registry<T>> registryKey, BiConsumer<ResourceKey<T>, Supplier<T>> registryFunc);
    }
}
