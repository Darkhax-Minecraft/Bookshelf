package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.lib.VanillaColor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.oredict.OreDictionary;

public final class ItemStackUtils {
    
    /**
     * Sets a stack compound to an ItemStack if it does not already have one.
     * 
     * @param stackItemStack having a tag set on it.
     */
    public static NBTTagCompound prepareDataTag (ItemStack stack) {
        
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
            
        return stack.getTagCompound();
    }
    
    /**
     * Sets the lore for an ItemStack. This will override any existing lore on that item.
     * 
     * @param stackAn instance of an ItemStack to write the lore to.
     * @param loreAn array containing the lore to write. Each line is a new entry.
     * @return ItemStackThe same instance of ItemStack that was passed to this method.
     */
    public static ItemStack setLore (ItemStack stack, String[] lore) {
        
        prepareDataTag(stack);
        final NBTTagCompound tag = stack.getTagCompound();
        final NBTTagList loreList = new NBTTagList();
        
        if (!tag.hasKey("display", 10))
            tag.setTag("display", new NBTTagCompound());
            
        for (final String line : lore)
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
     * @param stackThe instance of ItemStack to write.
     * @return StringA string which can be used to represent a damage sensitive item.
     */
    public static String writeStackToString (ItemStack stack) {
        
        return stack.getItem().getRegistryName().toString() + "#" + stack.getItemDamage();
    }
    
    /**
     * Reads an ItemStack from a string This method is intended for use in reading information
     * from a configuration file. The correct format is "itemid#damage". This method is
     * intended for use with writeStackToString.
     * 
     * @param stackStringThe string used to construct an ItemStack.
     * @return ItemStackAn ItemStack representation of a damage sensitive item.
     */
    public static ItemStack createStackFromString (String stackString) {
        
        final String[] parts = stackString.split("#");
        final Object contents = getThingByName(parts[0]);
        final int damage = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        return contents instanceof Item ? new ItemStack((Item) contents, 1, damage) : new ItemStack((Block) contents, 1, damage);
    }
    
    /**
     * Retrieves the color associated with an ItemStack. This method will check the
     * OreDictionary for all items that match with a dye item. The color of that dye will be
     * returned. This is currently only for dyes.
     * 
     * @param stackThe ItemStack to check for the color.
     * @return intAn Integer based representation of a color. Java's Color can be used to
     *         convert these back into their primary components.
     */
    public static int getDyeColor (ItemStack stack) {
        
        if (ItemStackUtils.isValidStack(stack))
            for (final VanillaColor color : VanillaColor.values())
                for (final ItemStack oreStack : OreDictionary.getOres(color.getDyeName()))
                    if (oreStack.isItemEqual(stack))
                        return color.color.getRGB();
                        
        return -1337;
    }
    
    /**
     * Checks if an ItemStack is valid. A valid ItemStack is one that is not null, and has an
     * Item.
     * 
     * @param stackThe ItemStack to check.
     * @return booleanTrue if the stack is valid, false if it is not.
     */
    public static boolean isValidStack (ItemStack stack) {
        
        return stack != null && stack.getItem() != null;
    }
    
    /**
     * Compares all ore dictionary names associated with an ItemStack, with the provided ore
     * dictionary name.
     * 
     * @param stackThe ItemStack to compare against.
     * @param oreNameThe ore dictionary name to compare to.
     * @return booleanTrue if any of the ore dictionary entries for the provided stack match
     *         the provided ore name.
     */
    public static boolean compareStackToOreName (ItemStack stack, String oreName) {
        
        for (final int stackName : OreDictionary.getOreIDs(stack))
            if (OreDictionary.getOreName(stackName).equalsIgnoreCase(oreName))
                return true;
                
        return false;
    }
    
    /**
     * Compares all applicable ore dictionary names for two item stacks, to see if either have
     * a name in common.
     * 
     * @param firstStackThe first ItemStack to compare.
     * @param secondStackThe second ItemStack to compare.
     * @return booleanTrue, if any of the ore dictionary names for either stack are the same.
     */
    public static boolean doStacksShareOreName (ItemStack firstStack, ItemStack secondStack) {
        
        for (final int firstName : OreDictionary.getOreIDs(firstStack))
            for (final int secondName : OreDictionary.getOreIDs(secondStack))
                if (firstName == secondName)
                    return true;
                    
        return false;
    }
    
    /**
     * Checks to see if two ItemStacks are similar. A similar stack has the same item, and the
     * same damage.
     * 
     * @param firstStackThe first stack to check.
     * @param secondStackThe second stack to check.
     * @return booleanTrue if stacks are similar, or if both are null.
     */
    public static boolean areStacksSimilar (ItemStack firstStack, ItemStack secondStack) {
        
        return firstStack == null && secondStack == null ? true : isValidStack(firstStack) && isValidStack(secondStack) && firstStack.getItemDamage() == secondStack.getItemDamage() && firstStack.getItem() == secondStack.getItem();
    }
    
    /**
     * Checks to see if two ItemStacks are similar. A similar stack has the same item, and the
     * same damage and same size.
     * 
     * @param firstStackThe first stack to check.
     * @param secondStackThe second stack to check.
     * @return booleanTrue if stacks are similar, or if both are null.
     */
    public static boolean areStacksSimilarWithSize (ItemStack firstStack, ItemStack secondStack) {
        
        return firstStack == null && secondStack == null ? true : isValidStack(firstStack) && isValidStack(secondStack) && firstStack.getItemDamage() == secondStack.getItemDamage() && firstStack.getItem() == secondStack.getItem() && firstStack.stackSize == secondStack.stackSize;
    }
    
    public static ItemStack writePotionEffectsToStack (ItemStack stack, PotionEffect[] effects) {
        
        final NBTTagCompound stackTag = prepareDataTag(stack);
        final NBTTagList potionTag = new NBTTagList();
        
        for (final PotionEffect effect : effects)
            potionTag.appendTag(effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            
        stackTag.setTag("CustomPotionEffects", potionTag);
        return stack;
    }
    
    /**
     * Writes an ItemStack as a sub NBTTagCompound on a larger NBTTagCompound.
     * 
     * @param stackThe ItemStack to write to the tag.
     * @param tagThe NBTTagCompound to write the stack to.
     * @param tagNameThe name for this new NBTTagCompound entry.
     */
    public static void writeStackToTag (ItemStack stack, NBTTagCompound tag, String tagName) {
        
        final NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        tag.setTag(tagName, stackTag);
    }
    
    /**
     * Safely decreases the amount of items held by an ItemStack.
     * 
     * @param stackThe ItemStack to decrease the size of.
     * @param amountThe amount to decrease the stack size by.
     * @return ItemStackNull, if the stack size is smaller than 1.
     */
    public static ItemStack decreaseStackSize (ItemStack stack, int amount) {
        
        stack.stackSize -= amount;
        return stack.stackSize <= 0 ? null : stack;
    }
    
    /**
     * Checks if two given ItemStack are equal. For them to be equal, both must be null, or
     * both must have a null item, or both must share a damage value. If either stack has a
     * wild card damage value, they will also be considered the same. If the checkNBT parameter
     * is true, they will also need the same item nbt.
     * 
     * @param firstStack The first ItemStack to compare.
     * @param secondStack The second ItemStack to compare.
     * @param checkNBT Should NBT be checked as well?
     * @return boolean Whether or not the items are close enough to be called the same.
     */
    public static boolean areStacksEqual (ItemStack firstStack, ItemStack secondStack, boolean checkNBT) {
        
        if (firstStack == null || secondStack == null)
            return firstStack == secondStack;
            
        final Item firstItem = firstStack.getItem();
        final Item secondItem = secondStack.getItem();
        
        if (firstItem == null || secondItem == null)
            return firstItem == secondItem;
            
        if (firstItem == secondItem) {
            
            if (checkNBT && NBTUtils.NBT_COMPARATOR.compare(firstStack.getTagCompound(), secondStack.getTagCompound()) != 0)
                return false;
                
            return firstStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || secondStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || firstStack.getItemDamage() == secondStack.getItemDamage();
        }
        
        return false;
    }
    
    /**
     * A check to see if an ItemStack exists within an array of other ItemStack.
     * 
     * @param stackThe ItemStack you are searching for.
     * @param checkNBTShould the stacks need the same NBT for them to be the same?
     * @param stacksThe array of ItemStack to search through.
     * @return booleanWhether or not the array contains the stack you are looking for.
     */
    public static boolean isStackInArray (ItemStack stack, boolean checkNBT, ItemStack... stacks) {
        
        for (final ItemStack currentStack : stacks)
            if (areStacksEqual(stack, currentStack, checkNBT))
                return true;
                
        return false;
    }
    
    /**
     * Copies an ItemStack with a new size value.
     * 
     * @param stack The ItemStack to copy.
     * @param size The new size to set for the stack.
     * @return ItemStack A new ItemStack with the same item and meta as the original, but with
     *         a new size.
     */
    public static ItemStack copyStackWithSize (ItemStack stack, int size) {
        
        final ItemStack output = stack.copy();
        output.stackSize = size;
        return output;
    }
    
    /**
     * A blend between the itemRegistry.getObject and bockRegistry.getObject methods. Used for
     * grabbing something from an ID, when you have no clue what it might be.
     *
     * @param name The ID of the thing you're looking for. Domains are often preferred.
     * @return Hopefully the thing you're looking for.
     */
    public static Object getThingByName (String name) {
        
        Object thing = Item.getByNameOrId(name);
        
        if (thing != null)
            return thing;
            
        thing = Block.getBlockFromName(name);
        
        if (thing != null)
            return thing;
            
        return null;
    }
    
    /**
     * Safely gets a block instance from an ItemStack. If the ItemStack is not valid, null will
     * be returned. Null can also be returned if the Item does not have a block form.
     * 
     * @param stack The ItemStack to get a block from.
     * @return The block version of the item contained in the ItemStack.
     */
    public static Block getBlockFromStack (ItemStack stack) {
        
        return isValidStack(stack) ? Block.getBlockFromItem(stack.getItem()) : null;
    }
}