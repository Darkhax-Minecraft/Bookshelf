package net.darkhax.bookshelf.lib.util;

import java.util.Comparator;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class NBTUtils {
    
    /**
     * A Comparator used to compare NBTTagCompound.
     */
    public static final Comparator<NBTTagCompound> NBT_COMPARATOR = new Comparator<NBTTagCompound>() {
        
        @Override
        public int compare (NBTTagCompound firstTag, NBTTagCompound secondTag) {
            
            if (firstTag != null && firstTag != secondTag)
                return 1;
                
            else if (secondTag != null)
                return -1;
                
            return 0;
        }
    };
    
    /**
     * Sets an unknown data type to an NBTTagCompound. If the type of the data can not be
     * identified, and exception will be thrown. Current supported data types include String,
     * Integer, Float, Boolean, Double, Long, Short, Byte, ItemStack, Entity and Position.
     * 
     * @param dataTag: An NBTTagCompound to write this unknown data to.
     * @param tagName: The name to save this unknown data under.
     * @param value: The unknown data you wish to write to the dataTag.
     */
    public static void setGenericNBTValue (NBTTagCompound dataTag, String tagName, Object value) {
        
        if (value instanceof String)
            dataTag.setString(tagName, (String) value);
            
        else if (value instanceof Integer)
            dataTag.setInteger(tagName, (Integer) value);
            
        else if (value instanceof Float)
            dataTag.setFloat(tagName, (Float) value);
            
        else if (value instanceof Boolean)
            dataTag.setBoolean(tagName, (Boolean) value);
            
        else if (value instanceof Double)
            dataTag.setDouble(tagName, (Double) value);
            
        else if (value instanceof Long)
            dataTag.setLong(tagName, (Long) value);
            
        else if (value instanceof Short)
            dataTag.setShort(tagName, (Short) value);
            
        else if (value instanceof Byte)
            dataTag.setByte(tagName, (Byte) value);
            
        else if (value instanceof ItemStack)
            dataTag.setTag(tagName, ((ItemStack) value).writeToNBT(new NBTTagCompound()));
            
        else if (value instanceof Entity) {
            
            NBTTagCompound newTag = new NBTTagCompound();
            ((Entity) value).writeToNBT(newTag);
            dataTag.setTag(tagName, newTag);
        }
        
        else
            throw new RuntimeException("The data type of " + value.getClass().getName() + " is currently not supported." + Constants.NEW_LINE + "Raw Data: " + value.toString());
    }
    
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
            tag.setString("CustomName", inventory.getName());
            
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