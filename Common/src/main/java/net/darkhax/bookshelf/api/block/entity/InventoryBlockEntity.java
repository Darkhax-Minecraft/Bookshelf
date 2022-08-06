package net.darkhax.bookshelf.api.block.entity;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

/**
 * An implementation of BlockEntity that holds an inventory. The inventory is persisted and synced to the client.
 *
 * @param <T> The type of inventory held by the BlockEntity.
 */
public abstract class InventoryBlockEntity<T extends Container> extends BaseContainerBlockEntity {

    /**
     * The inventory held by the block entity.
     */
    private final T inventory = this.createInventory();

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    /**
     * Gets the inventory currently held by the block entity.
     *
     * @return The inventory currently held by the block entity.
     */
    public final T getInventory() {

        return this.inventory;
    }

    /**
     * Creates a new instance of the inventory held by the block entity. The resulting inventory should be effectively a
     * clean slate that represents the default state for the inventory. Persisting the state of the inventory is managed
     * by {@link #load(net.minecraft.nbt.CompoundTag)} and
     * {@link #saveAdditional(net.minecraft.nbt.CompoundTag)}.
     * <p>
     * This method should only be invoked once per tile entity instance. The resulting value is stored with
     * {@link #inventory}.
     *
     * @return A new inventory to be held by the block entity.
     */
    public abstract T createInventory();

    /**
     * Drops the contents of the held inventory into the world. This is used in situations where the block has been
     * removed from the world and allows the inventory to drop the contents before they are destroyed.
     * <p>
     * The default behaviour will drop the entire contents of the inventory onto the ground.
     *
     * @param state The state of the block.
     * @param world The level the block is in.
     * @param pos   The position of the block.
     */
    public void dropContents(BlockState state, Level world, BlockPos pos) {

        Containers.dropContents(world, pos, this.getInventory());
    }

    /**
     * Update the state of the held inventory using state data read from NBT.
     * <p>
     * Implementations of this method must be capable of reading from the result of {@link #saveInventory()}.
     * <p>
     * The inventory instance should always be accessed using {@link #getInventory()}. While each instance of the block
     * entity will have their own unique instance of the inventory, that instance will be reused across multiple read
     * and write cycles. You must ensure that the entire state of the inventory is properly reinitialized on read.
     *
     * @param tag A tag containing the inventory state data.
     */
    public void readInventory(CompoundTag tag) {

        final NonNullList<ItemStack> tempInvStacks = NonNullList.withSize(this.getInventory().getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, tempInvStacks);
        Services.INVENTORY_HELPER.fill(this.getInventory(), tempInvStacks);
    }

    /**
     * Stores the state of the currently held inventory in an NBT tag.
     * <p>
     * Implementations of this method must be capable of writing a tag that can be read by
     * {@link #readInventory(CompoundTag)}.
     * <p>
     * The inventory instance should always be accessed using {@link #getInventory()}.
     *
     * @return A tag holding the state of the current inventory.
     */
    public CompoundTag saveInventory() {

        return ContainerHelper.saveAllItems(new CompoundTag(), Services.INVENTORY_HELPER.toList(this.getInventory()));
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);

        // Reads the state of the inventory from the tile tag.
        if (tag.contains("Inventory", Tag.TAG_COMPOUND)) {

            this.readInventory(tag.getCompound("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {

        super.saveAdditional(tag);

        // Writes the state of the inventory to the tile tag.
        tag.put("Inventory", this.saveInventory());
    }


    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {

        // Default implementation for MenuConstructor.
        return null;
    }

    @Override
    public final int getContainerSize() {

        // Delegate to the held inventory.
        return this.getInventory().getContainerSize();
    }

    @Override
    public final boolean isEmpty() {

        // Delegate to the held inventory.
        return this.getInventory().isEmpty();
    }

    @Override
    public final ItemStack getItem(int slot) {

        // Delegate tot he held inventory.
        return this.getInventory().getItem(slot);
    }

    @Override
    public final ItemStack removeItem(int slot, int amount) {

        // Delegate to the held inventory.
        return this.getInventory().removeItem(slot, amount);
    }

    @Override
    public final ItemStack removeItemNoUpdate(int slot) {

        // Delegate to the held inventory.
        return this.getInventory().removeItemNoUpdate(slot);
    }

    @Override
    public final void setItem(int slot, ItemStack stack) {

        // Delegate to the held inventory.
        this.getInventory().setItem(slot, stack);
    }

    @Override
    public final int getMaxStackSize() {

        // Delegate to the held inventory.
        return this.getInventory().getMaxStackSize();
    }

    @Override
    public final void setChanged() {

        // Delegate to the held inventory.
        this.getInventory().setChanged();
    }

    @Override
    public final boolean stillValid(Player player) {

        // Delegate to the held inventory.
        return this.getInventory().stillValid(player);
    }

    @Override
    public final void startOpen(Player player) {

        // Delegate to the held inventory.
        this.getInventory().startOpen(player);
    }

    @Override
    public final void stopOpen(Player player) {

        // Delegate to the held inventory.
        this.getInventory().stopOpen(player);
    }

    @Override
    public final boolean canPlaceItem(int slot, ItemStack stack) {

        // Delegate to the held inventory.
        return this.getInventory().canPlaceItem(slot, stack);
    }

    @Override
    public final int countItem(Item toCount) {

        // Delegate to the held inventory.
        return this.getInventory().countItem(toCount);
    }

    @Override
    public final boolean hasAnyOf(Set<Item> toFind) {

        // Delegate to the held inventory.
        return this.getInventory().hasAnyOf(toFind);
    }

    @Override
    public final void clearContent() {

        // Delegate to the held inventory.
        this.getInventory().clearContent();
    }
}