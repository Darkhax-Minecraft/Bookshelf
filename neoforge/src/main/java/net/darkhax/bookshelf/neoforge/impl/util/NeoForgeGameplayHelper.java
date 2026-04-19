package net.darkhax.bookshelf.neoforge.impl.util;

import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.darkhax.bookshelf.common.impl.registry.adapter.CreativeModeTabAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Collection;

public class NeoForgeGameplayHelper implements IGameplayHelper {

    @Override
    public ItemStack getCraftingRemainder(ItemStack input) {
        final ItemStackTemplate remainder = input.getItem().getCraftingRemainder(input);
        return remainder != null ? remainder.create() : ItemStack.EMPTY;
    }

    @Override
    public ItemStack inventoryInsert(ServerLevel level, BlockPos pos, Direction side, ItemStack stack) {
        final ResourceHandler<ItemResource> inventory = level.getCapability(Capabilities.Item.BLOCK, pos, side);
        return inventory != null ? insertItemStacked(inventory, stack, false) : IGameplayHelper.super.inventoryInsert(level, pos, side, stack);
    }

    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
    }

    /**
     * Attempts to insert an item into an inventory, stacking it with other stackable entries before occupying new
     * slots. This logic is similar to when a player picks up an item from the ground.
     *
     * @param inventory The inventory to insert the item into.
     * @param stack     The item to be inserted.
     * @param simulate  If the transaction is simulated and should not be committed.
     * @return The remaining ItemStack that was not inserted.
     */
    public static ItemStack insertItemStacked(ResourceHandler<ItemResource> inventory, ItemStack stack, boolean simulate) {
        final int amount = stack.count();
        try (Transaction transaction = Transaction.openRoot()) {
            final int amountInserted = ResourceHandlerUtil.insertStacking(inventory, ItemResource.of(stack), amount, transaction);
            if (amountInserted > 0) {
                if (!simulate) {
                    transaction.commit();
                }
                return amountInserted >= amount ? ItemStack.EMPTY : stack.copyWithCount(amount - amountInserted);
            }
            return stack;
        }
    }

    @Override
    public void setTabOutputs(CreativeModeTab.Builder tab, CreativeModeTabAdapter.OutputBuilder output) {
        tab.displayItems((p, o) -> {
            output.build(p, new CreativeModeTabAdapter.OutputWrapper() {
                @Override
                public void accept(ItemStack stack) {
                    o.accept(stack);
                }

                @Override
                public void accept(ItemLike item) {
                    o.accept(item);
                }

                @Override
                public void acceptAll(Collection<ItemStack> stacks) {
                    o.acceptAll(stacks);
                }
            });
        });
    }
}