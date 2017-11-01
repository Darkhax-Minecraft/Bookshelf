/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public final class InventoryUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private InventoryUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Write an inventory as an NBTTagCompound. See {@link #readInventory(NBTTagCompound)} for
     * how this can be read.
     *
     * @param inventory The inventory to write. Index of item determines the slot.
     * @return An NBTTagCompound which holds all of the info for the inventory.
     */
    public static NBTTagCompound writeInventory (NonNullList<ItemStack> inventory) {

        final NBTTagList items = new NBTTagList();

        for (int slot = 0; slot < inventory.size(); slot++) {

            if (!inventory.get(slot).isEmpty()) {

                final NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", slot);
                inventory.get(slot).writeToNBT(itemTag);
                items.appendTag(itemTag);
            }
        }

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Items", items);
        tag.setInteger("Size", inventory.size());
        return tag;
    }

    /**
     * Reads an inventory from an NBTTagCompound. Check {@link #writeInventory(NonNullList)}
     * for the type of tag supported. Size of the inventory is set based on the item tag list
     * size.
     *
     * @param tag The NBT tag to read the inventory data from.
     * @return A NonNullList which contains the read inventory contents.
     */
    public static NonNullList<ItemStack> readInventory (NBTTagCompound tag) {

        final NonNullList<ItemStack> stacks = NonNullList.withSize(tag.getInteger("Size"), ItemStack.EMPTY);

        final NBTTagList tagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++) {

            final NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("Slot");
            stacks.set(slot, new ItemStack(itemTag));
        }

        return stacks;
    }
}