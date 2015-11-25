package net.darkhax.bookshelf.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;

import net.darkhax.bookshelf.lib.Constants;

public class NBTUtils {
    
    /**
     * Writes an inventory to an NBTTagCompound. Can be used to save an inventory in a
     * TileEntity, or perhaps an ItemStack.
     * 
     * @param tag: The NBTTagCompound to write the inventory to.
     * @param inventory: The inventory to write to the NBTTagCompound.
     * @return NBTTagCompound: The same NBTTagCompound that was passed to this method.
     */
    public static NBTTagCompound writeInventoryToNBT (NBTTagCompound tag, InventoryBasic inventory) {
        
        if (inventory.hasCustomName())
            tag.setString("CustomName", inventory.getCommandSenderName());
            
        NBTTagList nbttaglist = new NBTTagList();
        
        for (int slotCount = 0; slotCount < inventory.getSizeInventory(); slotCount++) {
            
            ItemStack stackInSlot = inventory.getStackInSlot(slotCount);
            
            if (stackInSlot != null) {
                
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slotCount);
                stackInSlot.writeToNBT(itemTag);
                nbttaglist.appendTag(itemTag);
            }
        }
        
        tag.setTag("Items", nbttaglist);
        
        return tag;
    }
    
    /**
     * Reads an inventory from an NBTTagCompound. Can be used to load an Inventory from a
     * TileEntity or perhaps an ItemStak.
     * 
     * @param tag: The NBTTagCompound to read the inventory data from.
     * @param inventory: The inventory to set all of the inventory data to.
     * @return InventoryBasic: The same instance of InventoryBasic that was passed to this
     *         method.
     */
    public static InventoryBasic readInventoryFromNBT (NBTTagCompound tag, InventoryBasic inventory) {
        
        if (tag.hasKey("CustomName", 8))
            inventory.setCustomName(tag.getString("CustomName"));
            
        NBTTagList items = tag.getTagList("Items", 10);
        
        for (int storedCount = 0; storedCount < items.tagCount(); storedCount++) {
            
            NBTTagCompound itemTag = items.getCompoundTagAt(storedCount);
            int slotCount = itemTag.getByte("Slot") & 0xFF;
            
            if ((slotCount >= 0) && (slotCount < inventory.getSizeInventory()))
                inventory.setInventorySlotContents(slotCount, ItemStack.loadItemStackFromNBT(itemTag));
        }
        
        return inventory;
    }
    
    /**
     * Retrieves an array of ItemStack from an NBTTagCompound. This method is intended to be
     * used with the NBT version of an IInventory and can be used when parsing things like
     * TileEntity NBT data.
     * 
     * @param tag: The tag to retrieve all of the item data from.
     * @param invSize: The projected size of the inventory stored to the tag. It is critical
     *            that this never be smaller then the actual amount.
     * @return ItemStack[]: An array of ItemStack stored on the NBTTagCompound.
     */
    public static ItemStack[] getStoredItems (NBTTagCompound tag, int invSize) {
        
        ItemStack[] inventory = null;
        
        if (tag.hasKey("Items")) {
            
            NBTTagList list = tag.getTagList("Items", 10);
            inventory = new ItemStack[invSize];
            
            for (int i = 0; i < list.tagCount(); i++) {
                
                if (!(i > list.tagCount())) {
                    
                    NBTTagCompound currentTag = list.getCompoundTagAt(i);
                    inventory[(int) currentTag.getByte("Slot")] = ItemStack.loadItemStackFromNBT(currentTag);
                }
            }
        }
        
        return inventory;
    }
    
    /**
     * Provides a way to access an NBTTagCompound that is very deep within another
     * NBTTagCompound. This will allow you to use an array of strings which represent the
     * different steps to get to the deep NBTTagCompound.
     * 
     * @param tag: An NBTTagCompound to search through.
     * @param tags: An array containing the various steps to get to the desired deep
     *            NBTTagCompound.
     * @return NBTTagCompound: This method will return the deepest possible NBTTagCompound. In
     *         some cases, this may be the tag you provide, or only a few steps deep, rather
     *         than all of the way.
     */
    public static NBTTagCompound getDeepTagCompound (NBTTagCompound tag, String[] tags) {
        
        NBTTagCompound deepTag = tag;
        
        if (tag != null)
            for (String tagName : tags)
                if (deepTag.hasKey(tagName))
                    deepTag = deepTag.getCompoundTag(tagName);
                    
        return deepTag;
    }
}
