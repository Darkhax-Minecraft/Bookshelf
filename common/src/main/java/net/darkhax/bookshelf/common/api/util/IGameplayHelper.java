package net.darkhax.bookshelf.common.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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
}