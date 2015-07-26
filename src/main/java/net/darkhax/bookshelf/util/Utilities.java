package net.darkhax.bookshelf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Utilities {
    
    /**
     * This method can be used to round a double to a certain amount of places.
     * 
     * @param value: The double being round.
     * @param places: The amount of places to round the double to.
     * @return double: The double entered however being rounded to the amount of places
     *         specified.
     */
    public static double round (double value, int places) {
    
        if (value >= 0 && places > 0) {
            
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        
        return value;
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
    public static NBTTagCompound preparedataTag (ItemStack stack) {
    
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
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     */
    @SideOnly(Side.CLIENT)
    public static EntityPlayer thePlayer () {
    
        return Minecraft.getMinecraft().thePlayer;
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