package net.darkhax.bookshelf.common.api.entity.villager.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A simple villager trade entry that selects a random offer from an equally weighted array of offers.
 *
 * @param offers An equally weighted array of offers.
 */
public record VillagerOffers(Supplier<MerchantOffer>... offers) implements VillagerTrades.ItemListing {
    @Override
    public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource randomSource) {
        return offers[randomSource.nextInt(offers.length)].get();
    }
}