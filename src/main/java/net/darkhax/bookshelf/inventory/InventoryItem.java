package net.darkhax.bookshelf.inventory;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

public class InventoryItem extends Item implements IInventory {

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
    private final ItemStack[] inventory;
    
    /**
     * The name of the inventory.
     */
    private final String name;
    
    /**
     * Constructs a new Item Inventory. This is an inventory which is stored on an Item. Such an inventory can not contain items of the same type.
     * @param invItem The item holding the inventory.
     * @param size The size of the inventory.
     * @param name The name of the inventory. 
     */
    public InventoryItem(ItemStack invItem, int size, String name) {
        
        this.invItem = invItem;
        this.size = size;
        this.inventory = new ItemStack[this.size];
        this.name = name;
        
        ItemStackUtils.prepareDataTag(invItem);
        readFromNBT();
    }
    
    /**
     * Reads the inventory data from the ItemInventory tag on {@link #invItem}.
     */
    public void readFromNBT() {
        
        NBTTagList items = this.invItem.getTagCompound().getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
        
        for (int index = 0; index < items.tagCount(); index++) {
            
            NBTTagCompound itemData = items.getCompoundTagAt(index);
            int slot = itemData.getInteger("Slot");
            
            if (slot >= 0 && slot < getSizeInventory())
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemData);
        }
    }
    
    /**
     * Writes the inventory data to the ItemInventory tag on {@link #invItem}.
     */
    public void writeToNBT() {
        
        NBTTagList items = new NBTTagList();
        
        for (int index = 0; index < getSizeInventory(); index++) {
            
            ItemStack stack = getStackInSlot(index);
            
            if (ItemStackUtils.isValidStack(stack)) {
                
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setInteger("Slot", index);
                stack.writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }
        
        this.invItem.getTagCompound().setTag("ItemInventory", items);
    }
    
    @Override
    public String getName () {
        
        return name;
    }

    @Override
    public boolean hasCustomName () {
        
        return name.length() > 0;
    }

    @Override
    public ITextComponent getDisplayName () {
        
        return new TextComponentTranslation(this.name);
    }

    @Override
    public int getSizeInventory () {
        
        return size;
    }

    @Override
    public ItemStack getStackInSlot (int index) {
        
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize (int index, int count) {
        
        ItemStack stack = getStackInSlot(index);
        
        if (ItemStackUtils.isValidStack(stack)) {
            
            if (stack.stackSize > count) {
                
                stack = stack.splitStack(count);
                markDirty();
            }
            
            else
                setInventorySlotContents(index, null);
        }
        
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot (int index) {
        
        ItemStack stack = getStackInSlot(index);
        setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents (int index, ItemStack stack) {
        
        inventory[index] = stack;
        
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
        
        markDirty();
    }

    @Override
    public int getInventoryStackLimit () {
        
        return 64;
    }

    @Override
    public void markDirty () {
        
        for (int index = 0; index < getSizeInventory(); index++)
            if (ItemStackUtils.isValidStack(getStackInSlot(index)) && getStackInSlot(index).stackSize == 0)
                inventory[index] = null;
        
        writeToNBT();
    }

    @Override
    public boolean isUseableByPlayer (EntityPlayer player) {
        
        return true;
    }

    @Override
    public void openInventory (EntityPlayer player) {

    }

    @Override
    public void closeInventory (EntityPlayer player) {
        
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
        
    }

    @Override
    public int getFieldCount () {
        
        return 0;
    }

    @Override
    public void clear () {
        
    }
}
