package net.darkhax.bookshelf.api.block.entity;

import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * An implementation of InventoryBlockEntity that allows the inventory to be externally accessed by stuff like hoppers.
 *
 * @param <T> The type of inventory held by the BlockEntity.
 */
public abstract class WorldlyInventoryBlockEntity<T extends Container> extends InventoryBlockEntity<T> implements WorldlyContainer {

    private final CachedSupplier<int[]> fallbackSlots = CachedSupplier.cache(() -> IntStream.range(0, getContainerSize()).toArray());

    public WorldlyInventoryBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getContainerSize() {

        return getInventory().getContainerSize();
    }

    @Override
    public boolean isEmpty() {

        return getInventory().isEmpty();
    }

    @Override
    public ItemStack getItem(int slotId) {

        return getInventory().getItem(slotId);
    }

    @Override
    public ItemStack removeItem(int slotId, int amount) {

        return getInventory().removeItem(slotId, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotId) {

        return getInventory().removeItemNoUpdate(slotId);
    }

    @Override
    public void setItem(int slotId, ItemStack stack) {

        getInventory().setItem(slotId, stack);
    }

    @Override
    public int getMaxStackSize() {

        return getInventory().getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player player) {

        return getInventory().stillValid(player);
    }

    @Override
    public void startOpen(Player player) {

        getInventory().startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {

        getInventory().stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slotId, ItemStack stack) {

        return getInventory().canPlaceItem(slotId, stack);
    }

    @Override
    public int countItem(Item item) {

        return getInventory().countItem(item);
    }

    @Override
    public boolean hasAnyOf(Set<Item> item) {

        return getInventory().hasAnyOf(item);
    }

    @Override
    public void clearContent() {

        getInventory().clearContent();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {

        if (this.getInventory() instanceof WorldlyContainer worldy) {

            return worldy.getSlotsForFace(side);
        }

        return fallbackSlots.get();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotId, ItemStack stack, @Nullable Direction side) {

        if (this.getInventory() instanceof WorldlyContainer worldly) {

            return worldly.canPlaceItemThroughFace(slotId, stack, side);
        }

        return this.getInventory().canPlaceItem(slotId, stack) && this.getItem(slotId).getCount() < this.getMaxStackSize();
    }

    @Override
    public boolean canTakeItemThroughFace(int slotId, ItemStack stack, Direction side) {

        if (this.getInventory() instanceof WorldlyContainer worldly) {

            return worldly.canTakeItemThroughFace(slotId, stack, side);
        }

        return true;
    }
}