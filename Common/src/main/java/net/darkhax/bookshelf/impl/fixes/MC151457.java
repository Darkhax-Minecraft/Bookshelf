package net.darkhax.bookshelf.impl.fixes;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.mixin.item.AccessorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * Fixes MC-151457 https://bugs.mojang.com/browse/MC-151457
 * <p>
 * New bucket items such as fish buckets and powder snow buckets do not have a crafting remainder.
 */
public final class MC151457 {

    public static void applyFix() {

        setCraftingRemainderIfNull(Items.PUFFERFISH_BUCKET, Items.BUCKET);
        setCraftingRemainderIfNull(Items.SALMON_BUCKET, Items.BUCKET);
        setCraftingRemainderIfNull(Items.COD_BUCKET, Items.BUCKET);
        setCraftingRemainderIfNull(Items.TROPICAL_FISH_BUCKET, Items.BUCKET);
        setCraftingRemainderIfNull(Items.AXOLOTL_BUCKET, Items.BUCKET);
        setCraftingRemainderIfNull(Items.POWDER_SNOW_BUCKET, Items.BUCKET);
    }

    private static void setCraftingRemainderIfNull(Item target, Item remainder) {

        if (!target.hasCraftingRemainingItem()) {

            ((AccessorItem) target).bookshelf$setCraftingRemainder(remainder);
            Constants.LOG.info("Fixing MC-151457. Crafting remainder for {} is now {}.", Services.REGISTRIES.items().getId(target), Services.REGISTRIES.items().getId(remainder));
        }
    }
}
