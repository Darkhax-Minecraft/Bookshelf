package net.darkhax.bookshelf.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class PistonEvent extends BlockEvent {
    
    /**
     * The direction the piston block is facing.
     */
    public final EnumFacing facing;
    
    /**
     * Whether or not the piston is sticky.
     */
    public final boolean sticky;
    
    /**
     * Constructs a new PistonEvent which is used to detect and modify piston logic. See
     * PistonExtendEvent, PistonRetractEvent and PistonPushEvent for more information.
     * 
     * @param x: The X coordinate of the piston block.
     * @param y: The Y coordinate of the piston block.
     * @param z: The Z coordinate of the piston block.
     * @param world: An instance of the world in which the piston is being updated.
     * @param block: An instance of the block acting as the piston.
     * @param blockMetadata: The meta value for the block acting as the piston.
     * @param sticky: Whether or not the piston is sticky.
     * @param facing: The direction that the piston is facing.
     */
    public PistonEvent(int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {
        
        super(x, y, z, world, block, blockMetadata);
        this.facing = facing;
        this.sticky = sticky;
    }
    
    @Cancelable
    public static class PistonExtendEvent extends PistonEvent {
        
        /**
         * Constructs a new PistonExtendEvent which is fired every time a piston is extended.
         * This event can be canceled, which will prevent the piston from extending. This can
         * be used to modify the behavior of a piston when it extends.
         * 
         * @param x: The X coordinate of the piston block.
         * @param y: The Y coordinate of the piston block.
         * @param z: The Z coordinate of the piston block.
         * @param world: An instance of the world in which the piston is being updated.
         * @param block: An instance of the block acting as the piston.
         * @param blockMetadata: The meta value for the block acting as the piston.
         * @param sticky: Whether or not the piston is sticky.
         * @param facing: The direction that the piston is facing.
         */
        public PistonExtendEvent(int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {
            
            super(x, y, z, world, block, blockMetadata, sticky, facing);
        }
    }
    
    @Cancelable
    public static class PistonRetractEvent extends PistonEvent {
        
        /**
         * Constructs a new PistonRetractEvent which is fired whenever a piston is retracted.
         * This event can be cancelled which will prevent a piston from retracting. This can be
         * used to modify the behavior of a piston when it retracts.
         * 
         * @param x: The X coordinate of the piston block.
         * @param y: The Y coordinate of the piston block.
         * @param z: The Z coordinate of the piston block.
         * @param world: An instance of the world in which the piston is being updated.
         * @param block: An instance of the block acting as the piston.
         * @param blockMetadata: The meta value for the block acting as the piston.
         * @param sticky: Whether or not the piston is sticky.
         * @param facing: The direction that the piston is facing.
         */
        public PistonRetractEvent(int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {
            
            super(x, y, z, world, block, blockMetadata, sticky, facing);
        }
    }
    
    @HasResult
    public static class PistonPushEvent extends PistonEvent {
        
        /**
         * The block that is being pushed by the piston. This may be null, or air.
         */
        public final Block pushedBlock;
        
        // TODO Actually implement this event.
        // TODO documentation is likely not accurate. Further discussion is needed.
        
        /**
         * This event is fired whenever a piston attempts to push a block.
         * 
         * @param x: The X coordinate of the piston block.
         * @param y: The Y coordinate of the piston block.
         * @param z: The Z coordinate of the piston block.
         * @param world: An instance of the world in which the piston is being updated.
         * @param block: An instance of the block acting as the piston.
         * @param blockMetadata: The meta value for the block acting as the piston.
         * @param sticky: Whether or not the piston is sticky.
         * @param pushedBlock: The blockt hat is being pushed by the piston.
         * @param facing: The direction that the piston is facing.
         */
        public PistonPushEvent(Block block, World world, int x, int y, int z, int blockMetadata, boolean sticky, Block pushedBlock, EnumFacing facing) {
            
            super(x, y, z, world, block, blockMetadata, sticky, facing);
            this.pushedBlock = pushedBlock;
        }
    }
}