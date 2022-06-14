package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
import net.darkhax.bookshelf.api.util.IInventoryHelper;
import net.darkhax.bookshelf.impl.inventory.ItemHandlerInventoryAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public class InventoryHelperForge implements IInventoryHelper {

    @Override
    public Player getCraftingPlayer(Container container) {

        final Player player = ForgeHooks.getCraftingPlayer();
        return player != null ? player : IInventoryHelper.super.getCraftingPlayer(container);
    }

    @Override
    public boolean canItemsStack(ItemStack original, ItemStack toStack) {

        return ItemHandlerHelper.canItemStacksStack(toStack, original);
    }

    @Nullable
    @Override
    public IInventoryAccess getInventory(Level level, BlockPos pos, @Nullable Direction direction) {

        final BlockEntity be = level.getBlockEntity(pos);

        if (be != null) {

            final Optional<IItemHandler> inventory = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).resolve();

            if (inventory.isPresent()) {

                return new ItemHandlerInventoryAccess(inventory.get());
            }
        }

        return IInventoryHelper.super.getInventory(level, pos, direction);
    }
}