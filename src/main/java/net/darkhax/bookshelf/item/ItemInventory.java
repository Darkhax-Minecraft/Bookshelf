/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

public class ItemInventory extends Item implements IInventory {

    /**
     * The item holding the inventory.
     */
    private final ItemStack invItem;

    /**
     * The size of the inventory.
     */
    private final int size;

    /**
     * The inventory contents.
     */
    private final NonNullList<ItemStack> inventory;

    /**
     * The name of the inventory.
     */
    private final String name;

    /**
     * Constructs a new Item Inventory. This is an inventory which is stored on an Item. Such
     * an inventory can not contain items of the same type.
     *
     * @param invItem The item holding the inventory.
     * @param size The size of the inventory.
     * @param name The name of the inventory.
     */
    public ItemInventory (ItemStack invItem, int size, String name) {

        this.invItem = invItem;
        this.size = size;
        this.inventory = NonNullList.<ItemStack> withSize(size, ItemStack.EMPTY);
        this.name = name;

        ItemStackUtils.prepareDataTag(invItem);
        this.readFromNBT();
    }

    /**
     * Reads the inventory data from the ItemInventory tag on {@link #invItem}.
     */
    public void readFromNBT () {

        final NBTTagList items = this.invItem.getTagCompound().getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

        for (int index = 0; index < items.tagCount(); index++) {

            final NBTTagCompound itemData = items.getCompoundTagAt(index);
            final int slot = itemData.getInteger("Slot");

            if (slot >= 0 && slot < this.getSizeInventory()) {
                this.inventory.set(slot, new ItemStack(itemData));
            }
        }
    }

    /**
     * Writes the inventory data to the ItemInventory tag on {@link #invItem}.
     */
    public void writeToNBT () {

        final NBTTagList items = new NBTTagList();

        for (int index = 0; index < this.getSizeInventory(); index++) {

            final ItemStack stack = this.getStackInSlot(index);

            if (!stack.isEmpty()) {

                final NBTTagCompound itemData = new NBTTagCompound();
                itemData.setInteger("Slot", index);
                stack.writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }

        this.invItem.getTagCompound().setTag("ItemInventory", items);
    }

    /**
     * Gets a List of all the ItemStack inside the inventory from a stack. Allows for access
     * without creating an entire inventory. Should only be used for reading, and never
     * writing.
     *
     * @param stack The stack to read from.
     * @return A List of the stored ItemStack.
     */
    public static List<ItemStack> getContents (ItemStack stack) {

        ItemStackUtils.prepareDataTag(stack);
        final List<ItemStack> list = new ArrayList<>();
        final NBTTagList items = stack.getTagCompound().getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

        for (int index = 0; index < items.tagCount(); index++) {
            list.add(new ItemStack(items.getCompoundTagAt(index)));
        }

        return list;
    }

    @Override
    public String getName () {

        return this.name;
    }

    @Override
    public boolean hasCustomName () {

        return this.name.length() > 0;
    }

    @Override
    public ITextComponent getDisplayName () {

        return new TextComponentTranslation(this.name);
    }

    @Override
    public int getSizeInventory () {

        return this.size;
    }

    @Override
    public ItemStack getStackInSlot (int index) {

        return this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize (int index, int count) {

        ItemStack stack = this.getStackInSlot(index);

        if (!stack.isEmpty())
            if (stack.getCount() > count) {

                stack = stack.splitStack(count);
                this.markDirty();
            }
            else {
                this.setInventorySlotContents(index, null);
            }

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot (int index) {

        final ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents (int index, ItemStack stack) {

        this.inventory.set(index, stack);

        if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit () {

        return 64;
    }

    @Override
    public void markDirty () {

        for (int index = 0; index < this.getSizeInventory(); index++)
            if (!this.getStackInSlot(index).isEmpty() && this.getStackInSlot(index).getCount() == 0) {
                this.inventory.set(index, ItemStack.EMPTY);
            }

        this.writeToNBT();
    }

    @Override
    public void openInventory (EntityPlayer player) {

        // We don't care about this.
    }

    @Override
    public void closeInventory (EntityPlayer player) {

        // We don't care about this.
    }

    @Override
    public boolean isItemValidForSlot (int index, ItemStack stack) {

        return stack.getItem() != this.invItem.getItem();
    }

    @Override
    public int getField (int id) {

        return 0;
    }

    @Override
    public void setField (int id, int value) {

        // We don't care about this.
    }

    @Override
    public int getFieldCount () {

        return 0;
    }

    @Override
    public void clear () {

        // We don't care about this.
    }

    @Override
    public boolean isEmpty () {

        for (final ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public boolean isUsableByPlayer (EntityPlayer player) {

        return true;
    }
}
