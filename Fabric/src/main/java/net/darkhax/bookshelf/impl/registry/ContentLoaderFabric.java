package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.registry.IContentLoader;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.resources.WrappedReloadListener;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ContentLoaderFabric implements IContentLoader {

    @Override
    public void loadContent(RegistryDataProvider content) {

        this.consumeVanillaRegistry(content.blocks, BuiltInRegistries.BLOCK);
        this.consumeVanillaRegistry(content.fluids, BuiltInRegistries.FLUID);
        this.consumeVanillaRegistry(content.items, BuiltInRegistries.ITEM);
        this.consumeVanillaRegistry(content.bannerPatterns, BuiltInRegistries.BANNER_PATTERN);
        this.consumeVanillaRegistry(content.mobEffects, BuiltInRegistries.MOB_EFFECT);
        this.consumeVanillaRegistry(content.sounds, BuiltInRegistries.SOUND_EVENT);
        this.consumeVanillaRegistry(content.potions, BuiltInRegistries.POTION);
        this.consumeVanillaRegistry(content.enchantments, BuiltInRegistries.ENCHANTMENT);
        this.consumeVanillaRegistry(content.entities, BuiltInRegistries.ENTITY_TYPE);
        this.consumeVanillaRegistry(content.blockEntities, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        this.consumeVanillaRegistry(content.particleTypes, BuiltInRegistries.PARTICLE_TYPE);
        this.consumeVanillaRegistry(content.menus, BuiltInRegistries.MENU);
        this.consumeVanillaRegistry(content.recipeSerializers, BuiltInRegistries.RECIPE_SERIALIZER);
        this.consumeVanillaRegistry(content.paintings, BuiltInRegistries.PAINTING_VARIANT);
        this.consumeVanillaRegistry(content.attributes, BuiltInRegistries.ATTRIBUTE);
        this.consumeVanillaRegistry(content.stats, BuiltInRegistries.STAT_TYPE);
        this.consumeVanillaRegistry(content.villagerProfessions, BuiltInRegistries.VILLAGER_PROFESSION);
        this.consumeRegistry(content.commandArguments, (id, value) -> ArgumentTypeRegistry.registerArgumentType(id, (Class) value.getType(), (ArgumentTypeInfo) value.getSerializer().get()));
        this.consumeVanillaRegistry(content.recipeTypes, BuiltInRegistries.RECIPE_TYPE);

        CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> content.commands.build((id, value) -> value.build(dispatcher, access, environment)));

        this.registerTradeData(content.trades.getVillagerTrades());
        this.registerWanderingTrades(content.trades.getCommonWanderingTrades(), content.trades.getRareWanderingTrades());

        this.consumeRegistry(content.dataListeners, (id, value) -> ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new WrappedReloadListener(id, value)));

        if (Services.PLATFORM.isPhysicalClient()) {

            this.loadClient(content);
        }
    }

    private void loadClient(RegistryDataProvider content) {

        this.consumeRegistry(content.resourceListeners, (id, value) -> ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedReloadListener(id, value)));
    }

    private <T, O> void consumeVanillaRegistry(IRegistryEntries<O> toRegister, Registry<T> registry, Function<O, T> wrapper) {

        toRegister.build((id, value) -> Registry.register(registry, id, wrapper.apply(value)));
    }

    private <T> void consumeVanillaRegistry(IRegistryEntries<T> toRegister, Registry<T> registry) {

        toRegister.build((id, value) -> Registry.register(registry, id, value));
    }

    private <T> void consumeRegistry(IRegistryEntries<T> toRegister, BiConsumer<ResourceLocation, T> func) {

        toRegister.build(func);
    }

    private void registerTradeData(Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> villagerTrades) {

        for (Map.Entry<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> professionData : villagerTrades.entrySet()) {

            final Int2ObjectMap<VillagerTrades.ItemListing[]> professionTrades = VillagerTrades.TRADES.computeIfAbsent(professionData.getKey(), profession -> new Int2ObjectOpenHashMap<>());

            for (int merchantTier : professionData.getValue().keySet()) {

                final List<VillagerTrades.ItemListing> tradesForTier = new ArrayList<>(Arrays.asList(professionTrades.getOrDefault(merchantTier, new VillagerTrades.ItemListing[0])));
                tradesForTier.addAll(professionData.getValue().get(merchantTier));
                professionTrades.put(merchantTier, tradesForTier.toArray(new VillagerTrades.ItemListing[0]));
            }
        }
    }

    private void registerWanderingTrades(List<VillagerTrades.ItemListing> commonTrades, List<VillagerTrades.ItemListing> rareTrades) {

        if (!commonTrades.isEmpty()) {

            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(1)));
            tradeData.addAll(commonTrades);
            VillagerTrades.WANDERING_TRADER_TRADES.put(1, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }

        if (!rareTrades.isEmpty()) {

            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(2)));
            tradeData.addAll(rareTrades);
            VillagerTrades.WANDERING_TRADER_TRADES.put(2, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }
    }
}
