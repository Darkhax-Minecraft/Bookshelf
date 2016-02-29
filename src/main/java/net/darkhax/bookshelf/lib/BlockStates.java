package net.darkhax.bookshelf.lib;

import net.darkhax.bookshelf.lib.properties.PropertyObject;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStates {
    
    /**
     * Used to handle whether or not the block is on or off. Used mainly by redstone blocks.
     */
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    
    /**
     * Used to hold another block state. This is useful for mimicking other blocks.
     */
    public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<IBlockState>("held_state", IBlockState.class);
    
    /**
     * Used to hold an IBlockAccess of the block.
     */
    public static final PropertyObject<IBlockAccess> BLOCK_ACCESS = new PropertyObject<IBlockAccess>("world", IBlockAccess.class);
    
    /**
     * Used to hold the BlockPos of the block.
     */
    public static final PropertyObject<BlockPos> BLOCKPOS = new PropertyObject<BlockPos>("pos", BlockPos.class);
    
    /**
     * Used to determine the color of a block. Only supports the 16 vanilla colors.
     */
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
}