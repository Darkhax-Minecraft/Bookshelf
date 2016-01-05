package net.darkhax.bookshelf.lib.util;

import java.lang.reflect.Field;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerUtils {
    
    /**
     * Access to the eventHandler field in an InventoryCrafting instance.
     */
    private static final Field eventHandler = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");
    
    /**
     * Access to the thePlayer field in a ContainerPlayer instance.
     */
    private static final Field containerPlayer = ReflectionHelper.findField(ContainerPlayer.class, "thePlayer", "field_82862_h");
    
    /**
     * Access to the thePlayer field in a SlotCrafting instance.
     */
    private static final Field slotPlayer = ReflectionHelper.findField(SlotCrafting.class, "thePlayer", "field_75238_b");
    
    /**
     * Access to the curBlockDamageMP field in the client-side PlayerControllerMP instance.
     */
    @SideOnly(Side.CLIENT)
    public static Field currentBlockDamage;
    
    /**
     * Checks if a specific player can sleep. For this to be true, a player must not already be
     * in a bed, and the world time bust be greater than 12541, but less than 23458.
     * 
     * @param player: The player to check the sleepability of.
     * @return boolean: True if the player can sleep, false if they can not.
     */
    public static boolean canPlayerSleep (EntityPlayer player) {
        
        return (!player.isPlayerSleeping() && player.isEntityAlive() && player.worldObj.getWorldTime() > 12541 && player.worldObj.getWorldTime() < 23458);
    }
    
    /**
     * A simple check to make sure that an EntityPlayer actually exists.
     * 
     * @param player: The instance of EntityPlayer to check.
     * @return boolean: If the player exists true will be returned. If they don't false will be
     *         returned.
     */
    public static boolean isPlayerReal (EntityPlayer player) {
        
        if (player == null || player.worldObj == null || player.getClass() != EntityPlayerMP.class)
            return false;
            
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
    }
    
    /**
     * Retrieves an instance of EntityPlayer based on a UUID. For this to work, the player must
     * currently be online, and within the world.
     * 
     * @param world: The world in which the target player resides.
     * @param playerID: A unique identifier associated with the target player.
     * @return EntityPlayer: If the target player is online and within the targeted world,
     *         their EntityPlayer instance will be returned. If the player is not found, null
     *         will be returned.
     */
    public static EntityPlayer getPlayerFromUUID (World world, UUID playerID) {
        
        for (Object playerEntry : world.playerEntities) {
            
            if (playerEntry instanceof EntityPlayer) {
                
                EntityPlayer player = (EntityPlayer) playerEntry;
                
                if (player.getUniqueID().equals(playerID))
                    return player;
            }
        }
        
        return null;
    }
    
    /**
     * Retrieves the player that is crafting in an InventoryCrafting. This is done through
     * reflection. The first attempt will try to get the player from a ContainerPlayer, and the
     * second attempt tries a ContainerWorkbench. If no player is found, null will be thrown.
     * 
     * @param inventory: An instance of the InventoryCrafting being used.
     * @return EntityPlayer: The EntityPlayer that is using the InventoryCrafting. If none is
     *         found, null will be returned.
     */
    public static EntityPlayer getPlayerFromCrafting (InventoryCrafting inventory) {
        
        try {
            
            Container container = (Container) eventHandler.get(inventory);
            
            if (container instanceof ContainerPlayer)
                return (EntityPlayer) containerPlayer.get(container);
                
            else if (container instanceof ContainerWorkbench)
                return (EntityPlayer) slotPlayer.get(container.getSlot(0));
                
        }
        
        catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     */
    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer () {
        
        return Minecraft.getMinecraft().thePlayer;
    }
    
    /**
     * A client sided method used to retrieve the progression of the block currently being
     * mined by the player. This method is client side only, and refers to only the one
     * instance of the player. Do not try to use this method to get data for multiple players,
     * or for server sided things.
     * 
     * @return float: A float value representing how much time is left for the block being
     *         broken to break. 0 = no damage has been done. 1 = the block is broken.
     */
    @SideOnly(Side.CLIENT)
    public static float getBlockDamage () {
        
        if (currentBlockDamage == null)
            return 0;
            
        try {
            
            return currentBlockDamage.getFloat(Minecraft.getMinecraft().playerController);
        }
        
        catch (IllegalArgumentException e) {
        
        }
        
        catch (IllegalAccessException e) {
        
        }
        
        return 0;
    }
}