/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class StackUtils {
    
    /**
     * Sets a stack compound to an ItemStack if it does not already have one.
     *
     * @param stack ItemStack having a tag set on it.
     * @return The tag of the stack.
     */
    public static CompoundNBT prepareStackTag (ItemStack stack) {
        
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());
        
        return stack.getTag();
    }
    
    /**
     * Sets a stack compound to an ItemStack if it does not have one.
     *
     * @param stack The stack to set the tag of.
     * @return The stack, for convenience.
     */
    public static ItemStack prepareStack (ItemStack stack) {
        
        prepareStackTag(stack);
        return stack;
    }
    
    /**
     * Sets the lore for an ItemStack. This will completely override any existing lore for that
     * item.
     *
     * @param stack The stack to set the lore to.
     * @param lore The lore to add.
     * @return The stack that was updated.
     */
    public static ItemStack setLore (ItemStack stack, String... lore) {
        
        final ListNBT loreList = new ListNBT();
        
        for (final String line : lore)
            loreList.add(new StringNBT(line));
        
        return setLoreTag(stack, loreList);
    }
    
    /**
     * Adds lore to the ItemStack, preserving the old lore.
     *
     * @param stack The stack to append the lore to.
     * @param lore The lore to append.
     * @return The stack that was updated.
     */
    public static ItemStack appendLore (ItemStack stack, String... lore) {
        
        final ListNBT loreTag = getLoreTag(stack);
        
        for (final String line : lore)
            loreTag.add(new StringNBT(line));
        return stack;
    }
    
    /**
     * Sets the lore tag for a stack.
     *
     * @param stack The stack to update.
     * @param lore The lore tag.
     * @return The stack that was updated.
     */
    public static ItemStack setLoreTag (ItemStack stack, ListNBT lore) {
        
        final CompoundNBT displayTag = getDisplayTag(stack);
        displayTag.put("Lore", lore);
        return stack;
    }
    
    /**
     * Gets the display tag from a stack, creates it if it does not exist.
     *
     * @param stack The stack to get the display tag of.
     * @return The display tag.
     */
    public static CompoundNBT getDisplayTag (ItemStack stack) {
        
        prepareStackTag(stack);
        final CompoundNBT tag = stack.getTag();
        
        if (!tag.hasUniqueId("display"))
            tag.put("display", new CompoundNBT());
        
        return tag.getCompound("display");
    }
    
    /**
     * Gets the lore tag from a stack, creates it if it does not exist.
     *
     * @param stack The stack to get the lore tag of.
     * @return The lore tag list.
     */
    public static ListNBT getLoreTag (ItemStack stack) {
        
        final CompoundNBT displayTag = getDisplayTag(stack);
        
        if (!displayTag.hasUniqueId("Lore"))
            displayTag.put("Lore", new ListNBT());
        
        return displayTag.getList("Lore", 8);
    }
    
    /**
     * Writes an ItemStack as a sub CompoundNBT on a larger CompoundNBT.
     *
     * @param stack The ItemStack to write to the tag.
     * @param tag The CompoundNBT to write the stack to.
     * @param tagName The name for this new CompoundNBT entry.
     */
    public static void writeStackToTag (ItemStack stack, CompoundNBT tag, String tagName) {
        
        final CompoundNBT stackTag = new CompoundNBT();
        stack.write(stackTag);
        tag.put(tagName, stackTag);
    }
    
    /**
     * Safely drops an ItemStack intot he world. Used for mob drops.
     *
     * @param world The world to drop the item in.
     * @param pos The base pos to drop the item.
     * @param stack The stack to drop.
     */
    public static void dropStackInWorld (World world, BlockPos pos, ItemStack stack) {
        
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            
            final float offset = 0.7F;
            final double offX = world.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
            final double offY = world.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
            final double offZ = world.rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
            final ItemEntity entityitem = new ItemEntity(world, pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ, stack);
            entityitem.setDefaultPickupDelay();
            world.addEntity(entityitem);
        }
    }
    
    /**
     * Gets the identifier for an ItemStack. If the stack is empty or null, the id for air will
     * be given.
     *
     * @param stack The ItemStack to get the identifier of.
     * @return The identifier for the item.
     */
    public static String getStackIdentifier (ItemStack stack) {
        
        return stack != null && !stack.isEmpty() ? stack.getItem().getRegistryName().toString() : "minecraft:air";
    }
    
    /**
     * Gets an CompoundNBT from a stack without polluting the original input stack. If the
     * stack does not have a tag, you will get a new one. This new tag will NOT be set to the
     * stack automatically.
     *
     * @param stack The stack to check.
     * @return The nbt data for the stack.
     */
    public static CompoundNBT getTagCleanly (ItemStack stack) {
        
        return stack.hasTag() ? stack.getTag() : new CompoundNBT();
    }
}