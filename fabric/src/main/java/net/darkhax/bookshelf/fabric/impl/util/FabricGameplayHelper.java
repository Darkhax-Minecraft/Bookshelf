package net.darkhax.bookshelf.fabric.impl.util;

import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class FabricGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        final int initialCount = stack.getCount();
        final ItemStack result = IGameplayHelper.super.inventoryInsert(level, pos, side, stack);
        if (result.isEmpty() || result.getCount() != initialCount) {
            return result;
        }
        final Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, side);
        if (storage != null && storage.supportsInsertion()) {
            try (Transaction tx = Transaction.openOuter()) {
                final long count = storage.insert(ItemVariant.of(stack), stack.getCount(), tx);
                tx.commit();
                if (count >= stack.getCount()) {
                    return ItemStack.EMPTY;
                }
                else {
                    final ItemStack txResult = stack.copy();
                    txResult.shrink((int) count);
                    return txResult;
                }
            }
        }
        return stack;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType.Builder<T> builder(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
        BlockEntityType.BlockEntitySupplier<T> supplier = factory::apply;
        return BlockEntityType.Builder.of(supplier, validBlocks);
    }
}