package net.darkhax.bookshelf.common.impl.registry.adapter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.darkhax.bookshelf.common.api.entity.villager.MerchantTier;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VillagerTradeAdapter {

    private final Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> villagerTrades = new HashMap<>();
    private final List<VillagerTrades.ItemListing> rareTrades = new ArrayList<>();
    private final List<VillagerTrades.ItemListing> commonTrades = new ArrayList<>();

    /**
     * Adds a new villager trade to the game.
     *
     * @param profession The profession that offers the trade.
     * @param tier       The tier of that profession that offers the trade.
     * @param trade      The trade to offer.
     */
    public void addTrade(VillagerProfession profession, int tier, VillagerTrades.ItemListing trade) {
        villagerTrades.computeIfAbsent(profession, p -> ArrayListMultimap.create()).put(tier, trade);
    }

    /**
     * Adds a new villager trade to the game.
     *
     * @param profession The profession that offers the trade.
     * @param tier       The tier of that profession that offers the trade.
     * @param trade      The trade to offer.
     */
    public void addTrade(VillagerProfession profession, MerchantTier tier, VillagerTrades.ItemListing trade) {
        this.addTrade(profession, tier.ordinal() + 1, trade);
    }

    /**
     * Adds a trade to the wandering trader.
     *
     * @param trade  The trade for the wandering trader to offer.
     * @param isRare If the trade should be added to the rare or common pool.
     */
    public void addWanderingTrade(VillagerTrades.ItemListing trade, boolean isRare) {
        (isRare ? rareTrades : commonTrades).add(trade);
    }

    /**
     * Adds a trade to the wandering traders common trades pool.
     *
     * @param trade the trade for the wandering trader to offer.
     */
    public void addCommonWanderingTrade(VillagerTrades.ItemListing trade) {
        this.addWanderingTrade(trade, false);
    }

    /**
     * Adds a trade to the wandering traders rare trades pool.
     *
     * @param trade the trade for the wandering trader to offer.
     */
    public void addRareWanderingTrade(VillagerTrades.ItemListing trade) {
        this.addWanderingTrade(trade, true);
    }

    /**
     * Gets a read-only view of the villager trades to register. Only contains trades added by the content provider.
     *
     * @return A read-only map of villager trades to register.
     */
    public Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> getVillagerTrades() {
        return Collections.unmodifiableMap(this.villagerTrades);
    }

    /**
     * Gets a read-only view of the rare wandering trader trades. Only contains trades added by the content provider.
     *
     * @return The rare wandering trader trades.
     */
    public List<VillagerTrades.ItemListing> getRareWanderingTrades() {
        return Collections.unmodifiableList(this.rareTrades);
    }

    /**
     * Gets a read-only view of the common wandering trader trades. Only contains trades added by the content provider.
     *
     * @return the common wandering trader trades.
     */
    public List<VillagerTrades.ItemListing> getCommonWanderingTrades() {
        return Collections.unmodifiableList(this.commonTrades);
    }
}
