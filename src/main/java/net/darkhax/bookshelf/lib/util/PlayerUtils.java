package net.darkhax.bookshelf.lib.util;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerUtils {
    
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
        
        return (player != null && player.worldObj != null && player.getClass() == EntityPlayerMP.class);
        // TODO Check ServerList for player
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
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     */
    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer () {
        
        return Minecraft.getMinecraft().thePlayer;
    }
}