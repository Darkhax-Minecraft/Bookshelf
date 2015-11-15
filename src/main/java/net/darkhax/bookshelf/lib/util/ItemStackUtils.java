package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.lib.VanillaColor;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtils {
    
    /**
     * Sets a stack compound to an ItemStack if it does not already have one.
     * 
     * @param stack: ItemStack having a tag set on it.
     */
    public static NBTTagCompound prepareDataTag (ItemStack stack) {
        
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
            
        return stack.getTagCompound();
    }
    
    /**
     * Sets the lore for an ItemStack. This will override any existing lore on that item.
     * 
     * @param stack: An instance of an ItemStack to write the lore to.
     * @param lore: An array containing the lore to write. Each line is a new entry.
     * @return ItemStack: The same instance of ItemStack that was passed to this method.
     */
    public static ItemStack setLore (ItemStack stack, String[] lore) {
        
        prepareDataTag(stack);
        NBTTagCompound tag = stack.getTagCompound();
        NBTTagList loreList = new NBTTagList();
        
        if (!tag.hasKey("display", 10))
            tag.setTag("display", new NBTTagCompound());
            
        for (String line : lore)
            loreList.appendTag(new NBTTagString(line));
            
        tag.getCompoundTag("display").setTag("Lore", loreList);
        stack.setTagCompound(tag);
        
        return stack;
    }
    
    /**
     * Writes an ItemStack as a String. This method is intended for use in configuration files,
     * and allows for a damage sensitive item to be represented as a String. The format looks
     * like "itemid#damage". This method is not intended for actually saving an ItemStack.
     * 
     * @param stack: The instance of ItemStack to write.
     * @return String: A string which can be used to represent a damage sensitive item.
     */
    public static String writeStackToString (ItemStack stack) {
        
        return Item.itemRegistry.getNameForObject(stack.getItem()) + "#" + stack.getItemDamage();
    }
    
    /**
     * Reads an ItemStack from a string This method is intended for use in reading information
     * from a configuration file. The correct format is "itemid#damage". This method is
     * intended for use with writeStackToString.
     * 
     * @param stackString: The string used to construct an ItemStack.
     * @return ItemStack: An ItemStack representation of a damage sensitive item.
     */
    public static ItemStack createStackFromString (String stackString) {
        
        String[] parts = stackString.split("#");
        Object contents = Utilities.getThingByName(parts[0]);
        int damage = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;
        return (contents instanceof Item) ? new ItemStack((Item) contents, 1, damage) : new ItemStack((Block) contents, 1, damage);
    }
    
    /**
     * Retrieves the color associated with an ItemStack. This method will check the
     * OreDictionary for all items that match with a dye item. The color of that dye will be
     * returned. This is currently only for dyes.
     * 
     * @param stack: The ItemStack to check for the color.
     * @return int: An Integer based representation of a color. Java's Color can be used to
     *         convert these back into their primary components.
     */
    public static int getDyeColor (ItemStack stack) {
        
        if (ItemStackUtils.isValidStack(stack))
            for (VanillaColor color : VanillaColor.values())
                for (ItemStack oreStack : OreDictionary.getOres(color.getDyeName()))
                    if (oreStack.isItemEqual(stack))
                        return color.color.getRGB();
                        
        return -1337;
    }
    
    /**
     * Checks if an ItemStack is valid. A valid ItemStack is one that is not null, and has an
     * Item.
     * 
     * @param stack: The ItemStack to check.
     * @return boolean: True if the stack is valid, false if it is not.
     */
    public static boolean isValidStack (ItemStack stack) {
        
        return (stack != null && stack.getItem() != null);
    }
    
    /**
     * Retrieves an array of all the enchantments placed on an ItemStack. This method can be
     * used for regular items, along with enchanted books, which store enchantments under a
     * different NBTTagCompound so that the enchantment's effects won't apply for that book.
     * 
     * @param stack: The ItemStack you wish to read the enchantments from.
     * @param stored: Whether or not the stored enchantments should be read. Stored
     *            enchantments are those which do not give the ItemStack special abilities. For
     *            example, enchanted books.
     * @return Enchantment[]: An array of all the enchantments stored on the ItemStack.
     */
    public static Enchantment[] getEnchantmentsFromStack (ItemStack stack, boolean stored) {
        
        prepareDataTag(stack);
        String tagName = (stored) ? "StoredEnchantments" : "ench";
        NBTTagCompound tag = stack.stackTagCompound;
        NBTTagList list = tag.getTagList(tagName, 10);
        Enchantment[] ench = new Enchantment[list.tagCount()];
        
        for (int i = 0; i < list.tagCount(); i++)
            ench[i] = Enchantment.enchantmentsList[list.getCompoundTagAt(i).getShort("id")];
            
        return ench;
    }
    
    /**
     * Compares all ore dictionary names associated with an ItemStack, with the provided ore
     * dictionary name.
     * 
     * @param stack: The ItemStack to compare against.
     * @param oreName: The ore dictionary name to compare to.
     * @return boolean: True if any of the ore dictionary entries for the provided stack match
     *         the provided ore name.
     */
    public static boolean compareStackToOreName (ItemStack stack, String oreName) {
        
        for (int stackName : OreDictionary.getOreIDs(stack))
            if (OreDictionary.getOreName(stackName).equalsIgnoreCase(oreName))
                return true;
                
        return false;
    }
    
    /**
     * Compares all applicable ore dictionary names for two item stacks, to see if either have
     * a name in common.
     * 
     * @param firstStack: The first ItemStack to compare.
     * @param secondStack: The second ItemStack to compare.
     * @return boolean: True, if any of the ore dictionary names for either stack are the same.
     */
    public static boolean doStacksShareOreName (ItemStack firstStack, ItemStack secondStack) {
        
        for (int firstName : OreDictionary.getOreIDs(firstStack))
            for (int secondName : OreDictionary.getOreIDs(secondStack))
                if (firstName == secondName)
                    return true;
                    
        return false;
    }
    
    /**
     * Checks to see if two ItemStacks are similar. A similar stack has the same item, and the
     * same damage.
     * 
     * @param firstStack: The first stack to check.
     * @param secondStack: The second stack to check.
     * @return boolean: True if stacks are similar, and not null.
     */
    public static boolean areStacksSimilar (ItemStack firstStack, ItemStack secondStack) {
        
        return (isValidStack(firstStack) && isValidStack(secondStack) && firstStack.getItemDamage() == secondStack.getItemDamage() && firstStack.getItem() == secondStack.getItem());
    }
    
    /**
     * Retrieves the custom color of an ItemStack. This will only retrieve color data that has
     * been set through this mod. If no valid color can be found, white will be used.
     * 
     * @param stack: The ItemStack to check the color of.
     * @return int: A numeric representation of the color, that can be broken down into RGB
     *         components.
     */
    public static int getItemColor (ItemStack stack) {
        
        return stack.getTagCompound().hasKey("bookshelfColor") ? stack.getTagCompound().getInteger("bookshelfColor") : 16777215;
    }
    
    /**
     * Sets a color to an ItemStack. This color will override any color value provided by the
     * getColorFromItemStack method.
     * 
     * @param stack: The ItemStack to change the color of.
     * @param color: A numeric representation of the color, that can be broken down into RGB
     *            components.
     */
    public static void setItemColor (ItemStack stack, int color) {
        
        prepareDataTag(stack);
        stack.getTagCompound().setInteger("bookshelfColor", color);
    }
    
    /**
     * Removes all color data associated with an ItemStack. This only works for custom NBT
     * colors set by this mod.
     * 
     * @param stack: The ItemStack to remove the color from.
     */
    public static void removeItemColor (ItemStack stack) {
        
        prepareDataTag(stack);
        stack.getTagCompound().removeTag("bookshelfColor");
    }
    
    /**
     * Writes an ItemStack as a sub NBTTagCompound on a larger NBTTagCompound.
     * 
     * @param stack: The ItemStack to write to the tag.
     * @param tag: The NBTTagCompound to write the stack to.
     * @param tagName: The name for this new NBTTagCompound entry.
     */
    public static void writeStackToTag (ItemStack stack, NBTTagCompound tag, String tagName) {
        
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        tag.setTag(tagName, stackTag);
    }
}