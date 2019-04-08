/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.EnumFacing;

public final class Blockstates {
    
    /**
     * Used to handle whether or not the block is on or off. Used mainly by redstone blocks.
     */
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    
    /**
     * Used to determine the color of a block. Only supports the 16 vanilla colors.
     */
    public static final EnumProperty<EnumDyeColor> COLOR = EnumProperty.create("color", EnumDyeColor.class);
    
    /**
     * Used to determine the direction a block is facing.
     */
    public static final EnumProperty<EnumFacing> FACING = EnumProperty.create("facing", EnumFacing.class);
    
    /**
     * Used to determine the direction a block is facing. Only includes horizontal directions.
     * (N-S-W-E)
     */
    public static final DirectionProperty HORIZONTAL = DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    /**
     * Used to determine if a block is connected on the bottom face.
     */
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
    
    /**
     * Used to determine if a block is connected on the upward face.
     */
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    
    /**
     * Used to determine if a block is connected on the northern face.
     */
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
    
    /**
     * Used to determine if a block is connected on the southern face.
     */
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    
    /**
     * Used to determine if a block is connected on the eastern face.
     */
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");
    
    /**
     * Used to determine if a block is connected on the western face.
     */
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
    
    /**
     * Used to determine if a block has been enabled or not.
     */
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
}