/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public final class MathsUtils {
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private MathsUtils() {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Checks if a double is within range of two other doubles.
     *
     * @param min: The smallest valid value.
     * @param max: The largest valid value.
     * @param value: The value to check.
     * @return boolean: Whether or not the value is within the provided scope.
     */
    public static boolean isInRange (double min, double max, double value) {
        
        return value <= max && value >= min;
    }
    
    /**
     * Calculates the distance between two Vec3 positions.
     *
     * @param firstPos: The first position to work with.
     * @param secondPos: The second position to work with.
     * @return double: The distance between the two provided locations.
     */
    public static double getDistanceBetweenPoints (Vec3d firstPos, Vec3d secondPos) {
        
        final double distanceX = firstPos.x - secondPos.x;
        final double distanceY = firstPos.y - secondPos.y;
        final double distanceZ = firstPos.z - secondPos.z;
        
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
        
        return value >= 0 && places > 0 ? BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue() : value;
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
     *        reach is 4.5D.
     * @return MovingObjectPosition: A MovingObjectPosition containing the exact location where
     *         the player is looking.
     */
    public static RayTraceResult rayTrace (EntityPlayer player, double length) {
        
        final Vec3d vec1 = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final Vec3d vec2 = player.getLookVec();
        final Vec3d vec3 = vec1.add(vec2.x * length, vec2.y * length, vec2.z * length);
        return player.world.rayTraceBlocks(vec1, vec3);
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
        
        final int seconds = ticks / 20;
        final int minutes = seconds / 60;
        return minutes + ":" + seconds;
    }
    
    /**
     * Gets the percentage of an integer. Result is an integer and decimal is lost.
     *
     * @param value The value to get the percentage of.
     * @param total The total/max value.
     * @return The percentage as an integer.
     */
    public static int getPercentage (int value, int total) {
        
        return (int) ((float) value / (float) total * 100f);
    }
    
    /**
     * Gets the distance in world for an amount of pixels. A basic block is a cubic meter, and
     * each pixel is 1/16th of a block.
     *
     * @param pixels The amount of pixels
     * @return The distance in game for those pixels.
     */
    public static double getPixelDistance (int pixels) {
        
        return pixels / 16d;
    }
    
    /**
     * Creates a bounding box using pixel size.
     *
     * @param minX The min X pos.
     * @param minY The min Y pos.
     * @param minZ The min Z pos.
     * @param maxX The max X pos.
     * @param maxY The max Y pos.
     * @param maxZ The max Z pos.
     * @return A bounding box that is made to a pixel specific size.
     */
    public static AxisAlignedBB getBoundsForPixels (int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        
        return new AxisAlignedBB(getPixelDistance(minX), getPixelDistance(minY), getPixelDistance(minZ), getPixelDistance(maxX), getPixelDistance(maxY), getPixelDistance(maxZ));
    }
    
    /**
     * Takes an integer value and fits it within a range. If the initial value is less than the
     * minimum it will be set to the minimum. If the initial value is greater than the maximum
     * it will be lowered to the maximum.
     *
     * @param initial The initial value.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The adjusted value.
     */
    public static int adjustToRange (int initial, int min, int max) {
        
        return initial < min ? min : initial > max ? max : initial;
    }
}