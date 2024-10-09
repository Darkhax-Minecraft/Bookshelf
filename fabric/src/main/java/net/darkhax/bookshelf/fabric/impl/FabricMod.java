package net.darkhax.bookshelf.fabric.impl;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.darkhax.bookshelf.common.api.registry.register.RegisterVillagerTrades;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.fabricmc.api.ModInitializer;
import net.minecraft.DetectedVersion;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        BookshelfMod.getInstance().init();
        this.registerVillagerTrades();
        checkForUpdates();
    }

    private void registerVillagerTrades() {
        final RegisterVillagerTrades register = new RegisterVillagerTrades();
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerTrades(register));
        for (Map.Entry<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> professionData : register.getVillagerTrades().entrySet()) {
            final Int2ObjectMap<VillagerTrades.ItemListing[]> professionTrades = VillagerTrades.TRADES.computeIfAbsent(professionData.getKey(), profession -> new Int2ObjectOpenHashMap<>());
            for (int merchantTier : professionData.getValue().keySet()) {
                final List<VillagerTrades.ItemListing> tradesForTier = new ArrayList<>(Arrays.asList(professionTrades.getOrDefault(merchantTier, new VillagerTrades.ItemListing[0])));
                tradesForTier.addAll(professionData.getValue().get(merchantTier));
                professionTrades.put(merchantTier, tradesForTier.toArray(new VillagerTrades.ItemListing[0]));
            }
        }
        final List<VillagerTrades.ItemListing> commonTrades = register.getCommonWanderingTrades();
        if (!commonTrades.isEmpty()) {
            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(1)));
            tradeData.addAll(commonTrades);
            VillagerTrades.WANDERING_TRADER_TRADES.put(1, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }
        final List<VillagerTrades.ItemListing> rareTrades = register.getRareWanderingTrades();
        if (!rareTrades.isEmpty()) {
            final List<VillagerTrades.ItemListing> tradeData = new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(2)));
            tradeData.addAll(rareTrades);
            VillagerTrades.WANDERING_TRADER_TRADES.put(2, tradeData.toArray(new VillagerTrades.ItemListing[0]));
        }
    }

    private static void checkForUpdates() {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL("https://updates.blamejared.com/get?n=" + Constants.MOD_ID + "&gv=" + DetectedVersion.BUILT_IN.getName() + "&ml=fabric").openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Constants.LOG.warn("Version checker is not available.");
            }
        }
        catch (Exception e) {
            // TODO
        }
    }
}