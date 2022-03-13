package net.darkhax.bookshelf.api.entity.merchant.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import java.util.Random;
import java.util.function.Supplier;

public class VillagerSells implements VillagerTrades.ItemListing {

    private final Supplier<ItemStack> itemToBuy;
    private final int emeraldCost;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;

    public VillagerSells(Supplier<ItemStack> itemToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this.itemToBuy = itemToBuy;
        this.emeraldCost = emeraldCost;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(Entity villager, Random rng) {

        return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), this.itemToBuy.get(), this.maxUses, this.villagerXp, this.priceMultiplier);
    }

    public static VillagerTrades.ItemListing create(Supplier<? extends ItemLike> stackToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        return new VillagerSells(() -> stackToBuy.get().asItem().getDefaultInstance(), emeraldCost, maxUses, villagerXp, priceMultiplier);
    }
}