package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.bookshelf.impl.data.recipes.WrappedRecipeSerializer;
import net.darkhax.bookshelf.impl.resources.WrappedReloadListener;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GameRegistriesFabric extends GameRegistriesVanilla {

    @Override
    public void loadContent(RegistryDataProvider content) {

        this.consumeVanillaRegistry(content.blocks, Registry.BLOCK);
        this.consumeVanillaRegistry(content.fluids, Registry.FLUID);
        this.consumeVanillaRegistry(content.items, Registry.ITEM);
        this.consumeVanillaRegistry(content.mobEffects, Registry.MOB_EFFECT);
        this.consumeVanillaRegistry(content.sounds, Registry.SOUND_EVENT);
        this.consumeVanillaRegistry(content.potions, Registry.POTION);
        this.consumeVanillaRegistry(content.enchantments, Registry.ENCHANTMENT);
        this.consumeVanillaRegistry(content.entities, Registry.ENTITY_TYPE);
        this.consumeVanillaRegistry(content.blockEntities, Registry.BLOCK_ENTITY_TYPE);
        this.consumeVanillaRegistry(content.particleTypes, Registry.PARTICLE_TYPE);
        this.consumeVanillaRegistry(content.menus, Registry.MENU);
        this.consumeVanillaRegistry(content.recipeSerializers, Registry.RECIPE_SERIALIZER, e -> {

            final RecipeSerializer<?> wrapper = new WrappedRecipeSerializer<>(e);
            e.setWrapper(wrapper);
            return wrapper;
        });
        this.consumeVanillaRegistry(content.paintings, Registry.MOTIVE);
        this.consumeVanillaRegistry(content.attributes, Registry.ATTRIBUTE);
        this.consumeVanillaRegistry(content.stats, Registry.STAT_TYPE);
        this.consumeVanillaRegistry(content.villagerProfessions, Registry.VILLAGER_PROFESSION);

        this.consumeRegistry(content.commandArguments, (id, value) -> ArgumentTypes.register(id.toString(), value.getA(), value.getB()));

        CommandRegistrationCallback.EVENT.register((dispatcher, isDedicated) -> {

            content.commands.build((id, value) -> value.build(dispatcher, isDedicated));
        });

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
