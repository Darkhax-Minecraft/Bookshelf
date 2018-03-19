/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Resources;
import com.google.gson.stream.JsonReader;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerUtils {

    /**
     * A cache for storing known username uuid pairs.
     */
    public static BiMap<String, UUID> PROFILE_CACHE = HashBiMap.<String, UUID> create();

    /**
     * The UUID of the player with the eternal birthday.
     */
    public static final String BIRTHDAY_BOY_UUID = "10755ea6-9721-467a-8b5c-92adf689072c";

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private PlayerUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Checks if a specific player can sleep. For this to be true, a player must not already be
     * in a bed, and the world time bust be greater than 12541, but less than 23458.
     *
     * @param player: The player to check the sleepability of.
     * @return boolean: True if the player can sleep, false if they can not.
     */
    public static boolean canPlayerSleep (EntityPlayer player) {

        return !player.isPlayerSleeping() && player.isEntityAlive() && player.world.getWorldTime() > 12541 && player.world.getWorldTime() < 23458;
    }

    /**
     * A simple check to make sure that an EntityPlayer actually exists.
     *
     * @param player: The instance of EntityPlayer to check.
     * @return boolean: If the player exists true will be returned. If they don't false will be
     *         returned.
     */
    public static boolean isPlayerReal (Entity player) {

        return player != null && player.world != null && player.getClass() == EntityPlayerMP.class;
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

        for (final Object playerEntry : world.playerEntities) {
            if (playerEntry instanceof EntityPlayer) {

                final EntityPlayer player = (EntityPlayer) playerEntry;

                if (player.getUniqueID().equals(playerID)) {
                    return player;
                }
            }
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

        if (PROFILE_CACHE.containsValue(uuid)) {
            return PROFILE_CACHE.inverse().get(uuid);
        }

        String name = null;

        try {

            final BufferedReader reader = Resources.asCharSource(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")), StandardCharsets.UTF_8).openBufferedStream();
            final JsonReader json = new JsonReader(reader);
            json.beginObject();

            while (json.hasNext()) {
                if ("name".equals(json.nextName())) {
                    name = json.nextString();
                }
                else {
                    json.skipValue();
                }
            }

            json.endObject();
            json.close();
            reader.close();
        }

        catch (final Exception exception) {

            Constants.LOG.info("Could not get name for {}", uuid);
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

        if (PROFILE_CACHE.containsKey(username)) {
            return PROFILE_CACHE.get(username);
        }

        UUID uuid = null;

        try {

            final BufferedReader reader = Resources.asCharSource(new URL("https://api.mojang.com/users/profiles/minecraft/" + username), StandardCharsets.UTF_8).openBufferedStream();
            final JsonReader json = new JsonReader(reader);

            json.beginObject();

            while (json.hasNext()) {
                if ("id".equals(json.nextName())) {
                    uuid = fixStrippedUUID(json.nextString());
                }
                else {
                    json.skipValue();
                }
            }

            json.endObject();
            json.close();
            reader.close();
        }

        catch (final Exception exception) {

            Constants.LOG.info("Could not get name for {}", username);
        }

        return uuid;
    }

    /**
     * Attempts to fix a stripped UUID. Usually used to fix stripped uuid strings from Mojang.
     *
     * @param uuidString The UUID string to fix.
     * @return The fixed UUID, or null if the uuid string is invalid.
     */
    public static UUID fixStrippedUUID (String uuidString) {

        return uuidString.length() != 32 ? null : UUID.fromString(uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20, 32));
    }

    /**
     * Gets the amount of an item in a players inventory. Only checks main inventory and hot
     * bar. Checks the stack size of the items found.
     *
     * @param player The player to check the inventory of.
     * @param item The item to check for.
     * @return The amount of the item being searched for.
     */
    public static int getItemCountInInv (EntityPlayer player, Item item) {

        int count = 0;

        for (final ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isEmpty() && stack.getItem().equals(item)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    /**
     * Checks if a player has an item in their inventory.
     *
     * @param player The player to check the inventory of.
     * @param item The item to check for.
     * @param meta The metadata for the item to look for. Less than 0 can be used for any item.
     * @return Whether or not the player has the item in their inventory.
     */
    public static boolean playerHasItem (EntityPlayer player, Item item, int meta) {

        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem().equals(item) && (meta < 0 || stack.getMetadata() == meta)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets all stacks of a certain type from the player's inventory.
     *
     * @param player The player to search.
     * @param item The item to search for.
     * @param meta The desired metadata for the item. If less than 0, any meta will work.
     * @return The list of found items.
     */
    public static List<ItemStack> getStacksFromPlayer (EntityPlayer player, Item item, int meta) {

        final List<ItemStack> items = new ArrayList<>();

        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() == item && (meta < 0 || stack.getMetadata() == meta)) {
                items.add(stack);
            }
        }

        return items;
    }

    /**
     * Changes the dimension a player is in, in a safe way. This will make sure the player is
     * teleported to the dimension and all the correct packets are sent to keep the client in
     * sync. It also gets around teleporter code which in some cases will crash the server.
     *
     * @param player The player to change the dimension of.
     * @param dimension The dimension to send the player to.
     */
    public static void changeDimension (EntityPlayerMP player, int dimension) {

        changeDimension(player, dimension, player.getServer().getPlayerList());
    }

    /**
     * Changes the dimension a player is in, in a safe way. This will make sure the player is
     * teleported to the dimension and all the correct packets are sent to keep the client in
     * sync. It also gets around teleporter code which in some cases will crash the server.
     *
     * @param player The player to change the dimension of.
     * @param dimension The dimension to send the player to.
     * @param playerData The player data from the server.
     */
    public static void changeDimension (EntityPlayerMP player, int dimension, PlayerList playerData) {

        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, dimension)) {

            return;
        }

        final int oldDim = player.dimension;
        final boolean wasAlive = player.isEntityAlive();
        final WorldServer worldOld = playerData.getServerInstance().getWorld(player.dimension);
        final WorldServer worldNew = playerData.getServerInstance().getWorld(dimension);

        if (player.isBeingRidden()) {

            player.removePassengers();
        }

        if (player.isRiding()) {

            player.dismountRidingEntity();
        }

        player.dimension = dimension;
        player.connection.sendPacket(new SPacketRespawn(player.dimension, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
        worldOld.removeEntityDangerously(player);

        EntityUtils.changeWorld(player, worldOld, worldNew);
        playerData.preparePlayer(player, worldOld);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldNew);
        playerData.updateTimeAndWeatherForPlayer(player, worldNew);
        playerData.syncPlayerInventory(player);

        if (player.isDead && wasAlive) {

            player.isDead = false;
        }

        for (final PotionEffect potioneffect : player.getActivePotionEffects()) {

            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }

        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }

    /**
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     */
    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer () {

        return Minecraft.getMinecraft().player;
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
        return mc.player != null && mc.world != null && mc.player.world != null;
    }

    /**
     * Gets the UUID for the client side player.
     *
     * @return The UUID for the client side player. May be null.
     */
    @SideOnly(Side.CLIENT)
    public static UUID getClientUUID () {

        return fixStrippedUUID(Minecraft.getMinecraft().getSession().getPlayerID());
    }

    /**
     * Checks if it's the players birthday. A false does not necesarily mean it's not the
     * players birthday.
     *
     * @param player The player to check.
     * @return Whether or not we know it's their birthday.
     */
    public static boolean isPlayersBirthdate (EntityPlayer player) {

        return player.getUniqueID().toString().equalsIgnoreCase(BIRTHDAY_BOY_UUID);
    }

    /**
     * Checks if a DamageSource was caused by a player.
     *
     * @param source The damage source to check.
     * @return Whether or not the source was caused by a player.
     */
    public static boolean isPlayerDamage (DamageSource source) {

        return source != null && source.getTrueSource() instanceof EntityPlayer;
    }
}