package net.darkhax.bookshelf.common.api.registry.register;

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

public class RegisterVillagerTrades {

    private final Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> villagerTrades = new HashMap<>();
    private final List<VillagerTrades.ItemListing> rareTrades = new ArrayList<>();
    private final List<VillagerTrades.ItemListing> commonTrades = new ArrayList<>();

    /**
     * Adds a new trade listing to a specific profession that may become available when the specified profession tier is
     * reached.
     *
     * @param profession The profession to register the trade to.
     * @param tier       The tier at which this trade can become available.
     * @param trade      The trade listing to add.
     */
    public void addTrade(VillagerProfession profession, int tier, VillagerTrades.ItemListing trade) {
        villagerTrades.computeIfAbsent(profession, p -> ArrayListMultimap.create()).put(tier, trade);
    }

    /**
     * Adds a new trade listing to a specific profession that may become available when the specified profession tier is
     * reached.
     *
     * @param profession The profession to register the trade to.
     * @param tier       The tier at which this trade can become available.
     * @param trade      The trade listing to add.
     */
    public void addTrade(VillagerProfession profession, MerchantTier tier, VillagerTrades.ItemListing trade) {
        this.addTrade(profession, tier.ordinal() + 1, trade);
    }

    /**
     * Adds a trade to the wandering villager trade pool.
     *
     * @param trade  The trade to add.
     * @param isRare If the trade belongs in the rare or common pool.
     */
    public void addWanderingTrade(VillagerTrades.ItemListing trade, boolean isRare) {
        (isRare ? rareTrades : commonTrades).add(trade);
    }

    /**
     * Adds a trade to the wandering traders rare trades pool.
     *
     * @param trade The trade to add.
     */
    public void addRareWanderingTrade(VillagerTrades.ItemListing trade) {
        this.addWanderingTrade(trade, true);
    }

    /**
     * Adds a trade to the wandering traders common trade pool.
     *
     * @param trade The trade to add.
     */
    public void addCommonWanderingTrade(VillagerTrades.ItemListing trade) {
        this.addWanderingTrade(trade, false);
    }

    public Map<VillagerProfession, Multimap<Integer, VillagerTrades.ItemListing>> getVillagerTrades() {
        return Collections.unmodifiableMap(this.villagerTrades);
    }

    public List<VillagerTrades.ItemListing> getRareWanderingTrades() {
        return Collections.unmodifiableList(this.rareTrades);
    }
    
    public List<VillagerTrades.ItemListing> getCommonWanderingTrades() {
        return Collections.unmodifiableList(this.commonTrades);
    }
}