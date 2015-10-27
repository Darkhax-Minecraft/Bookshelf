package net.darkhax.bookshelf.util;

import java.awt.Color;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

public class Utilities {
    
    /**
     * Lists of names for the vanilla villagers.
     */
    private static String[] vanillaVillagers = { "farmer", "librarian", "priest", "blacksmith", "butcher" };
    
    /**
     * A list of all biome IDs that have been found by the getAvailableBiomeID method. This is
     * meant to keep track of biome IDs which have already been found, and prevents duplicate
     * results. This array should only be accessed internally.
     */
    private static ArrayList<Integer> foundBiomes = new ArrayList();
    
    /**
     * A list of all enchantments that have been found by the getNextEnchantmentID method. This
     * is meant to keep track of all enchantment IDs that have already been found, and prevent
     * duplicate results. This should only be accessed internally.
     */
    private static ArrayList<Integer> foundEnchantments = new ArrayList();
    
    /**
     * A reference to the curBlockDamageMP method from the PlayerControllerMP class. Used by
     * the getBlockDamage method to get the current client-side block damage amount.
     */
    @SideOnly(Side.CLIENT)
    public static Field currentBlockDamage;
    
    /**
     * This method can be used to round a double to a certain amount of places.
     * 
     * @param value: The double being round.
     * @param places: The amount of places to round the double to.
     * @return double: The double entered however being rounded to the amount of places
     *         specified.
     */
    public static double round (double value, int places) {
        
        return (value >= 0 && places > 0) ? new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue() : value;
    }
    
    /**
     * This method will take a string and break it down into multiple lines based on a provided
     * line length. The separate strings are then added to the list provided. This method is
     * useful for adding a long description to an item tool tip and having it wrap. This method
     * is similar to wrap in Apache WordUtils however it uses a List making it easier to use
     * when working with Minecraft.
     * 
     * @param string: The string being split into multiple lines. It's recommended to use
     *            StatCollector.translateToLocal() for this so multiple languages will be
     *            supported.
     * @param lnLength: The ideal size for each line of text.
     * @param wrapLongWords: If true the ideal size will be exact, potentially splitting words
     *            on the end of each line.
     * @param list: A list to add each line of text to. An good example of such list would be
     *            the list of tooltips on an item.
     * @return List: The same List instance provided however the string provided will be
     *         wrapped to the ideal line length and then added.
     */
    public static List<String> wrapStringToList (String string, int lnLength, boolean wrapLongWords, List<String> list) {
        
        String strings[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);
        list.addAll(Arrays.asList(strings));
        return list;
    }
    
    /**
     * Creates a MovingObjectPosition based on where a player is looking.
     * 
     * @param player: The player to get the looking position of.
     * @param length: The distance to go outwards from the player, the maximum "reach". Default
     *            reach is 4.5D.
     * @return MovingObjectPosition: A MovingObjectPosition containing the exact location where
     *         the player is looking.
     */
    public static MovingObjectPosition rayTrace (EntityPlayer player, double length) {
        
        Vec3 vec1 = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 vec2 = player.getLookVec();
        Vec3 vec3 = vec1.addVector(vec2.xCoord * length, vec2.yCoord * length, vec2.zCoord * length);
        return player.worldObj.rayTraceBlocks(vec1, vec3);
    }
    
