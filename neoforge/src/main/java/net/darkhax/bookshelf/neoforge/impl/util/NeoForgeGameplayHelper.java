package net.darkhax.bookshelf.neoforge.impl.util;

import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class NeoForgeGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack getCraftingRemainder(ItemStack input) {
        final Item item = input.getItem();
        return item.hasCraftingRemainingItem(input) ? item.getCraftingRemainingItem(input) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        final IItemHandler inventory = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side);
        return inventory != null ? ItemHandlerHelper.insertItemStacked(inventory, stack, false) : IGameplayHelper.super.inventoryInsert(level, pos, side, stack);
    }
}