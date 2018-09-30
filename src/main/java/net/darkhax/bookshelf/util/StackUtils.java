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

import javax.annotation.Nonnull;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public final class StackUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private StackUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Sets a stack compound to an ItemStack if it does not already have one.
     *
     * @param stack ItemStack having a tag set on it.
     */
    public static NBTTagCompound prepareStackTag (ItemStack stack) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound();
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
     * Sets the lore for an ItemStack. This will override any existing lore on that item.
     *
     * @param stackAn instance of an ItemStack to write the lore to.
     * @param loreAn array containing the lore to write. Each line is a new entry.
     * @return ItemStackThe same instance of ItemStack that was passed to this method.
     */

    /**
     * Sets the lore for an ItemStack. This will completely override any existing lore for that
     * item.
     *
     * @param stack The stack to set the lore to.
     * @param lore The lore to add.
     * @return The stack that was updated.
     */
    public static ItemStack setLore (ItemStack stack, String... lore) {

        final NBTTagList loreList = new NBTTagList();

        for (final String line : lore) {
            loreList.appendTag(new NBTTagString(line));
        }

        return setLoreTag(stack, loreList);
    }

    /**
     * Reads the lore strings off of an ItemStack.
     *
     * @param stack The ItemStack to read.
     * @return The lore on the stack. Can be empty.
     */
    public static List<String> getLore (ItemStack stack) {

        final List<String> result = new ArrayList<>();
        final NBTTagList lore = getLoreTag(stack);

        if (!lore.isEmpty()) {
            for (int l1 = 0; l1 < lore.tagCount(); ++l1) {
                result.add(lore.getStringTagAt(l1));
            }
        }

        return result;
    }

    /**
     * Adds lore to the ItemStack, preserving the old lore.
     *
     * @param stack The stack to append the lore to.
     * @param lore The lore to append.
     * @return The stack that was updated.
     */
    public static ItemStack appendLore (ItemStack stack, String... lore) {

        final NBTTagList loreTag = getLoreTag(stack);

        for (final String line : lore) {
            loreTag.appendTag(new NBTTagString(line));
        }
        return stack;
    }

    /**
     * Sets the lore tag for a stack.
     *
     * @param stack The stack to update.
     * @param lore The lore tag.
     * @return The stack that was updated.
     */
    public static ItemStack setLoreTag (ItemStack stack, NBTTagList lore) {

        final NBTTagCompound displayTag = getDisplayTag(stack);
        displayTag.setTag("Lore", lore);
        return stack;
    }

    /**
     * Gets the display tag from a stack, creates it if it does not exist.
     *
     * @param stack The stack to get the display tag of.
     * @return The display tag.
     */
    public static NBTTagCompound getDisplayTag (ItemStack stack) {

        prepareStackTag(stack);
        final NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey("display", 10)) {
            tag.setTag("display", new NBTTagCompound());
        }

        return tag.getCompoundTag("display");
    }

    /**
     * Gets the lore tag from a stack, creates it if it does not exist.
     *
     * @param stack The stack to get the lore tag of.
     * @return The lore tag list.
     */
    public static NBTTagList getLoreTag (ItemStack stack) {

        final NBTTagCompound displayTag = getDisplayTag(stack);

        if (!displayTag.hasKey("Lore")) {

            displayTag.setTag("Lore", new NBTTagList());
        }

        return displayTag.getTagList("Lore", 8);
    }

    /**
     * Writes an ItemStack as a String. This method is intended for use in configuration files,
     * and allows for a damage sensitive item to be represented as a String. The format looks
     * like "itemid#damage". This method is not intended for actually saving an ItemStack.
     *
     * @param stack The instance of ItemStack to write.
     * @return String A string which can be used to represent a damage sensitive item.
     */
    public static String writeStackToString (ItemStack stack) {

        return stack.getItem().getRegistryName().toString() + "#" + stack.getItemDamage();
    }

    /**
     * Reads an ItemStack from a string This method is intended for use in reading information
     * from a configuration file. The correct format is "itemid#damage". This method is
     * intended for use with writeStackToString.
     *
     * @param stackString The string used to construct an ItemStack.
     * @return ItemStackAn ItemStack representation of a damage sensitive item.
     */
    public static ItemStack createStackFromString (String stackString) {

        final String[] parts = stackString.split("#");
        final Object contents = getThingByName(parts[0]);
        final int damage = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        return contents instanceof Item ? new ItemStack((Item) contents, 1, damage) : new ItemStack((Block) contents, 1, damage);
    }

    /**
     * Compares all ore dictionary names associated with an ItemStack, with the provided ore
     * dictionary name.
     *
     * @param stack The ItemStack to compare against.
     * @param oreName The ore dictionary name to compare to.
     * @return booleanTrue if any of the ore dictionary entries for the provided stack match
     *         the provided ore name.
     */
    public static boolean compareStackToOreName (ItemStack stack, String oreName) {

        for (final int stackName : OreDictionary.getOreIDs(stack)) {
            if (OreDictionary.getOreName(stackName).equalsIgnoreCase(oreName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compares all applicable ore dictionary names for two item stacks, to see if either have
     * a name in common.
     *
     * @param firstStack The first ItemStack to compare.
     * @param secondStack The second ItemStack to compare.
     * @return booleanTrue, if any of the ore dictionary names for either stack are the same.
     */
    public static boolean doStacksShareOreName (ItemStack firstStack, ItemStack secondStack) {

        for (final int firstName : OreDictionary.getOreIDs(firstStack)) {
            for (final int secondName : OreDictionary.getOreIDs(secondStack)) {
                if (firstName == secondName) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks to see if two ItemStacks are similar. A similar stack has the same item, and the
     * same damage.
     *
     * @param firstStack The first stack to check.
     * @param secondStack The second stack to check.
     * @return booleanTrue if stacks are similar, or if both are null.
     */
    public static boolean areStacksSimilar (@Nonnull ItemStack firstStack, @Nonnull ItemStack secondStack) {

        return firstStack.getItem() == secondStack.getItem() && areStacksSameMeta(firstStack, secondStack);
    }

    /**
     * Checks to see if two ItemStacks are similar. A similar stack has the same item, and the
     * same damage and same size.
     *
     * @param firstStack The first stack to check.
     * @param secondStack The second stack to check.
     * @return booleanTrue if stacks are similar, or if both are null.
     */
    public static boolean areStacksSimilarWithSize (ItemStack firstStack, ItemStack secondStack) {

        return areStacksSimilar(firstStack, secondStack) && firstStack.getCount() == secondStack.getCount();
    }

    /**
     * Checks if two item stacks are similar, and the first stack contains all nbt of the
     * second stack.
     *
     * @param firstStack The first stack to check.
     * @param secondStack The second stack to check.
     * @return Whether or not the stacks are similar.
     */
    public static boolean areStacksSimilarWithPartialNBT (ItemStack firstStack, ItemStack secondStack) {

        return areStacksSimilar(firstStack, secondStack) && NBTUtils.containsAllTags(getTagCleanly(firstStack), getTagCleanly(secondStack));
    }

    /**
     * Checks if both stacks have the same metadata, or either has the wildcard meta.
     *
     * @param first The first stack.
     * @param second The second stack.
     * @return Whether or not they have the same meta or wildcard meta.
     */
    public static boolean areStacksSameMeta (@Nonnull ItemStack first, @Nonnull ItemStack second) {

        return first.getMetadata() == second.getMetadata() || first.getMetadata() == OreDictionary.WILDCARD_VALUE || second.getMetadata() == OreDictionary.WILDCARD_VALUE;
    }

    /**
     * Checks if an ItemStack matches a recipe ingredient object.
     *
     * @param stack The item stack to check.
     * @param recipe The recipe ingredient object.
     * @return Whether or not the stack and recipe requirements match.
     */
    public static boolean areStacksEqualForCrafting (ItemStack stack, Object recipe) {

        if (recipe instanceof ItemStack) {

            return areStacksSimilarWithSize(stack, (ItemStack) recipe);
        }

        if (recipe instanceof String) {

            return hasOreName(stack, (String) recipe);
        }
        return false;
    }

    /**
     * Checks is an ItemStack has an ore dictionary name.
     *
     * @param stack The item to check.
     * @param name The ore dict name to look up.
     * @return Whether or not the stack has that ore dictionary name.
     */
    public static boolean hasOreName (ItemStack stack, String name) {

        return OreDictUtils.getOreNames(stack).contains(name);
    }

    public static ItemStack writePotionEffectsToStack (ItemStack stack, PotionEffect[] effects) {

        final NBTTagCompound stackTag = prepareStackTag(stack);
        final NBTTagList potionTag = new NBTTagList();

        for (final PotionEffect effect : effects) {
            potionTag.appendTag(effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }

        stackTag.setTag("CustomPotionEffects", potionTag);
        return stack;
    }

    /**
     * Writes an ItemStack as a sub NBTTagCompound on a larger NBTTagCompound.
     *
     * @param stack The ItemStack to write to the tag.
     * @param tag The NBTTagCompound to write the stack to.
     * @param tagName The name for this new NBTTagCompound entry.
     */
    public static void writeStackToTag (ItemStack stack, NBTTagCompound tag, String tagName) {

        final NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        tag.setTag(tagName, stackTag);
    }

    /**
     * Safely decreases the amount of items held by an ItemStack.
     *
     * @param stack The ItemStack to decrease the size of.
     * @param amount The amount to decrease the stack size by.
     * @return ItemStackNull, if the stack size is smaller than 1.
     */
    public static ItemStack decreaseStackSize (ItemStack stack, int amount) {

        stack.shrink(amount);
        return stack.getCount() <= 0 ? ItemStack.EMPTY : stack;
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

        if (firstStack == null || secondStack == null) {
            return firstStack == secondStack;
        }

        final Item firstItem = firstStack.getItem();
        final Item secondItem = secondStack.getItem();

        if (firstItem == null || secondItem == null) {
            return firstItem == secondItem;
        }

        if (firstItem == secondItem) {

            if (checkNBT && NBTUtils.NBT_COMPARATOR.compare(firstStack.getTagCompound(), secondStack.getTagCompound()) != 0) {
                return false;
            }

            return firstStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || secondStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || firstStack.getItemDamage() == secondStack.getItemDamage();
        }

        return false;
    }

    /**
     * A check to see if an ItemStack exists within an array of other ItemStack.
     *
     * @param stack The ItemStack you are searching for.
     * @param checkNBT Should the stacks need the same NBT for them to be the same?
     * @param stacks The array of ItemStack to search through.
     * @return booleanWhether or not the array contains the stack you are looking for.
     */
    public static boolean isStackInArray (ItemStack stack, boolean checkNBT, ItemStack... stacks) {

        for (final ItemStack currentStack : stacks) {
            if (areStacksEqual(stack, currentStack, checkNBT)) {
                return true;
            }
        }

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
        output.setCount(size);
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

        if (thing != null) {
            return thing;
        }

        thing = Block.getBlockFromName(name);

        if (thing != null) {
            return thing;
        }

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

        return !stack.isEmpty() ? Block.getBlockFromItem(stack.getItem()) : Blocks.AIR;
    }

    /**
     * Safely consumes an item from an ItemStack. Respects container items.
     *
     * @param stack The stack to use.
     * @return The remaining/generated item.
     */
    public static ItemStack consumeStack (ItemStack stack) {

        if (stack.getCount() == 1) {

            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            }
            else {
                return ItemStack.EMPTY;
            }
        }

        else {

            stack.splitStack(1);
            return stack;
        }
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
            final EntityItem entityitem = new EntityItem(world, pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ, stack);
            entityitem.setPickupDelay(10);
            world.spawnEntity(entityitem);
        }
    }

    /**
     * Creates an ItemStack which represents a TileEntity, and has all of the TileEntities
     * properties stored.
     *
     * @param tile The TileEntity to turn into an ItemStack.
     * @return The resulting ItemStack.
     */
    public static ItemStack createStackFromTileEntity (TileEntity tile) {

        final ItemStack stack = new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata());
        prepareStackTag(stack);
        final NBTTagCompound tileTag = tile.writeToNBT(new NBTTagCompound());
        stack.getTagCompound().setTag("TileData", tileTag);
        return stack;
    }

    /**
     * Reads tile entity data from an ItemStack. Meant to be an inverse of
     * {@link StackUtils#createStackFromTileEntity(TileEntity)}.
     *
     * @param tile The tile to apply the changes to.
     * @param stack The stack to read from.
     */
    public static void readTileEntityFromStack (TileEntity tile, ItemStack stack) {

        tile.readFromNBT(stack.getTagCompound().getCompoundTag("TileData"));
    }

    /**
     * Creates an ItemStack representation of an IBlockState.
     *
     * @param state The state to use.
     * @param size The stack size to create.
     * @return An ItemStack which represents the passed state.
     */
    @Deprecated
    public static ItemStack getStackFromState (IBlockState state, int size) {

        return new ItemStack(state.getBlock(), size, state.getBlock().damageDropped(state));
    }

    /**
     * Gets an IBlockState from an ItemStack.
     *
     * @param stack The stack to get stuff from.
     * @return The IBlockState from the ItemStack.
     */
    @Deprecated
    public static IBlockState getStateFromStack (ItemStack stack) {

        final Block block = Block.getBlockFromItem(stack.getItem());
        return block != null ? block.getStateFromMeta(stack.getMetadata()) : null;
    }

    /**
     * Finds all the variations of an item, but checking it's
     * {@link Item#getSubItems(CreativeTabs, NonNullList)} results.
     *
     * @param item The item to check for variations of.
     * @return A list of all the variations that were found.
     */
    public static NonNullList<ItemStack> findVariations (Item item) {

        final NonNullList<ItemStack> items = NonNullList.create();

        for (final CreativeTabs tab : item.getCreativeTabs()) {

            if (tab == null) {

                // Enchanted books are a special case in vanilla
                if (item == Items.ENCHANTED_BOOK) {

                    item.getSubItems(CreativeTabs.SEARCH, items);
                }

                items.add(new ItemStack(item));
            }

            else {

                // Create a second list to prevent the item from modifying the harvested list.
                final NonNullList<ItemStack> subItems = NonNullList.create();

                try {

                    item.getSubItems(tab, subItems);
                }

                catch (final Exception e) {

                    Bookshelf.LOG.error("Caught the following exception while getting sub items for {}. It should be reported to that mod's author.", item.getRegistryName().toString());
                    Bookshelf.LOG.catching(e);
                }

                items.addAll(subItems);
            }
        }

        return items;
    }

    /**
     * Gets an array of all variations of an item. This method will grab items from the
     * creative tab entries of an item, and can possibly be slow.
     *
     * @param item The item to get variations of.
     * @return An array of all the variations of the item.
     */
    public static ItemStack[] getAllItems (Item item) {

        return findVariations(item).toArray(new ItemStack[0]);
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
     * Gets an NBTTagCompound from a stack without polluting the original input stack. If the
     * stack does not have a tag, you will get a new one. This new tag will NOT be set to the
     * stack automatically.
     *
     * @param stack The stack to check.
     * @return The nbt data for the stack.
     */
    public static NBTTagCompound getTagCleanly (ItemStack stack) {

        return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    }

    /**
     * Gets all of the block stats for an oredict name. If entries have the wildcard meta, like
     * wood logs, their sub blocks will all be added.
     *
     * @param oredict The ore dictionary name to look up files for.
     * @return A NonNullList containing all of the found itemstacks.
     */
    public static NonNullList<ItemStack> getAllBlocksForOredict (String oredict) {

        final NonNullList<ItemStack> subBlocks = NonNullList.create();

        for (final ItemStack stack : OreDictionary.getOres(oredict)) {

            final Block block = Block.getBlockFromItem(stack.getItem());
            final int meta = stack.getItemDamage();

            // If block is wildcard add sub blocks
            if (meta == OreDictionary.WILDCARD_VALUE) {

                block.getSubBlocks(null, subBlocks);
            }

            // Else just add the block.
            else {

                subBlocks.add(stack);
            }
        }

        return subBlocks;
    }
}