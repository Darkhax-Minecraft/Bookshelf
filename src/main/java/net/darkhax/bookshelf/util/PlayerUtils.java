/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class PlayerUtils {

    /**
     * The UUID of the player with the eternal birthday.
     */
    public static final String BIRTHDAY_BOY_UUID = "10755ea6-9721-467a-8b5c-92adf689072c";

    /**
     * Checks if a specific player can sleep. For this to be true, a player must not already be
     * in a bed, and the world time bust be greater than 12541, but less than 23458.
     *
     * @param player: The player to check the sleepability of.
     * @return boolean: True if the player can sleep, false if they can not.
     */
    public static boolean canPlayerSleep (EntityPlayer player) {

        return !player.isPlayerSleeping() && player.isAlive() && player.world.getDayTime() > 12541 && player.world.getDayTime() < 23458;
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

        for (final EntityPlayer player : world.playerEntities) {

            if (player != null && player.getUniqueID().equals(playerID)) {

                return player;
            }
        }

        return null;
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
     * @return Whether or not the player has the item in their inventory.
     */
    public static boolean playerHasItem (EntityPlayer player, Item item) {

        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem().equals(item)) {
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
     * @return The list of found items.
     */
    public static List<ItemStack> getStacksFromPlayer (EntityPlayer player, Item item) {

        final List<ItemStack> items = new ArrayList<>();

        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() == item) {
                items.add(stack);
            }
        }

        return items;
    }

    /**
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     *
     * @return The client side player.
     */
    @OnlyIn(Dist.CLIENT)
    public static EntityPlayerSP getClientPlayer () {

        return Minecraft.getInstance().player;
    }

    /**
     * Checks if the player is currently in game. This is done by checking if the current world
     * and player exist, and if the player exists in a valid world.
     *
     * @return Whether or not the player is currently in game.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isPlayerInGame () {

        final Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.world != null && mc.player.world != null;
    }

    /**
     * Gets the UUID for the client side player.
     *
     * @return The UUID for the client side player. May be null.
     */
    @OnlyIn(Dist.CLIENT)
    public static UUID getClientUUID () {

        return fixStrippedUUID(Minecraft.getInstance().getSession().getPlayerID());
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

    /**
     * Gets a resource location that is bound to a player skin texture.
     *
     * @param profile The profile to lookup.
     * @return The texture to use for that profile.
     */
    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getPlayerTexture (GameProfile profile) {

        // Validate the profile first.
        if (profile != null) {

            final Minecraft minecraft = Minecraft.getInstance();

            // Load skin data about the profile.
            final Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);

            // If the loaded data has a skin, return that.
            if (map.containsKey(Type.SKIN)) {

                return minecraft.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
            }

            // Otherwise return a default skin for the player.
            else {

                return DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getUUID(profile));
            }
        }

        // Default to the legacy steve skin.
        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }
}