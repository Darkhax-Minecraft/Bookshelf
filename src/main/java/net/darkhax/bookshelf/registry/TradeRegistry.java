package net.darkhax.bookshelf.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TradeRegistry {

    private final Logger logger;
    private final Map<VillagerProfession, Int2ObjectMap<List<ITrade>>> trades;
    private final List<ITrade> basicTrades;
    private final List<ITrade> rareTrades;

    public TradeRegistry (Logger logger) {

        this.logger = logger;
        this.trades = new HashMap<>();
        this.basicTrades = NonNullList.create();
        this.rareTrades = NonNullList.create();
    }

    public void initialize (IEventBus modBus) {

        if (!this.trades.isEmpty()) {

            MinecraftForge.EVENT_BUS.addListener(this::registerVillagerTrades);
        }

        if (!this.basicTrades.isEmpty() || !this.rareTrades.isEmpty()) {

            MinecraftForge.EVENT_BUS.addListener(this::registerWanderingTrades);
        }
    }

    public ITrade registerVillagerTrade (VillagerProfession profession, int level, ITrade trade) {

        // Get or create a new int map
        final Int2ObjectMap<List<ITrade>> tradesByLevel = this.trades.getOrDefault(profession, new Int2ObjectOpenHashMap<>());

        // Get or create a new list of trades for the level.
        final List<ITrade> tradesForLevel = tradesByLevel.getOrDefault(level, new ArrayList<>());

        // Add the trades.
        tradesForLevel.add(trade);

        // Add the various maps and lists to their parent collection.
        tradesByLevel.put(level, tradesForLevel);
        this.trades.put(profession, tradesByLevel);
        return trade;
    }

    private void registerVillagerTrades (VillagerTradesEvent event) {

        // Ensure type isn't null, because mods.
        if (event.getType() != null) {

            // Get all trades for the current profession
            final Int2ObjectMap<List<ITrade>> tradesByLevel = this.trades.get(event.getType());

            // Check to make sure a trade has been registered.
            if (tradesByLevel != null && !tradesByLevel.isEmpty()) {

                // Iterate for the various profession levels
                for (final int level : tradesByLevel.keySet()) {

                    final List<ITrade> tradeRegistry = event.getTrades().get(level);

                    // If the trade pool exists add all trades for that tier.
                    if (tradeRegistry != null) {

                        tradeRegistry.addAll(tradesByLevel.get(level));
                    }

                    else {

                        throw new IllegalArgumentException("Tried to register a trade to a profession level that does not exist!");
                    }
                }
            }
        }
    }

    public ITrade addBasicWanderingTrade (ITrade trade) {

        this.basicTrades.add(trade);
        return trade;
    }

    public ITrade addRareWanderingTrade (ITrade trade) {

        this.rareTrades.add(trade);
        return trade;
    }

    private void registerWanderingTrades (WandererTradesEvent event) {

        event.getGenericTrades().addAll(this.basicTrades);
        event.getRareTrades().addAll(this.rareTrades);
    }
}