package net.darkhax.bookshelf.lib.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;

import net.darkhax.bookshelf.lib.Constants;

public final class MathsUtils {
    
    /**
     * A list of all enchantments that have been found by the getNextEnchantmentID method. This
     * is meant to keep track of all enchantment IDs that have already been found, and prevent
     * duplicate results. This should only be accessed internally.
     */
    static ArrayList<Integer> foundEnchantments = new ArrayList();
    
    /**
     * A list of all biome IDs that have been found by the getAvailableBiomeID method. This is
     * meant to keep track of biome IDs which have already been found, and prevents duplicate
     * results. This array should only be accessed internally.
     */
    static ArrayList<Integer> foundBiomes = new ArrayList();
    
    /**
     * A list of all potion IDs that have been found by the getNextPotionID method. This is
     * meant to keep track of potion IDs which have already been found, and prevents duplicate
     * results. This array should only be accessed internally.
     */
    static ArrayList<Integer> foundPotions = new ArrayList();
    
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
     * Generates a random color as an integer, from Color and three random floats.
     * 
     * @return int: An integer based representation of a Color.
     */
    public static int getRandomColor () {
        
        return new Color(Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat(), Constants.RANDOM.nextFloat()).getRGB();
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
            
            if (Enchantment.enchantmentsList[possibleID] == null && !foundEnchantments.contains(possibleID)) {
                
                foundEnchantments.add(possibleID);
                return possibleID;
            }
        }
        
        throw new RuntimeException("An attempt to find an available enchantment ID was made, however no IDs are available.");
    }
    
    /**
     * Attempts to find a Potion ID which is vacant. There is no guarantee that other mods
     * loaded after yours will not have the same ID, however it will prevent a great deal of
     * issues.
     * 
     * @return int: A Potion ID which was not assigned at the time of the method being called.
     */
    public static int getNextPotionID () {
        
        for (int possibleID = 0; possibleID < Potion.potionTypes.length; possibleID++) {
            
            if (Potion.potionTypes[possibleID] == null && !foundPotions.contains(possibleID) && possibleID > 32) {
                
                foundPotions.add(possibleID);
                return possibleID;
            }
        }
        
        throw new RuntimeException("An attempt to find an available potion ID was made, however no IDs are available.");
    }
}