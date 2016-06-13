package net.darkhax.bookshelf.lib.util;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Resources;
import com.google.gson.stream.JsonReader;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerUtils {
    
    /**
     * A cache for storing known username uuid pairs.
     */
    public static BiMap<String, UUID> PROFILE_CACHE = HashBiMap.<String, UUID> create();
    
    /**
     * Checks if a specific player can sleep. For this to be true, a player must not already be
     * in a bed, and the world time bust be greater than 12541, but less than 23458.
     * 
     * @param player: The player to check the sleepability of.
     * @return boolean: True if the player can sleep, false if they can not.
     */
    public static boolean canPlayerSleep (EntityPlayer player) {
        
        return !player.isPlayerSleeping() && player.isEntityAlive() && player.worldObj.getWorldTime() > 12541 && player.worldObj.getWorldTime() < 23458;
    }
    
    /**
     * A simple check to make sure that an EntityPlayer actually exists.
     * 
     * @param player: The instance of EntityPlayer to check.
     * @return boolean: If the player exists true will be returned. If they don't false will be
     *         returned.
     */
    public static boolean isPlayerReal (EntityPlayer player) {
        
        return player != null && player.worldObj != null && player.getClass() == EntityPlayerMP.class;
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
        
        for (final Object playerEntry : world.playerEntities)
            if (playerEntry instanceof EntityPlayer) {
                
                final EntityPlayer player = (EntityPlayer) playerEntry;
                
                if (player.getUniqueID().equals(playerID))
                    return player;
            }
            
        return null;
    }
    
    /**
     * Attempts to get the username associated with a UUID from Mojang. If no username is
     * detected or an exception takes place, the exception message will be returned.
     * 
     * @param id The UUID to search for.
     * @return The name of the player associated to that uuid.
     */
    public static String getPlayerNameFromUUID (UUID uuid) {
        
        if (PROFILE_CACHE.containsValue(uuid))
            return PROFILE_CACHE.inverse().get(uuid);
            
        String name = null;
        
        try {
            
            final BufferedReader reader = Resources.asCharSource(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")), StandardCharsets.UTF_8).openBufferedStream();
            final JsonReader json = new JsonReader(reader);
            json.beginObject();
            
            while (json.hasNext())
                if (json.nextName().equals("name"))
                    name = json.nextString();
                    
                else
                    json.skipValue();
                    
            json.endObject();
            json.close();
            reader.close();
        }
        
        catch (final Exception exception) {
            
            Constants.LOG.warn("Could not get name for " + uuid + " " + exception.getMessage());
            name = exception.getMessage();
        }
        
        return name;
    }
    
    /**
     * Attempts to get the UUID associated with a username from Mojang. If no uuid is found,
     * null will return.
     * 
     * @param username The username to look for.
     * @return The UUID for the player, or null if it could not be found.
     */
    public static UUID getUUIDFromName (String username) {
        
        if (PROFILE_CACHE.containsKey(username))
            return PROFILE_CACHE.get(username);
            
        UUID uuid = null;
        
        try {
            
            final BufferedReader reader = Resources.asCharSource(new URL("https://api.mojang.com/users/profiles/minecraft/" + username), StandardCharsets.UTF_8).openBufferedStream();
            final JsonReader json = new JsonReader(reader);
            
            json.beginObject();
            
            while (json.hasNext())
                if (json.nextName().equals("id")) {
                    
                    final String id = json.nextString();
                    uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
                }
                
                else
                    json.skipValue();
                    
            json.endObject();
            json.close();
            reader.close();
        }
        
        catch (final Exception exception) {
            
            Constants.LOG.warn("Could not get name for " + username + " " + exception.getMessage());
        }
        
        return uuid;
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
     * Sends a spamless message to the chat. A spamless message is one that only shows up in
     * the chat once. If another version of the message were to be added to chat, the earlier
     * one would be removed.
     * 
     * @param messageID A unique message ID used to separate your message from the others. It
     *        is highly recommended to use a random number to prevent conflicts with other mods
     *        doing similar things. Each message type should have it's own ID.
     * @param message The message to send to chat, this message will replace earlier messages
     *        in the gui that use the same ID.
     */
    @SideOnly(Side.CLIENT)
    public static void sendSpamlessMessage (int messageID, ITextComponent message) {
        
        final GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        chat.printChatMessageWithOptionalDeletion(message, messageID);
    }
    
    /**
     * Checks if the player is currently in game. This is done by checking if the current world
     * and player exist, and if the player exists in a valid world.
     * 
     * @return Whether or not the player is currently in game.
     */
    @SideOnly(Side.CLIENT)
    public static boolean isPlayerInGame () {
        
        final Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.worldObj != null;
    }
}