package net.darkhax.bookshelf.common.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class MathsHelper {

    /**
     * A RNG source that can be used in contexts where a more suitable RNG source is not available.
     */
    public static final Random RANDOM = new SecureRandom();

    /**
     * A RandomSource that can be used in contexts where a more suitable RNG source is not available.
     */
    public static final RandomSource RANDOM_SOURCE = RandomSource.create();

    /**
     * A decimal format that will only preserve two decimal places.
     */
    public static final DecimalFormat DECIMAL_2 = new DecimalFormat("##.##");

    /**
     * Checks if a double is within the given range.
     *
     * @param min   The smallest value that is valid.
     * @param max   The largest value that is valid.
     * @param value The value to check.
     * @return If the value is within the defined range.
     */
    public static boolean inRange(double min, double max, double value) {
        return value <= max && value >= min;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param first  The first position.
     * @param second The second position.
     * @return The distance between the first and second position.
     */
    public static double distance(Vec3 first, Vec3 second) {
        final double distanceX = first.x - second.x;
        final double distanceY = first.y - second.y;
        final double distanceZ = first.z - second.z;
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
    }

    /**
     * Rounds a double with a certain amount of precision.
     *
     * @param value  The value to round.
     * @param places The amount of decimal places to preserve.
     * @return The rounded value.
     */
    public static double round(double value, int places) {
        return value >= 0 && places > 0 ? BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue() : value;
    }

    /**
     * Generates a pseudorandom number within a given range of values. The range of values is inclusive of the minimum
     * and maximum value.
     *
     * @param rng The RNG source to generate the number.
     * @param min The minimum value to generate.
     * @param max The maximum value to generate.
     * @return A pseudorandom number within the provided range.
     */
    public static int nextInt(Random rng, int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

    /**
     * Generates a pseudorandom number within a given range of values. The range of values is inclusive of the minimum
     * and maximum value.
     *
     * @param rng The RNG source to generate the number.
     * @param min The minimum value to generate.
     * @param max The maximum value to generate.
     * @return A pseudorandom number within the provided range.
     */
    public static int nextInt(RandomSource rng, int min, int max) {
        return rng.nextIntBetweenInclusive(min, max);
    }

    /**
     * Performs an RNG check that has a percent chance to succeed.
     *
     * @param chance The chance that the check will succeed.
     * @return Returns true when the RNG check is successful.
     */
    public static boolean percentChance(double chance) {
        return Math.random() < chance;
    }

    /**
     * Calculates the average of many integers.
     *
     * @param values The values to average.
     * @return The average of the input values.
     */
    public static float average(int... values) {
        return Arrays.stream(values).sum() / (float) values.length;
    }

    /**
     * Calculates the percentage out of a total.
     *
     * @param value The value that is available.
     * @param total The largest possible value.
     * @return The calculated percentage.
     */
    public static float percentage(int value, int total) {
        return (float) value / (float) total;
    }

    /**
     * Converts a standard pixel measurement to a world-space measurement. This assumes one block in the world
     * represents 16 pixels.
     *
     * @param pixels The amount of pixels.
     * @return The size of the pixels in the world-space.
     */
    public static double pixelSize(int pixels) {
        return pixels / 16d;
    }

    /**
     * Creates an Axis Aligned Bounding Box from a series of pixel measurements.
     *
     * @param minX The start on the X axis.
     * @param minY The start on the Y axis.
     * @param minZ The start on the Z axis.
     * @param maxX The end on the X axis.
     * @param maxY The end on the Y axis.
     * @param maxZ The end on the Z axis.
     * @return An AABB that represents a series of block pixel measurements.
     */
    public static AABB boundsForPixels(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new AABB(pixelSize(minX), pixelSize(minY), pixelSize(minZ), pixelSize(maxX), pixelSize(maxY), pixelSize(maxZ));
    }

    /**
     * Creates horizontally rotated variants of a VoxelShape. The input values are considered to be rotated north.
     *
     * @param minX The min X of the shape.
     * @param minY The min Y of the shape.
     * @param minZ The min Z of the shape.
     * @param maxX The max X of the shape.
     * @param maxY The max Y of the shape.
     * @param maxZ The max Z of the shape.
     * @return A map of rotated VoxelShape.
     */
    public static Map<Direction, VoxelShape> createHorizontalShapes(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

        final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        Direction.Plane.HORIZONTAL.forEach(dir -> shapes.put(dir, rotateShape(dir, minX, minY, minZ, maxX, maxY, maxZ)));
        return shapes;
    }

    /**
     * Creates a VoxelShape that has been rotated to face a given direction. The input sizes are considered to be
     * rotated north already. The up/down rotations are not supported yet.
     *
     * @param facing
     * @param x1     The min x coordinate.
     * @param y1     The min y coordinate.
     * @param z1     The min z coordinate.
     * @param x2     The max x coordinate.
     * @param y2     The max y coordinate.
     * @param z2     The max z coordinate.
     * @return The rotated VoxelShape.
     */
    public static VoxelShape rotateShape(Direction facing, double x1, double y1, double z1, double x2, double y2, double z2) {
        return switch (facing) {
            case NORTH -> Block.box(x1, y1, z1, x2, y2, z2);
            case EAST -> Block.box(16 - z2, y1, x1, 16 - z1, y2, x2);
            case SOUTH -> Block.box(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
            case WEST -> Block.box(z1, y1, 16 - x2, z2, y2, 16 - x1);
            default -> throw new IllegalArgumentException("Can not rotate face in direction " + facing.name());
        };
    }


    /**
     * Offsets a position horizontally by a random amount.
     *
     * @param startPos The starting position to offset from.
     * @param rng      The RNG source.
     * @param range    The maximum amount of blocks to offset the position by. This range applies to both the positive
     *                 and negative directions.
     * @return The randomly offset position.
     */
    public static BlockPos randomOffsetHorizontal(BlockPos startPos, RandomSource rng, int range) {
        return randomOffset(startPos, rng, range, 0, range);
    }

    /**
     * Offsets a position by a random amount within a limited range.
     *
     * @param startPos The starting position to offset from.
     * @param rng      The RNG source.
     * @param rangeX   The maximum amount of blocks to offset on the X axis.
     * @param rangeY   The maximum amount of blocks to offset on the Y axis.
     * @param rangeZ   The maximum amount of blocks to offset on the Z axis.
     * @return The randomly offset position.
     */
    public static BlockPos randomOffset(BlockPos startPos, RandomSource rng, int rangeX, int rangeY, int rangeZ) {
        if (rangeX < 0 || rangeY < 0 || rangeZ < 0) {
            throw new IllegalArgumentException("Cannot offset position by '" + rangeX + ", " + rangeY + ", " + rangeZ + "'. Range must be positive!");
        }
        final int offsetX = rangeX != 0 ? rng.nextIntBetweenInclusive(-rangeX, rangeX) : 0;
        final int offsetY = rangeY != 0 ? rng.nextIntBetweenInclusive(-rangeY, rangeY) : 0;
        final int offsetZ = rangeZ != 0 ? rng.nextIntBetweenInclusive(-rangeZ, rangeZ) : 0;
        return startPos.offset(offsetX, offsetY, offsetZ);
    }

}