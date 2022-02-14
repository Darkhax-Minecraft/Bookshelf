package net.darkhax.bookshelf.api.entity.merchant.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import java.util.Random;
import java.util.function.Supplier;

public class VillagerBuys implements VillagerTrades.ItemListing {

    private final Supplier<ItemStack> stackToBuy;
    private final int emeraldCost;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;

    public VillagerBuys(ItemLike item, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this(item.asItem().getDefaultInstance(), emeraldCost, maxUses, villagerXp, priceMultiplier);
    }

    public VillagerBuys(ItemStack stackToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this(stackToBuy::copy, emeraldCost, maxUses, villagerXp, priceMultiplier);
    }

    public VillagerBuys(Supplier<ItemStack> stackToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this.stackToBuy = stackToBuy;
        this.emeraldCost = emeraldCost;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(Entity entity, Random random) {

        return new MerchantOffer(this.stackToBuy.get(), new ItemStack(Items.EMERALD, this.emeraldCost), this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}