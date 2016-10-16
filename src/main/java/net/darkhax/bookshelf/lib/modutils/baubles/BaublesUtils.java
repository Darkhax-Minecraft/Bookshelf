package net.darkhax.bookshelf.lib.modutils.baubles;

import baubles.api.BaublesApi;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public class BaublesUtils {
    
    /**
     * Checks if a player has a bauble in the amulet slot.
     * 
     * @param player The player to check for.
     * @param item The item you are looking for. Only item, meta are accounted for. See
     *        {@link ItemStackUtils#areStacksSimilar(ItemStack, ItemStack)} for more info.
     * @return Whether or not the player has a bauble in the desired slot.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean isPlayerWearingAmulet (EntityPlayer player, ItemStack item) {
        
        return isPlayerWearingBauble(player, item, 0);
    }
    
    /**
     * Checks if a player has a bauble in the ring slot. This will check both slots!
     * 
     * @param player The player to check for.
     * @param item The item you are looking for. Only item, meta are accounted for. See
     *        {@link ItemStackUtils#areStacksSimilar(ItemStack, ItemStack)} for more info.
     * @return Whether or not the player has a bauble in the desired slot.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean isPlayerWearingRing (EntityPlayer player, ItemStack item) {
        
        return isPlayerWearingBauble(player, item, 1) || isPlayerWearingBauble(player, item, 2);
    }
    
    /**
     * Checks if a player has a bauble in the belt slot.
     * 
     * @param player The player to check for.
     * @param item The item you are looking for. Only item, meta are accounted for. See
     *        {@link ItemStackUtils#areStacksSimilar(ItemStack, ItemStack)} for more info.
     * @return Whether or not the player has a bauble in the desired slot.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean isPlayerWearingBelt (EntityPlayer player, ItemStack item) {
        
        return isPlayerWearingBauble(player, item, 3);
    }
    
    /**
     * Checks if a player has a bauble in the amulet slot.
     * 
     * @param player The player to check for.
     * @param item The item you are looking for. Only item, meta are accounted for. See
     *        {@link ItemStackUtils#areStacksSimilar(ItemStack, ItemStack)} for more info.
     * @param slot The slot to check within. 0 is amulet, 1 and 2 are rings. 3 is belt.
     * @return Whether or not the player has a bauble in the desired slot.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean isPlayerWearingBauble (EntityPlayer player, ItemStack item, int slot) {
        
        return ItemStackUtils.areStacksSimilar(getBauble(player, slot), item);
    }
    
    /**
     * Retrieves the amulet from the baubles inventory.
     * 
     * @param player The player to get the item from.
     * @return The stack in the amulet slot.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getAmulet (EntityPlayer player) {
        
        return getBauble(player, 0);
    }
    
    /**
     * Retrieves a ring from the baubles inventory. Does not care which slot, will try to get
     * the first slot and then the second.
     * 
     * @param player The player to get the item from.
     * @return The first ring that could be found.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getRing (EntityPlayer player) {
        
        final ItemStack ring = getFirstRing(player);
        return ring != null ? ring : getSecondRing(player);
    }
    
    /**
     * Retrieves the ring from the first ring slot in the baubles inventory.
     * 
     * @param player The player to get the item from.
     * @return The stack in the first ring slot.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getFirstRing (EntityPlayer player) {
        
        return getBauble(player, 1);
    }
    
    /**
     * Retrieves the ring from the second ring slot in the baubles inventory.
     * 
     * @param player The player to get the item from.
     * @return The stack in the second ring slot.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getSecondRing (EntityPlayer player) {
        
        return getBauble(player, 2);
    }
    
    /**
     * Retrieves the belt ItemStack from the baubles inventory.
     * 
     * @param player The player to get the item from.
     * @return The stack in the belt slot.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getBelt (EntityPlayer player) {
        
        return getBauble(player, 3);
    }
    
    /**
     * Retrieves an ItemStack from the baubles inventory.
     * 
     * @param player The player to get the item from.
     * @param slot The slot to search in. 0 is amulet, 1 and 2 are rings. 3 is belt.
     * @return The stack in the specified bauble slot.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getBauble (EntityPlayer player, int slot) {
        
        final IInventory inv = BaublesApi.getBaubles(player);
        return inv != null ? inv.getStackInSlot(slot) : null;
    }
    
    /**
     * Equips a bauble in the desired slot if one does not already exist.
     * 
     * @param player The player to give the ring to.
     * @param item The item to set in the slot.
     * @param slot The slot to place the bauble in. 0 is amulet, 1 and 2 are rings. 3 is belt.
     * @return Whether or not the bauble was equipped.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean equipBauble (EntityPlayer player, ItemStack item, int slot) {
        
        final IInventory inv = BaublesApi.getBaubles(player);
        if (inv != null) {
            final ItemStack existing = inv.getStackInSlot(slot);
            if (existing == null) {
                inv.setInventorySlotContents(slot, item.copy());
                item.stackSize--;
                return true;
            }
        }
        return false;
    }
}
