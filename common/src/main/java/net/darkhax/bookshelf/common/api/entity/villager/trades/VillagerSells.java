package net.darkhax.bookshelf.common.api.entity.villager.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A simple villager trade entry that represents an item being sold to the player.
 *
 * @param itemToBuy       The item being sold by the villager.
 * @param emeraldCost     The amount of emeralds the trade will cost.
 * @param maxUses         The amount of times the trade can be performed before a restocking is required.
 * @param villagerXp      The amount of villager XP to award for performing the trade.
 * @param priceMultiplier A price multiplier.
 */
public record VillagerSells(Supplier<ItemStack> itemToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) implements VillagerTrades.ItemListing {

    @Override
    public MerchantOffer getOffer(@NotNull Entity villager, @NotNull RandomSource rng) {
        return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost), this.itemToBuy.get(), this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}