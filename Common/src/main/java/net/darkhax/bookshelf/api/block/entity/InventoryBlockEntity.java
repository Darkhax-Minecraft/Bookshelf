package net.darkhax.bookshelf.api.block.entity;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An implementation of BlockEntity that holds an inventory. The inventory is persisted and synced to the client.
 *
 * @param <T> The type of inventory held by the BlockEntity.
 */
public abstract class InventoryBlockEntity<T extends Container> extends SerializedBlockEntity {

    private final T inventory = this.createInventory();

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    public final T getInventory() {

        return this.inventory;
    }

    public abstract T createInventory();

    @Override
    public void readTileData(CompoundTag tag) {

        final NonNullList<ItemStack> tempInvStacks = NonNullList.withSize(this.getInventory().getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, tempInvStacks);
        Services.INVENTORY_HELPER.fill(this.getInventory(), tempInvStacks);
    }

    @Override
    public void writeTileData(CompoundTag tag) {

        ContainerHelper.saveAllItems(tag, Services.INVENTORY_HELPER.toList(this.getInventory()));
    }
}