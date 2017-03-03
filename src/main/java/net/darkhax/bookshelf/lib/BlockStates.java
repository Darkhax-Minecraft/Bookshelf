package net.darkhax.bookshelf.lib;

import net.darkhax.bookshelf.lib.properties.PropertyObject;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class BlockStates {

    /**
     * Used to handle whether or not the block is on or off. Used mainly by redstone blocks.
     */
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    /**
     * Used to hold another block state. This is useful for mimicking other blocks.
     */
    public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<>("held_state", IBlockState.class);

    /**
     * Used to hold an IBlockAccess of the block.
     */
    public static final PropertyObject<IBlockAccess> BLOCK_ACCESS = new PropertyObject<>("world", IBlockAccess.class);

    /**
     * Used to hold the BlockPos of the block.
     */
    public static final PropertyObject<BlockPos> BLOCKPOS = new PropertyObject<>("pos", BlockPos.class);

    /**
     * Used to determine the color of a block. Only supports the 16 vanilla colors.
     */
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    /**
     * Used to determine the direction a block is facing.
     */
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    /**
     * Used to determine if a block is connected on the bottom face.
     */
    public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");

    /**
     * Used to determine if a block is connected on the upward face.
     */
    public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");

    /**
     * Used to determine if a block is connected on the northern face.
     */
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");

    /**
     * Used to determine if a block is connected on the southern face.
     */
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");

    /**
     * Used to determine if a block is connected on the eastern face.
     */
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");

    /**
     * Used to determine if a block is connected on the western face.
     */
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");

    /**
     * Used to determine if a block has been enabled or not.
     */
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private BlockStates () {

        throw new IllegalAccessError("Utility class");
    }
}