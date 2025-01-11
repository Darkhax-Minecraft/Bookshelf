package net.darkhax.bookshelf.common.api.util;

import net.darkhax.bookshelf.common.api.registry.register.RegisterMenuScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public interface IGameplayHelper {

    RandomSource RNG = RandomSource.create();

    default ItemStack getCraftingRemainder(ItemStack input) {
        if (input.getItem().hasCraftingRemainingItem()) {
            final Item remainder = input.getItem().getCraftingRemainingItem();
            if (remainder != null) {
                return remainder.getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

    default ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        if (stack.isEmpty()) {
            return stack;
        }
        final Container container = getContainer(level, pos);
        return container != null ? HopperBlockEntity.addItem(null, container, stack, side) : stack;
    }

    @Nullable
    default Container getContainer(ServerLevel level, BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof WorldlyContainerHolder holder) {
            return holder.getContainer(state, level, pos);
        }
        final BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof Container beContainer) {
            return beContainer;
        }
        return null;
    }

    default ItemStack addItem(ItemStack stack, NonNullList<ItemStack> inventory, int[] slots) {
        for (int slot : slots) {
            if (stack.isEmpty()) {
                return stack;
            }
            final ItemStack existing = inventory.get(slot);
            if (existing.isEmpty()) {
                inventory.set(slot, stack);
                return ItemStack.EMPTY;
            }
            else if (existing.getCount() < existing.getMaxStackSize() && ItemStack.isSameItemSameComponents(existing, stack)) {
                final int availableSpace = existing.getMaxStackSize() - existing.getCount();
                final int movedAmount = Math.min(stack.getCount(), availableSpace);
                stack.shrink(movedAmount);
                existing.grow(movedAmount);
            }
        }
        return stack;
    }

    <T extends BlockEntity> BlockEntityType.Builder<T> blockEntityBuilder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks);

    <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindMenu(MenuType<? extends M> type, RegisterMenuScreen.ScreenFactory<M, U> factory);
}