package net.darkhax.bookshelf.lib.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class MathsUtils {
    
    /**
     * Checks if a double is within range of two other doubles.
     * 
     * @param min: The smallest valid value.
     * @param max: The largest valid value.
     * @param value: The value to check.
     * @return boolean: Whether or not the value is within the provided scope.
     */
    public static boolean isInRange (double min, double max, double value) {
        
        return (value <= max && value >= min);
    }
    
    /**
     * Calculates the distance between two Vec3 positions.
     * 
     * @param firstPos: The first position to work with.
     * @param secondPos: The second position to work with.
     * @return double: The distance between the two provided locations.
     */
    public static double getDistanceBetweenPoints (Vec3 firstPos, Vec3 secondPos) {
        
        final double distanceX = firstPos.xCoord - secondPos.xCoord;
        final double distanceY = firstPos.yCoord - secondPos.yCoord;
        final double distanceZ = firstPos.zCoord - secondPos.zCoord;
        
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
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
        
        Vec3 vec1 = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
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
     * Gets the middle integer between two other integers. The order is not important.
     * 
     * @param first: The first integer.
     * @param second: The second integer.
     * @return int: The integer that is between the two provided integers.
     */
    public static int getAverage (int first, int second) {
        
        return Math.round((first + second) / 2.0F);
    }
    
    /**
     * Converts time in ticks to a human readable string.
     * 
     * @param ticks: The amount of ticks to convert.
     * @return String: A human readable version of the time.
     */
    public static String ticksToTime (int ticks) {
        
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        return minutes + ":" + seconds;
    }
}