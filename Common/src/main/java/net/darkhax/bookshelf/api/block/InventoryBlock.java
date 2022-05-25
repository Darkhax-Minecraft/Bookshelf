package net.darkhax.bookshelf.api.block;

import net.darkhax.bookshelf.api.block.entity.InventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class InventoryBlock extends Block implements EntityBlock {

    public InventoryBlock(Properties properties) {

        super(properties);
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean pushed) {

        if (!newState.is(oldState.getBlock())) {

            final BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof InventoryBlockEntity invBlock) {

                Containers.dropContents(world, pos, invBlock.getInventory());
            }
        }

        super.onRemove(oldState, world, pos, newState, pushed);
    }
}