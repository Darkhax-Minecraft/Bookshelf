/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public final class SkullUtils {
    
    /**
     * Create a skull from an instance of PlayerEntity.
     *
     * @param player The PlayerEntity to use the skin from.
     * @return ItemStack An ItemStack containing a skull that represents the passed player.
     */
    public static ItemStack createSkull (PlayerEntity player) {
        
        return createSkull(player.getGameProfile().getName(), player.getUniqueID());
    }
    
    /**
     * Creates a skull using a players UUID.
     *
     * @param name The name of the player.
     * @param uuid The UUID of the player to base the skull on.
     * @return ItemStack An ItemStack containing a skull which represents the owner of the
     *         passed UUID.
     */
    public static ItemStack createSkull (String name, UUID uuid) {
        
        final ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
        StackUtils.prepareStackTag(stack);
        final CompoundNBT ownerTag = new CompoundNBT();
        ownerTag.putString("Name", name);
        ownerTag.putString("Id", uuid.toString());
        stack.getTag().put("SkullOwner", ownerTag);
        return stack;
    }
    
    /**
     * Creates a skull that represents a player. This method can use plain text usernames, or
     * player UUID. It is recommended to use the UUID over the username, unless you are 100%
     * certain that the username will never change.
     *
     * @param owner The owner of the skull being created. Can be a username of a UUID.
     * @return ItemStack An ItemStack containing a skull which represents the passed owner
     *         name.
     */
    public static ItemStack createSkull (String owner) {
        
        final ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
        StackUtils.prepareStackTag(stack);
        stack.getTag().putString("SkullOwner", owner);
        return stack;
    }
}