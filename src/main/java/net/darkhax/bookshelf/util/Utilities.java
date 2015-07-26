package net.darkhax.bookshelf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Utilities {
    
    /**
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     */
    @SideOnly(Side.CLIENT)
    public static EntityPlayer thePlayer () {
    
        return Minecraft.getMinecraft().thePlayer;
    }
    
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
    
    // TODO add docs
    public static MovingObjectPosition rayTrace (World world, EntityPlayer player, int length) {
    
        Vec3 vec1 = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 vec2 = player.getLookVec();
        Vec3 vec3 = vec1.addVector(vec2.xCoord * length, vec2.yCoord * length, vec2.zCoord * length);
        
        return world.rayTraceBlocks(vec1, vec3);
    }
    
    public static void sendPlayerToPosition (EntityPlayer player, MovingObjectPosition position) {
    
        if ((position != null) && (position.typeOfHit == MovingObjectType.BLOCK)) {
            
            Position pos = new Position(position);
            
            switch (position.sideHit) {
            
                case 0:
                    pos.translateDown(1);
                    break;
                
                case 1:
                    pos.translateUp(1);
                    break;
                
                case 2:
                    pos.translateNorth(1);
                    break;
                
                case 3:
                    pos.translateSouth(1);
                    break;
                
                case 4:
                    pos.translateWest(1);
                    break;
                
                case 5:
                    pos.translateEast(1);
                    break;
                
                default:
                    pos.translateUp(1);
            }
            
            pos.sendEntityToPosition(player);
        }
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
    public static NBTTagCompound prepareStackTag (ItemStack stack) {
    
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        
        return stack.getTagCompound();
    }
    
    /**
     * Adds a value to the stack's tag.
     * 
     * @param stack: Stack being used.
     * @param tagName: Name of the tag.
     * @param value: Value being set, supports most data types that nbt supports.
     */
    public static void prepareStackNBT (ItemStack stack, String tagName, Object value) {
    
        prepareStackTag(stack);
        NBTTagCompound stackTag = stack.stackTagCompound;
        
        if (value instanceof String) {
            
            stackTag.setString(tagName, (String) value);
            return;
        }
        
        if (value instanceof Integer) {
            
            stackTag.setInteger(tagName, (Integer) value);
            return;
        }
        
        if (value instanceof Float) {
            
            stackTag.setFloat(tagName, (Float) value);
            return;
        }
        
        if (value instanceof Boolean) {
            
            stackTag.setBoolean(tagName, (Boolean) value);
            return;
        }
        
        if (value instanceof Double) {
            
            stackTag.setDouble(tagName, (Double) value);
            return;
        }
        
        if (value instanceof Long) {
            
            stackTag.setLong(tagName, (Long) value);
            return;
        }
        
        if (value instanceof Short) {
            
            stackTag.setShort(tagName, (Short) value);
            return;
        }
        
        if (value instanceof Byte) {
            
            stackTag.setByte(tagName, (Byte) value);
            return;
        }
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
}