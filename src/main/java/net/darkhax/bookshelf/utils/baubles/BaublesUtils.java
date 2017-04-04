package net.darkhax.bookshelf.utils.baubles;

import java.util.ArrayList;
import java.util.List;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.darkhax.bookshelf.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public final class BaublesUtils {

    public static final int AMULTER = 0;

    public static final int RING_1 = 1;

    public static final int RING_2 = 2;

    public static final int BELT = 3;

    public static final int HEAD = 4;

    public static final int BODY = 5;

    public static final int CHARM = 6;

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private BaublesUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Checks if a player has a bauble in a given slot. Automatically called by
     * PlayerUtils#playerHasItem
     *
     * @param player The player to check.
     * @param item The item you are looking for.
     * @param meta The required meta value.
     * @return Whether or not the item was found.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean hasItem (EntityPlayer player, Item item, int meta) {

        final BaubleType type = getBaubleType(item, meta);

        if (type != null) {

            final ItemStack stack = getBauble(player, type);

            if (!stack.isEmpty())
                return stack.getItem().equals(item) && (meta < 0 || stack.getMetadata() == meta);
        }

        return false;
    }

    /**
     * Gets all stacks of a specific type from the baubles inventory. Automatically called by
     * PlayerUtils#getStacksFromPlayer.
     *
     * @param player The player to search.
     * @param item The item to search for.
     * @param meta The desired metadata for the item. If less than 0, any meta will work.
     * @return The list of found items.
     */
    @Optional.Method(modid = "Baubles")
    public static List<ItemStack> getBaublesFromPlayer (EntityPlayer player, Item item, int meta) {

        final List<ItemStack> items = new ArrayList<>();
        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

        for (int slot = 0; slot < BaubleType.TRINKET.getValidSlots().length; slot++) {

            final ItemStack stack = inv.getStackInSlot(slot);
            if (stack != null && stack.getItem() == item && (meta < 0 || stack.getMetadata() == meta)) {
                items.add(stack);
            }
        }

        return items;
    }

    /**
     * Gets the type of a bauble from a generic item.
     *
     * @param item The item to get info from.
     * @param meta The meta to use.
     * @return The bauble type that was found. Can be null.
     */
    @Optional.Method(modid = "Baubles")
    public static BaubleType getBaubleType (Item item, int meta) {

        if (item instanceof IBauble)
            return ((IBauble) item).getBaubleType(new ItemStack(item, meta));
        return null;
    }

    /**
     * Attempts to get a bauble from the player.
     *
     * @param player The player to get the bauble from.
     * @param type The type of bauble being searched for.
     * @return The first stack found.
     */
    @Optional.Method(modid = "Baubles")
    public static ItemStack getBauble (EntityPlayer player, BaubleType type) {

        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

        for (final int slotId : type.getValidSlots())
            if (inv != null) {

                final ItemStack stack = inv.getStackInSlot(slotId);

                if (stack != null)
                    return stack;
            }

        return null;
    }

    /**
     * Attempts to get a bauble from the player.
     *
     * @param player The player to get the bauble from.
     * @param type The slot to get the bauble from.
     * @return Gets the stack in the slot.
     */
    public static ItemStack getBauble (EntityPlayer player, int type) {

        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

        if (inv != null) {

            final ItemStack stack = inv.getStackInSlot(type);

            if (stack != null)
                return stack;
        }

        return null;
    }

    /**
     * Checks if a player has a bauble in their inventory.
     *
     * @param player The player to get the bauble from.
     * @param stack The stack to search for.
     * @param type The type of bauble being searched for.
     * @return Whether or not a valid stack was found.
     */
    @Optional.Method(modid = "Baubles")
    public static boolean hasBauble (EntityPlayer player, ItemStack stack, BaubleType type) {

        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);

        for (final int slotId : type.getValidSlots())
            if (inv != null && ItemStackUtils.areStacksSimilar(stack, inv.getStackInSlot(slotId)))
                return true;

        return false;
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

        final IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
        if (inv != null) {
            final ItemStack existing = inv.getStackInSlot(slot);
            if (existing == null) {
                inv.setStackInSlot(slot, item.copy());
                item.shrink(1);
                return true;
            }
        }
        return false;
    }
}
