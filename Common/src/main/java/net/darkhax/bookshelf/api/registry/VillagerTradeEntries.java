package net.darkhax.bookshelf.api.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.api.entity.merchant.MerchantTier;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillagerTradeEntries {

    private final Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> villagerTrades = new HashMap<>();
    private final List<VillagerTrades.ItemListing> rareTrades = new ArrayList<>();
    private final List<VillagerTrades.ItemListing> commonTrades = new ArrayList<>();

    public void addTrade(VillagerProfession profession, int tier, VillagerTrades.ItemListing trade) {

        villagerTrades.computeIfAbsent(profession, p -> ArrayListMultimap.create()).put(tier, trade);
    }

    public void addTrade(VillagerProfession profession, MerchantTier tier, VillagerTrades.ItemListing trade) {

        this.addTrade(profession, tier.ordinal() + 1, trade);
    }

    public void addWanderingTrade(VillagerTrades.ItemListing trade, boolean isRare) {

        (isRare ? rareTrades : commonTrades).add(trade);
    }

    public void addRareWanderingTrade(VillagerTrades.ItemListing trade) {

        this.addWanderingTrade(trade, true);
    }

    public void addCommonWanderingTrade(VillagerTrades.ItemListing trade) {

        this.addWanderingTrade(trade, false);
    }

    public Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> getVillagerTrades() {

        return this.villagerTrades;
    }

    public List<VillagerTrades.ItemListing> getRareWanderingTrades() {

        return this.rareTrades;
    }

    public List<VillagerTrades.ItemListing> getCommonWanderingTrades() {

        return this.commonTrades;
    }
}