    /**
     * Used to retrieve a random integer between the two provided integers. The integers
     * provided are also possible outcomes.
     * 
     * @param min: The minimum value which can be returned by this method.
     * @param max: The maximum value which can be returned by this method.
     */
    public static int nextIntInclusive (int min, int max) {
        
        return Constants.RANDOM.nextInt(max - min + 1) + min;
    }
    
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
     * Sets an unknown data type to an NBTTagCompound. If the type of the data can not be
     * identified, and exception will be thrown. Current supported data types include String,
     * Integer, Float, Boolean, Double, Long, Short, Byte, ItemStack, Entity and Position.
     * 
     * @param dataTag: An NBTTagCompound to write this unknown data to.
     * @param tagName: The name to save this unknown data under.
     * @param value: The unknown data you wish to write to the dataTag.
     */
    public static void setGenericNBTValue (NBTTagCompound dataTag, String tagName, Object value) {
        
        if (value instanceof String)
            dataTag.setString(tagName, (String) value);
            
        else if (value instanceof Integer)
            dataTag.setInteger(tagName, (Integer) value);
            
        else if (value instanceof Float)
            dataTag.setFloat(tagName, (Float) value);
            
        else if (value instanceof Boolean)
            dataTag.setBoolean(tagName, (Boolean) value);
            
        else if (value instanceof Double)
            dataTag.setDouble(tagName, (Double) value);
            
        else if (value instanceof Long)
            dataTag.setLong(tagName, (Long) value);
            
        else if (value instanceof Short)
            dataTag.setShort(tagName, (Short) value);
            
        else if (value instanceof Byte)
            dataTag.setByte(tagName, (Byte) value);
            
        else if (value instanceof ItemStack)
            dataTag.setTag(tagName, ((ItemStack) value).writeToNBT(new NBTTagCompound()));
            
        else if (value instanceof Position)
            dataTag.setTag(tagName, ((Position) value).write(new NBTTagCompound()));
            
        else if (value instanceof Entity) {
            
            NBTTagCompound newTag = new NBTTagCompound();
            ((Entity) value).writeToNBT(newTag);
            dataTag.setTag(tagName, newTag);
        }
        
        else
            throw new UnsupportedTypeException(value);
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
        
        for (Object playerEntry : world.playerEntities) {
            
            if (playerEntry instanceof EntityPlayer) {
                
                EntityPlayer player = (EntityPlayer) playerEntry;
                
                if (player.getUniqueID().equals(playerID))
                    return player;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if a block is a fluid or not.
     * 
     * @param block: An instance of the block being checked.
     * @return boolean: If the block is a fluid, true will be returned. If not, false will be
     *         returned.
     */
    public static boolean isFluid (Block block) {
        
        return (block == Blocks.lava || block == Blocks.water || block instanceof IFluidBlock);
    }
    
    /**
     * A simple check to make sure that an EntityPlayer actually exists.
     * 
     * @param player: The instance of EntityPlayer to check.
     * @return boolean: If the player exists true will be returned. If they don't false will be
     *         returned.
     */
    public static boolean isPlayerReal (EntityPlayer player) {
        
        if (player == null || player.worldObj == null || player.getClass() != EntityPlayerMP.class)
            return false;
            
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
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
     * Writes an inventory to an NBTTagCompound. Can be used to save an inventory in a
     * TileEntity, or perhaps an ItemStack.
     * 
     * @param tag: The NBTTagCompound to write the inventory to.
     * @param inventory: The inventory to write to the NBTTagCompound.
     * @return NBTTagCompound: The same NBTTagCompound that was passed to this method.
     */
    public static NBTTagCompound writeInventoryToNBT (NBTTagCompound tag, InventoryBasic inventory) {
        
        if (inventory.hasCustomInventoryName())
            tag.setString("CustomName", inventory.getInventoryName());
            
        NBTTagList nbttaglist = new NBTTagList();
        
        for (int slotCount = 0; slotCount < inventory.getSizeInventory(); slotCount++) {
            
            ItemStack stackInSlot = inventory.getStackInSlot(slotCount);
            
            if (stackInSlot != null) {
                
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slotCount);
                stackInSlot.writeToNBT(itemTag);
                nbttaglist.appendTag(itemTag);
            }
        }
        
        tag.setTag("Items", nbttaglist);
        
        return tag;
    }
    
    /**
     * Reads an inventory from an NBTTagCompound. Can be used to load an Inventory from a
     * TileEntity or perhaps an ItemStak.
     * 
     * @param tag: The NBTTagCompound to read the inventory data from.
     * @param inventory: The inventory to set all of the inventory data to.
     * @return InventoryBasic: The same instance of InventoryBasic that was passed to this
     *         method.
     */
    public static InventoryBasic readInventoryFromNBT (NBTTagCompound tag, InventoryBasic inventory) {
        
        if (tag.hasKey("CustomName", 8))
            inventory.func_110133_a(tag.getString("CustomName"));
            
        NBTTagList items = tag.getTagList("Items", 10);
        
        for (int storedCount = 0; storedCount < items.tagCount(); storedCount++) {
            
            NBTTagCompound itemTag = items.getCompoundTagAt(storedCount);
            int slotCount = itemTag.getByte("Slot") & 0xFF;
            
            if ((slotCount >= 0) && (slotCount < inventory.getSizeInventory()))
                inventory.setInventorySlotContents(slotCount, ItemStack.loadItemStackFromNBT(itemTag));
        }
        
        return inventory;
    }
    
    /**
     * A method which handles the calculating of percentages. While this isn't a particularly
     * difficult piece of code, it has been added for the sake of simplicity.
     * 
     * @param percent: The percent chance that this method should return true. 1.00 = 100%
     * @return boolean: Returns are randomly true or false, based on the suplied percentage.
     */
    public static boolean tryPercentage (double percent) {
        
        return Math.random() < percent;
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
        Object contents = getThingByName(parts[0]);
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
        
        if (isValidStack(stack))
            for (VanillaColor color : VanillaColor.values())
                for (ItemStack oreStack : OreDictionary.getOres(color.getDyeName()))
                    if (oreStack.isItemEqual(stack))
                        return color.color.getRGB();
                        
        return -1337;
    }
    
    /**
     * A blend between the itemRegistry.getObject and bockRegistry.getObject methods. Used for
     * grabbing something from an ID, when you have no clue what it might be.
     * 
     * @param name: The ID of the thing you're looking for. Domains are often preferred.
     * @return Object: Hopefully the thing you're looking for.
     */
    public static Object getThingByName (String name) {
        
        Object thing = Item.itemRegistry.getObject(name);
        
        if (thing != null)
            return thing;
            
        thing = Block.blockRegistry.getObject(name);
        
        if (thing != null)
            return thing;
            
        return null;
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
     * Retrieves an array of ItemStack from an NBTTagCompound. This method is intended to be
     * used with the NBT version of an IInventory and can be used when parsing things like
     * TileEntity NBT data.
     * 
     * @param tag: The tag to retrieve all of the item data from.
     * @param invSize: The projected size of the inventory stored to the tag. It is critical
     *            that this never be smaller then the actual amount.
     * @return ItemStack[]: An array of ItemStack stored on the NBTTagCompound.
     */
    public static ItemStack[] getStoredItems (NBTTagCompound tag, int invSize) {
        
        ItemStack[] inventory = null;
        
        if (tag.hasKey("Items")) {
            
            NBTTagList list = tag.getTagList("Items", 10);
            inventory = new ItemStack[invSize];
            
            for (int i = 0; i < list.tagCount(); i++) {
                
                if (!(i > list.tagCount())) {
                    
                    NBTTagCompound currentTag = list.getCompoundTagAt(i);
                    inventory[(int) currentTag.getByte("Slot")] = ItemStack.loadItemStackFromNBT(currentTag);
                }
            }
        }
        
        return inventory;
    }
    
    /**
     * Attempts to harvest blocks in an AOE based effect around where the player is looking.
     * This effect is designed to be used in conjunction with a tool, and should be used as
     * such.
     * 
     * @param player: The player who is attempting to harvest the block.
     * @param materials: An array of valid material types that this effect will be able to
     *            break.
     * @param layers: The amount of layers to break. Each layer adds one more out in every
     *            direction. For example 1 will break a 3x3 area, 2 will break a 5x5 area and 3
     *            will break a 9x9 area.
     */
    public static void tryAOEHarvest (EntityPlayer player, Material[] materials, int layers) {
        
        ItemStack stack = player.getHeldItem();
        
        if (stack != null) {
            
            prepareDataTag(stack);
            MovingObjectPosition lookPos = rayTrace(player, 4.5d);
            
            if (lookPos != null && player instanceof EntityPlayerMP) {
                
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                NBTTagCompound dataTag = stack.stackTagCompound;
                
                int x = lookPos.blockX;
                int y = lookPos.blockY;
                int z = lookPos.blockZ;
                
                if (!dataTag.hasKey("bookshelfBreaking") || !dataTag.getBoolean("bookshelfBreaking")) {
                    
                    dataTag.setBoolean("bookshelfBreaking", true);
                    int rangeX = layers;
                    int rangeY = layers;
                    int rangeZ = layers;
                    
                    switch (lookPos.sideHit) {
                        
                        case 1:
                            rangeY = 0;
                            break;
                            
                        case 3:
                            rangeZ = 0;
                            break;
                            
                        case 5:
                            rangeX = 0;
                            break;
                    }
                    
                    for (int posX = x - rangeX; posX <= x + rangeX; posX++) {
                        
                        for (int posY = y - rangeY; posY <= y + rangeY; posY++) {
                            
                            for (int posZ = z - rangeZ; posZ <= z + rangeZ; posZ++) {
                                
                                Block block = playerMP.worldObj.getBlock(posX, posY, posZ);
                                
                                for (Material mat : materials)
                                    if (block != null && mat == block.getMaterial() && block.getPlayerRelativeBlockHardness(playerMP, playerMP.worldObj, x, posY, z) > 0)
                                        playerMP.theItemInWorldManager.tryHarvestBlock(posX, posY, posZ);
                            }
                        }
                    }
                    
                    dataTag.setBoolean("bookshelfBreaking", false);
                }
            }
        }
    }
    
    /**
     * A basic check to see if two classes are the same. For the classes to be the same,
     * neither can be null, and they must share the same name.
     * 
     * @param class1: The first class to compare.
     * @param class2: The second class to compare.
     * @return boolean: True if neither class is null, and both share the same name.
     */
    public static boolean compareClasses (Class class1, Class class2) {
        
        return (class1 != null && class2 != null && class1.getName().equalsIgnoreCase(class2.getName()));
    }
    
    /**
     * Compares the class of an Object with another class. Useful for comparing a TileEntity or
     * Item.
     * 
     * @param obj: The Object to compare.
     * @param clazz: The class to compare the Object to.
     * @return boolean: True if the Object is of the same class as the one provided.
     */
    public static boolean compareObjectToClass (Object obj, Class clazz) {
        
        return compareClasses(obj.getClass(), clazz);
    }
    
    /**
     * Makes the first character of a string upper case. Useful for taking raw text data and
     * turning it into part of a sentence or other display data.
     * 
     * @param text: The text to convert.
     * @return String: The same string that was passed, however the first character has been
     *         made upper case.
     */
    public static String makeUpperCased (String text) {
        
        return Character.toString(text.charAt(0)).toUpperCase() + text.substring(1);
    }
    
    /**
     * Provides a safe way to get a class by its name. This is essentially the same as
     * Class.forName however it will handle any ClassNotFoundException automatically.
     * 
     * @param name: The name of the class you are trying to get. Example: java.lang.String
     * @return Class: If a class could be found, it will be returned. Otherwise, null.
     */
    public static Class getClassFromString (String name) {
        
        try {
            
            return Class.forName(name);
        }
        
        catch (ClassNotFoundException e) {
            
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Checks if a specific player can sleep. For this to be true, a player must not already be
     * in a bed, and the world time bust be greater than 12541, but less than 23458.
     * 
     * @param player: The player to check the sleepability of.
     * @return boolean: True if the player can sleep, false if they can not.
     */
    public static boolean canPlayerSleep (EntityPlayer player) {
        
        return (!player.isPlayerSleeping() && player.isEntityAlive() && player.worldObj.getWorldTime() > 12541 && player.worldObj.getWorldTime() < 23458);
    }
    
    /**
     * Provides a way to access an NBTTagCompound that is very deep within another
     * NBTTagCompound. This will allow you to use an array of strings which represent the
     * different steps to get to the deep NBTTagCompound.
     * 
     * @param tag: An NBTTagCompound to search through.
     * @param tags: An array containing the various steps to get to the desired deep
     *            NBTTagCompound.
     * @return NBTTagCompound: This method will return the deepest possible NBTTagCompound. In
     *         some cases, this may be the tag you provide, or only a few steps deep, rather
     *         than all of the way.
     */
    public static NBTTagCompound getDeepTagCompound (NBTTagCompound tag, String[] tags) {
        
        NBTTagCompound deepTag = tag;
        
        if (tag != null)
            for (String tagName : tags)
                if (deepTag.hasKey(tagName))
                    deepTag = deepTag.getCompoundTag(tagName);
                    
        return deepTag;
    }
    
    /**
     * Generates a random color as an integer, from Color and three random floats.
     * 
     * @return int: An integer based representation of a Color.
     */
    public static int getRandomColor () {
        
        return new Color(Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat()).getRGB();
    }
    
    /**
     * A check to see if an entity is wearing a full suit of the armor. This check is based on
     * the class names of armor.
     * 
     * @param living: The living entity to check the armor of.
     * @param armorClass: The class of the armor to check against.
     * @return boolean: True if every piece of armor the entity is wearing are the same class
     *         as the provied armor class.
     */
    public static boolean isWearingFullSet (EntityLivingBase living, Class armorClass) {
        
        for (int armorSlot = 1; armorSlot <= 4; armorSlot++) {
            
            ItemStack armor = living.getEquipmentInSlot(armorSlot);
            
            if (armor == null || !armor.getItem().getClass().equals(armorClass))
                return false;
        }
        
        return true;
    }
    
    /**
     * Attempts to find a biome ID which is vacant. There is no guarantee that other mods
     * loaded after yours will not use the same ID, however it will prevent a great deal of
     * issues.
     * 
     * @return int: A biome ID which was not occupied at the time of the method being called.
     */
    public static int getAvailableBiomeID () {
        
        for (int possibleID = 0; possibleID < BiomeGenBase.getBiomeGenArray().length; possibleID++)
            
            if (BiomeGenBase.getBiome(possibleID) == null && !foundBiomes.contains(possibleID)) {
                
                foundBiomes.add(possibleID);
                return possibleID;
            }
            
        throw new RuntimeException("An attempt to find an available biome ID was made, however no IDs are available.");
    }
    
    /**
     * Attempts to find an Enchantment ID which is vacant. There is no guarantee that other
     * mods loaded after yours will not have the same ID, however it will prevent a great deal
     * of issues.
     * 
     * @return int: An Enchantment ID which was not assigned at the time of the method being
     *         called.
     */
    public static int getNextEnchantmentID () {
        
        for (int possibleID = 0; possibleID < Enchantment.enchantmentsList.length; possibleID++) {
            
            if (Enchantment.enchantmentsList[possibleID] == null && !foundBiomes.contains(possibleID)) {
                
                foundEnchantments.add(possibleID);
                return possibleID;
            }
        }
        
        throw new RuntimeException("Anattempt to find an available enchantment ID was made, however no IDs are available.");
    }
    
    /**
     * Retrieves the ItemStack placed in an EntityHorse's custom armor inventory slot.
     * 
     * @param horse: An instance of the EntityHorse to grab the armor ItemStack from.
     * @return ItemStack: The ItemStack in the horses custom armor slot. This ItemStack maybe
     *         null, and won't always be an instance of ItemHorseArmor.
     */
    public static ItemStack getCustomHorseArmor (EntityHorse horse) {
        
        return horse.getDataWatcher().getWatchableObjectItemStack(23);
    }
    
    /**
     * Allows for a custom ItemStack to be set to an EntityHorse's custom armor inventory slot.
     * 
     * @param horse: An instance of the EntityHorse to set the ItemStack to.
     * @param stack: An ItemStack you want to set to an EntityHorse's custom armor inventory
     *            slot.
     */
    public static void setCustomHorseArmor (EntityHorse horse, ItemStack stack) {
        
        horse.getDataWatcher().updateObject(23, stack);
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
     * Retrieves a unique string related to the texture name of a villager. This allows for
     * villagers to be differentiated based on their profession rather than their ID.
     * 
     * @param id : The ID of the villager being looked up.
     * @return String: The texture name, minus file path and extension.
     */
    @SideOnly(Side.CLIENT)
    public static String getVillagerName (int id) {
        
        ResourceLocation skin = VillagerRegistry.getVillagerSkin(id, null);
        return (id >= 0 && id <= 4) ? vanillaVillagers[id] : (skin != null) ? skin.getResourceDomain() + "." + skin.getResourcePath().substring(skin.getResourcePath().lastIndexOf("/") + 1, skin.getResourcePath().length() - 4) : "misingno";
    }
    
    /**
     * A client sided method used to retrieve the progression of the block currently being
     * mined by the player. This method is client side only, and refers to only the one
     * instance of the player. Do not try to use this method to get data for multiple players,
     * or for server sided things.
     * 
     * @return float: A float value representing how much time is left for the block being
     *         broken to break. 0 = no damage has been done. 1 = the block is broken.
     */
    @SideOnly(Side.CLIENT)
    public static float getBlockDamage () {
        
        if (currentBlockDamage == null)
            return 0;
            
        try {
            
            return currentBlockDamage.getFloat(Minecraft.getMinecraft().playerController);
        }
        
        catch (IllegalArgumentException e) {
        
        }
        
        catch (IllegalAccessException e) {
        
        }
        
        return 0;
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
     * A wrapper for glColor3f. This wrapper takes an integer and splits it up into it's RGB
     * components. This method primarily exists to reduce the complexity required to use the
     * glColor3f method through ASM.
     * 
     * @param colorVal: A single integer which represents the RGB compoinents of a color.
     */
    @SideOnly(Side.CLIENT)
    public static void renderColor (int colorVal) {
        
        Color color = new Color(colorVal);
        GL11.glColor3f(((float) color.getRed() / 255f), ((float) color.getGreen() / 255f), ((float) color.getBlue() / 255f));
    }
    
    /**
     * Synchronizes a ModelBiped to an EntityLivingBase. This method is intended for use in
     * rendering armor models which use ModelBiped as a base.
     * 
     * @param armorModel: The armor model to synchronize to the entity.
     * @param armorSlot: The type of armor which is being used.
     * @param entity: The entity to synchronize the armor model to.
     */
    @SideOnly(Side.CLIENT)
    public static void synArmorModelToEntity (ModelBiped armorModel, int armorSlot, EntityLivingBase entity) {
        
        if (armorModel == null || entity == null)
            return;
            
        armorModel.bipedHead.showModel = armorSlot == 0;
        armorModel.bipedHeadwear.showModel = armorSlot == 0;
        armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        armorModel.bipedRightArm.showModel = armorSlot == 1;
        armorModel.bipedLeftArm.showModel = armorSlot == 1;
        armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
        armorModel.isSneak = entity.isSneaking();
        armorModel.isRiding = entity.isRiding();
        armorModel.isChild = entity.isChild();
        armorModel.heldItemRight = entity.getEquipmentInSlot(0) != null ? 1 : 0;
        
        if (entity instanceof EntityPlayer)
            armorModel.aimedBow = ((EntityPlayer) entity).getItemInUseDuration() > 2;
    }
    
    public static class UnsupportedTypeException extends RuntimeException {
        
        /**
         * An exception that is thrown when an unsupported Object type is being worked with.
         * Used when working with generic Object Types. This exception will print the class,
         * and the toString() for unknown data type.
         * 
         * @param data: The unsupported type.
         */
        public UnsupportedTypeException(Object data) {
            
            super("The data type of " + data.getClass().toString() + " is currently not supported." + Constants.NEW_LINE + "Raw Data: " + data.toString());
        }
    }
}