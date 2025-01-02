package net.darkhax.bookshelf.forge.impl.util;

import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ForgeGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack getCraftingRemainder(ItemStack input) {
        final Item item = input.getItem();
        return item.hasCraftingRemainingItem(input) ? item.getCraftingRemainingItem(input) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        final BlockEntity be = level.getBlockEntity(pos);
        if (be != null && !be.isRemoved()) {
            final IItemHandler inventory = be.getCapability(ForgeCapabilities.ITEM_HANDLER, side).resolve().orElse(null);
            if (inventory != null) {
                return ItemHandlerHelper.insertItemStacked(inventory, stack, false);
            }
        }
        return IGameplayHelper.super.inventoryInsert(level, pos, side, stack);
    }
}