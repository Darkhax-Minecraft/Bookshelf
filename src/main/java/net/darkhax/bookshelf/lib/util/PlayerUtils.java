package net.darkhax.bookshelf.lib.util;

import java.lang.reflect.Field;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PlayerUtils {
    
    /**
     * A reference to the curBlockDamageMP method from the PlayerControllerMP class. Used by
     * the getBlockDamage method to get the current client-side block damage amount.
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
     * Attempts to harvest blocks in an AOE based effect around where the player is looking.
     * This effect is designed to be used in conjunction with a tool, and should be used as
     * such.
     * 
     * @param player: The player who is attempting to harvest the block.
     * @param materials: An array of valid material types that this effect will be able to
     *            break.
     * @param layers: The amount of layers to break. Each layer adds one more out in every
     *            direction. For example 1 will break a 3x3 area, 2 will break a 5x5 area and 3
     *            will break a 9x9 area.
     */
    public static void tryAOEHarvest (EntityPlayer player, Material[] materials, int layers) {
        
        ItemStack stack = player.getHeldItem();
        
        if (stack != null) {
            
            ItemStackUtils.prepareDataTag(stack);
            MovingObjectPosition lookPos = MathsUtils.rayTrace(player, 4.5d);
            
            if (lookPos != null && player instanceof EntityPlayerMP) {
                
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                NBTTagCompound dataTag = stack.stackTagCompound;
                
                int x = lookPos.blockX;
                int y = lookPos.blockY;
                int z = lookPos.blockZ;
                
                if (!dataTag.hasKey("bookshelfBreaking") || !dataTag.getBoolean("bookshelfBreaking")) {
                    
                    dataTag.setBoolean("bookshelfBreaking", true);
                    int rangeX = layers;
                    int rangeY = layers;
                    int rangeZ = layers;
                    
                    switch (lookPos.sideHit) {
                        
                        case 1:
                            rangeY = 0;
                            break;
                            
                        case 3:
                            rangeZ = 0;
                            break;
                            
                        case 5:
                            rangeX = 0;
                            break;
                    }
                    
                    for (int posX = x - rangeX; posX <= x + rangeX; posX++) {
                        
                        for (int posY = y - rangeY; posY <= y + rangeY; posY++) {
                            
                            for (int posZ = z - rangeZ; posZ <= z + rangeZ; posZ++) {
                                
                                Block block = playerMP.worldObj.getBlock(posX, posY, posZ);
                                
                                for (Material mat : materials)
                                    if (block != null && mat == block.getMaterial() && block.getPlayerRelativeBlockHardness(playerMP, playerMP.worldObj, x, posY, z) > 0)
                                        playerMP.theItemInWorldManager.tryHarvestBlock(posX, posY, posZ);
                            }
                        }
                    }
                    
                    dataTag.setBoolean("bookshelfBreaking", false);
                }
            }
        }
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
