//package net.darkhax.bookshelf.impl.inventory;
//
//import net.darkhax.bookshelf.api.inventory.IInventoryAccess;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.wrapper.SidedInvWrapper;
//
//import java.util.stream.IntStream;
//
//public class InventoryAccessForge implements IInventoryAccess {
//
//    private final IItemHandler itemHandler;
//
//    public InventoryAccessForge(IItemHandler itemHandler) {
//
//        this.itemHandler = itemHandler;
//    }
//
//    @Override
//    public boolean isEmpty() {
//
//        if (this.itemHandler.getSlots() > 0) {
//
//            for (int slotId = 0; slotId < this.itemHandler.getSlots(); slotId++) {
//
//                if (!this.itemHandler.getStackInSlot(slotId).isEmpty()) {
//
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public int getSize() {
//
//        return this.itemHandler.getSlots();
//    }
//
//    @Override
//    public ItemStack getItemInSlot(int slotId) {
//
//        return this.itemHandler.getStackInSlot(slotId);
//    }
//
//    @Override
//    public boolean canInsert(int slotId, ItemStack stack) {
//
//        return this.itemHandler.isItemValid(slotId, stack);
//    }
//
//    @Override
//    public void setItemInSlot(int slotId, ItemStack stack) {
//
//        // TODO return
//        this.itemHandler.insertItem(slotId, stack, false);
//    }
//
//    @Override
//    public int getMaxStackSize(int slotId) {
//
//        return this.itemHandler.getSlotLimit(slotId);
//    }
//
//    @Override
//    public int[] getAvailableSlots(Direction side) {
//
//        return IntStream.range(0, this.itemHandler.getSlots()).toArray();
//    }
//
//    @Override
//    public boolean canInsert(int slotId, ItemStack toInsert, Direction side) {
//
//        return this.itemHandler.isItemValid(slotId, toInsert);
//    }
//
////    @Override
////    public boolean canExtract(int slotId, ItemStack toExtract, Direction side) {
////
////        return this.itemHandler.extractItem(slotId, (slotId), true);
////    }
//}
