package net.darkhax.bookshelf.api.block;

import net.darkhax.bookshelf.api.block.entity.InventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class InventoryBlock extends BaseEntityBlock {

    public InventoryBlock(Properties properties) {

        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {

        // BaseEntityBlock uses INVISIBLE by default however I have personally found that model is a more convenient default for my block entities.
        return RenderShape.MODEL;
    }
    
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {

        super.setPlacedBy(world, pos, state, placer, stack);

        // Allow the custom name of the placer item to be applied to the placed block entity.
        if (stack.hasCustomHoverName() && world.getBlockEntity(pos) instanceof BaseContainerBlockEntity container) {

            container.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean pushed) {

        // Drop the contents of the inventory.
        if (!newState.is(oldState.getBlock()) && world.getBlockEntity(pos) instanceof InventoryBlockEntity<?> invBlock) {

            invBlock.dropContents(oldState, world, pos);
        }

        super.onRemove(oldState, world, pos, newState, pushed);
    }
}