package net.darkhax.bookshelf.api.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public final class MathsUtils {

    /**
     * A decimal format that only preserves two decimal places.
     */
    public static final DecimalFormat DECIMAL_2 = new DecimalFormat("##.##");

    /**
     * Checks if a double is within range of two other doubles.
     *
     * @param min:   The smallest valid value.
     * @param max:   The largest valid value.
     * @param value: The value to check.
     * @return boolean: Whether or not the value is within the provided scope.
     */
    public static boolean isInRange(double min, double max, double value) {

        return value <= max && value >= min;
    }

    /**
     * Calculates the distance between two Vec3 positions.
     *
     * @param firstPos:  The first position to work with.
     * @param secondPos: The second position to work with.
     * @return double: The distance between the two provided locations.
     */
    public static double getDistanceBetweenPoints(Vec3 firstPos, Vec3 secondPos) {

        final double distanceX = firstPos.x - secondPos.x;
        final double distanceY = firstPos.y - secondPos.y;
        final double distanceZ = firstPos.z - secondPos.z;

        return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
    }

    /**
     * This method can be used to round a double to a certain amount of places.
     *
     * @param value:  The double being round.
     * @param places: The amount of places to round the double to.
     * @return double: The double entered however being rounded to the amount of places specified.
     */
    public static double round(double value, int places) {

        return value >= 0 && places > 0 ? BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue() : value;
    }

    /**
     * Used to retrieve a random integer between the two provided integers. The integers provided are also possible
     * outcomes.
     *
     * @param rand An instance of random.
     * @param min  The minimum value which can be returned by this method.
     * @param max  The maximum value which can be returned by this method.
     * @return An integer between the min and max, including the min and max.
     */
    public static int nextIntInclusive(Random rand, int min, int max) {

        return rand.nextInt(max - min + 1) + min;
    }

    /**
     * A method which handles the calculating of percentages. While this isn't a particularly difficult piece of code,
     * it has been added for the sake of simplicity.
     *
     * @param percent: The percent chance that this method should return true. 1.00 = 100%
     * @return boolean: Returns are randomly true or false, based on the suplied percentage.
     */
    public static boolean tryPercentage(double percent) {

        return Math.random() < percent;
    }

    /**
     * Gets the middle integer between two other integers. The order is not important.
     *
     * @param first:  The first integer.
     * @param second: The second integer.
     * @return int: The integer that is between the two provided integers.
     */
    public static int getAverage(int first, int second) {

        return Math.round((first + second) / 2.0F);
    }

    /**
     * Converts time in ticks to a human readable string.
     *
     * @param ticks: The amount of ticks to convert.
     * @return String: A human readable version of the time.
     */
    public static String ticksToTime(int ticks) {

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
    public static int getPercentage(int value, int total) {

        return (int) ((float) value / (float) total * 100f);
    }

    /**
     * Gets the distance in world for an amount of pixels. A basic block is a cubic meter, and each pixel is 1/16th of a
     * block.
     *
     * @param pixels The amount of pixels
     * @return The distance in game for those pixels.
     */
    public static double getPixelDistance(int pixels) {

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
    public static AABB getBoundsForPixels(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {

        return new AABB(getPixelDistance(minX), getPixelDistance(minY), getPixelDistance(minZ), getPixelDistance(maxX), getPixelDistance(maxY), getPixelDistance(maxZ));
    }

    /**
     * Takes an integer value and fits it within a range. If the initial value is less than the minimum it will be set
     * to the minimum. If the initial value is greater than the maximum it will be lowered to the maximum.
     *
     * @param initial The initial value.
     * @param min     The minimum value.
     * @param max     The maximum value.
     * @return The adjusted value.
     */
    public static int adjustToRange(int initial, int min, int max) {

        return initial < min ? min : initial > max ? max : initial;
    }

    /**
     * Multiplies an int packed color and returns a new int packed number.
     *
     * @param color  The base color.
     * @param factor The value to multiply the color by. Less than 1 will darken. Greater than 1 will lighten.
     * @return The resulting color as a packed integer.
     */
    public static int multiplyColor(int color, float factor) {

        final int a = color >> 24 & 0xFF;
        final int r = (int) ((color >> 16 & 0xFF) * factor);
        final int g = (int) ((color >> 8 & 0xFF) * factor);
        final int b = (int) ((color & 0xFF) * factor);

        return a << 24 | r << 16 | g << 8 | b;
    }

    /**
     * Creates a rotated variant of a voxel shape for each horizontal direction. The default/base input is expected to
     * be oriented north.
     *
     * @param x1 The min x coordinate.
     * @param y1 The min y coordinate.
     * @param z1 The min z coordinate.
     * @param x2 The max x coordinate.
     * @param y2 The max y coordinate.
     * @param z2 The max z coordinate.
     * @return A map containing rotated shape variants for each horizontal direction.
     */
    public static Map<Direction, VoxelShape> createHorizontalShapes(double x1, double y1, double z1, double x2, double y2, double z2) {

        final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        Direction.Plane.HORIZONTAL.forEach(dir -> shapes.put(dir, rotateShape(dir, x1, y1, z1, x2, y2, z2)));
        return shapes;
    }

    /**
     * Creates a voxel shape that has been rotated a given direction. The default/base input is expected to be oriented
     * north. Thanks to Gigaherz for writing the original version of this code.
     *
     * @param facing The direction to rotate the shape.
     * @param x1     The min x coordinate.
     * @param y1     The min y coordinate.
     * @param z1     The min z coordinate.
     * @param x2     The max x coordinate.
     * @param y2     The max y coordinate.
     * @param z2     The max z coordinate.
     * @return A voxel shape that has been rotated in the specified direction.
     */
    public static VoxelShape rotateShape(Direction facing, double x1, double y1, double z1, double x2, double y2, double z2) {

        switch (facing) {
            case NORTH:
                return Block.box(x1, y1, z1, x2, y2, z2);
            case EAST:
                return Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
            case SOUTH:
                return Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
            case WEST:
                return Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
            default:
                throw new IllegalArgumentException("Can not rotate face in direction " + facing.name());
        }
    }
}