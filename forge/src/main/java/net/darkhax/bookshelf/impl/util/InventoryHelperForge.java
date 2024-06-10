package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
import net.darkhax.bookshelf.api.util.IInventoryHelper;
import net.darkhax.bookshelf.impl.inventory.ItemHandlerInventoryAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

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

            final Optional<IItemHandler> inventory = be.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).resolve();

            if (inventory.isPresent()) {

                return new ItemHandlerInventoryAccess(inventory.get());
            }
        }

        return IInventoryHelper.super.getInventory(level, pos, direction);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> buf, boolean allowFakes) {
        if (allowFakes || !isFakePlayer(player)) {
            NetworkHooks.openScreen(player, provider, buf);
        }
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack) {

        return stack.getCraftingRemainingItem();
    }

    @Override
    public boolean hasCraftingRemainder(ItemStack stack) {

        return stack.hasCraftingRemainingItem();
    }

    @Override
    public boolean isFakePlayer(Player player) {

        return player instanceof FakePlayer;
    }
}