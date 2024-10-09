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
 * A simple villager trade entry that represents an item being bought by the villager.
 *
 * @param stackToBuy      The item being bought by the villager.
 * @param emeralds        The amount of emeralds to award the player.
 * @param maxUses         The amount of times the trade can be performed before a restocking is required.
 * @param villagerXp      The amount of villager XP to award for performing the trade.
 * @param priceMultiplier A price multiplier.
 */
public record VillagerBuys(Supplier<ItemCost> stackToBuy, int emeralds, int maxUses, int villagerXp, float priceMultiplier) implements VillagerTrades.ItemListing {

    @Override
    public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
        return new MerchantOffer(this.stackToBuy.get(), new ItemStack(Items.EMERALD, this.emeralds), this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}