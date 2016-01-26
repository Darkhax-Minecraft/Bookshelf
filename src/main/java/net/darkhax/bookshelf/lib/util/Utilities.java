package net.darkhax.bookshelf.lib.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class Utilities {
    
    /**
     * Array of names for the vanilla villagers.
     */
    private static String[] vanillaVillagers = { "farmer", "librarian", "priest", "blacksmith", "butcher" };
    
    /**
     * An array of all keys used in ChestGenHooks for the vanilla chests.
     */
    public static String[] vanillaLootChests = new String[] { "dungeonChest", "bonusChest", "villageBlacksmith", "strongholdCrossing", "strongholdLibrary", "strongholdCorridor", "pyramidJungleDispenser", "pyramidJungleChest", "pyramidDesertyChest", "mineshaftCorridor" };
    
    /**
     * An array of formatting codes used to create rainbow text.
     */
    public static String[] rainbowChars = new String[] { "4", "6", "e", "a", "9", "5" };
    
    /**
     * An array of all the LWJGL numeric key codes.
     */
    public static int[] validKeys = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 71, 72, 73, 75, 76, 77, 79, 80, 81 };
    
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
        
        String lines[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);
        list.addAll(Arrays.asList(lines));
        return list;
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
     * @param format: A list to add each line of text to. An good example of such list would be
     *            the list of tooltips on an item.
     * @param color: An EnumChatFormatting to apply to all lines added to the list.
     * @return List: The same List instance provided however the string provided will be
     *         wrapped to the ideal line length and then added.
     */
    public static List<String> wrapStringToListWithFormat (String string, int lnLength, boolean wrapLongWords, List<String> list, EnumChatFormatting format) {
        
        String lines[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);
        
        for (String line : lines)
            list.add(format + line);
            
        return list;
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
     * A blend between the itemRegistry.getObject and bockRegistry.getObject methods. Used for
     * grabbing something from an ID, when you have no clue what it might be.
     *
     * @param name: The ID of the thing you're looking for. Domains are often preferred.
     * @return Object: Hopefully the thing you're looking for.
     */
    public static Object getThingByName (String name) {
        
        ResourceLocation location = new ResourceLocation(name);
        
        Object thing = Item.itemRegistry.getObject(location);
        
        if (thing != null)
            return thing;
            
        thing = Block.blockRegistry.getObject(location);
        
        if (thing != null)
            return thing;
            
        return null;
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
     * Retrieves the name of the mod that added the item to the game.
     *
     * @param item: The Item to get the mod name from.
     * @return String: The name of the mod which added the provided item to the game.
     */
    public static String getModName (Item item) {
        
        String itemID = GameData.getItemRegistry().getNameForObject(item).toString();
        return itemID.substring(0, itemID.indexOf(':'));
    }
    
    /**
     * Retrieves the name of the mod that added the block to the game.
     *
     * @param block: The Block to get the mod name from.
     * @return String: The name of the mod which added the provided block.
     */
    public static String getModName (Block block) {
        
        String blockID = GameData.getBlockRegistry().getNameForObject(block).toString();
        return blockID.substring(0, blockID.indexOf(':'));
    }
    
    /**
     * Spawns a particle into the world, in a basic ring pattern. The center of the ring is
     * focused around the provided XYZ coordinates.
     *
     * @param world: The world to spawn the particles in.
     * @param particle: The type of particle to spawn.
     * @param x: The x position to spawn the particle around.
     * @param y: The y position to spawn the particle around.
     * @param z: The z position to spawn the particle around.
     * @param velocityX: The velocity of the particle, in the x direction.
     * @param velocityY: The velocity of the particle, in the y direction.
     * @param velocityZ: The velocity of the particle, in the z direction.
     * @param step: The distance in degrees, between each particle. The maximum is 2 * PI,
     *            which will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnParticleRing (World world, EnumParticleTypes particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < 2 * Math.PI; degree += step)
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
    }
    
    /**
     * Spawns a particle into the world, in a basic ring pattern. The center of the ring is
     * focused around the provided XYZ coordinates.
     *
     * @param world: The world to spawn the particles in.
     * @param particle: The type of particle to spawn.
     * @param percent: The percentage of the ring to render.
     * @param x: The x position to spawn the particle around.
     * @param y: The y position to spawn the particle around.
     * @param z: The z position to spawn the particle around.
     * @param velocityX: The velocity of the particle, in the x direction.
     * @param velocityY: The velocity of the particle, in the y direction.
     * @param velocityZ: The velocity of the particle, in the z direction.
     * @param step: The distance in degrees, between each particle. The maximum is 2 * PI,
     *            which will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnParticleRing (World world, EnumParticleTypes particle, float percentage, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < (2 * Math.PI * percentage); degree += step)
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
    }
    
    public static String getTicksAstime (int timeInTicks) {
        
        float time = (float) timeInTicks / 20f;
        
        return MathsUtils.round(time, 2) + ((time == 1f) ? " Second " : "Seconds");
    }
    
    /**
     * Checks if a keyCode is numeric, meaning 0-9 on the keyboard or number pad.
     * 
     * @param keyCode: The key code to test.
     * @return boolean: True, if the key is a number key.
     */
    public static boolean isKeyCodeNumeric (int keyCode) {
        
        for (int validKey : validKeys)
            if (validKey == keyCode)
                return true;
                
        return false;
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
     * Searches through the array of CreativeTabs and finds the first tab with the same label
     * as the one passed.
     * 
     * @param label: The label of the tab you are looking for.
     * @return CreativeTabs: A CreativeTabs with the same label as the one passed. If this is
     *         not found, you will get null.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs getTabFromLabel (String label) {
        
        for (CreativeTabs tab : CreativeTabs.creativeTabArray)
            if (tab.getTabLabel().equalsIgnoreCase(label))
                return tab;
                
        return null;
    }
}