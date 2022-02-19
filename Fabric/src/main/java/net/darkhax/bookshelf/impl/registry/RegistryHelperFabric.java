package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryHelper;
import net.darkhax.bookshelf.impl.item.CreativeTabBuilderFabric;
import net.darkhax.bookshelf.impl.resources.WrappedReloadListener;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RegistryHelperFabric extends RegistryHelper {

    public RegistryHelperFabric(String ownerId) {

        super(ownerId);
    }

    @Override
    public ICreativeTabBuilder<?> createTabBuilder(String tabId) {

        return new CreativeTabBuilderFabric(new ResourceLocation(this.ownerId, tabId));
    }

    @Override
    public void init() {

        this.consumeVanillaRegistry(blocks, Registry.BLOCK);
        this.consumeVanillaRegistry(items, Registry.ITEM);
        this.consumeVanillaRegistry(enchantments, Registry.ENCHANTMENT);
        this.consumeVanillaRegistry(paintings, Registry.MOTIVE);
        this.consumeVanillaRegistry(mobEffects, Registry.MOB_EFFECT);
        this.consumeVanillaRegistry(attributes, Registry.ATTRIBUTE);
        this.consumeVanillaRegistry(villagerProfessions, Registry.VILLAGER_PROFESSION);

        this.serverReloadListeners.getEntries().forEach((k, v) -> ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new WrappedReloadListener(k, v)));

        if (Services.PLATFORM.isPhysicalClient()) {

            this.clientReloadListeners.getEntries().forEach((k, v) -> ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedReloadListener(k, v)));
        }

        CommandRegistrationCallback.EVENT.register(this::buildCommands);
        this.registerTradeData();
        this.registerWanderingTrades();
        this.registerCommandArguments();
    }

    private void registerCommandArguments () {

        for (Map.Entry<ResourceLocation, Tuple<Class, ArgumentSerializer>> entry : this.commandArguments.getEntries().entrySet()) {

            ArgumentTypes.register(entry.getKey().toString(), entry.getValue().getA(), entry.getValue().getB());
        }
    }

    private void registerTradeData() {

        for (Map.Entry<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> professionData : this.trades.getVillagerTrades().entrySet()) {

            final Int2ObjectMap<VillagerTrades.ItemListing[]> professionTrades = VillagerTrades.TRADES.computeIfAbsent(professionData.getKey(), profession -> new Int2ObjectOpenHashMap<>());

            for (int merchantTier : professionData.getValue().keySet()) {

                final List<VillagerTrades.ItemListing> tradesForTier = new ArrayList<>(Arrays.asList(professionTrades.getOrDefault(merchantTier, new VillagerTrades.ItemListing[0])));
                tradesForTier.addAll(professionData.getValue().get(merchantTier));
                professionTrades.put(merchantTier, tradesForTier.toArray(new VillagerTrades.ItemListing[0]));
            }
        }
    }

    private void registerWanderingTrades() {

        if (!this.trades.getCommonWanderingTrades().isEmpty()) {

            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(1)));
            tradeData.addAll(this.trades.getCommonWanderingTrades());
            VillagerTrades.WANDERING_TRADER_TRADES.put(1, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }

        if (!this.trades.getRareWanderingTrades().isEmpty()) {

            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(2)));
            tradeData.addAll(this.trades.getRareWanderingTrades());
            VillagerTrades.WANDERING_TRADER_TRADES.put(2, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }
    }

    private void buildCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean isDedicated) {

        this.commands.forEach(builder -> builder.build(dispatcher, isDedicated));
    }

    private <T> void consumeVanillaRegistry(IRegistryEntries<T> toRegister, Registry<T> registry) {

        toRegister.getEntries().forEach((id, value) -> Registry.register(registry, id, value));
    }
}