package net.darkhax.bookshelf.api.entity.merchant.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;

import java.util.Random;
import java.util.function.Supplier;

public class VillagerSells implements VillagerTrades.ItemListing {

    private final Supplier<ItemStack> itemToBuy;
    private final int emeraldCost;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;

    public VillagerSells(Block itemToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this(() -> new ItemStack(itemToBuy), emeraldCost, maxUses, villagerXp, priceMultiplier);
    }

    public VillagerSells(Block itemToBuy, int emeraldCost, int maxUses, int villagerXp) {

        this(itemToBuy, emeraldCost, maxUses, villagerXp, 0.05f);
    }

    public VillagerSells(Item itemToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this(itemToBuy::getDefaultInstance, emeraldCost, maxUses, villagerXp, priceMultiplier);
    }

    public VillagerSells(Item itemToBuy, int emeraldCost, int maxUses, int villagerXp) {

        this(itemToBuy, emeraldCost, maxUses, villagerXp, 0.05f);
    }

    public VillagerSells(ItemStack itemToBuy, int emeraldCost, int maxUses, int villagerXp, float priceMultiplier) {

        this(itemToBuy::copy, emeraldCost, maxUses, villagerXp, priceMultiplier);
    }

    public VillagerSells(ItemStack itemToBuy, int emeraldCost, int maxUses, int villagerXp) {

        this(itemToBuy::copy, emeraldCost, maxUses, villagerXp, 0.05f);
    }

    public VillagerSells(Supplier<ItemStack> itemToBuy, int emeraldCost, int maxUses, int villagerXp) {

        this(itemToBuy, emeraldCost, maxUses, villagerXp, 0.05f);
    }

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
}