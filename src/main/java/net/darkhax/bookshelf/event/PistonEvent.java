package net.darkhax.bookshelf.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class PistonEvent extends BlockEvent {
    public final EnumFacing facing;
    public final boolean sticky;

    public PistonEvent (int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {

        super(x, y, z, world, block, blockMetadata);

        this.sticky = sticky;
        this.facing = facing;
    }

    @Cancelable
    public static class PistonExtendEvent extends PistonEvent {
        public PistonExtendEvent (int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {

            super(x, y, z, world, block, blockMetadata, sticky, facing);
        }
    }

    @Cancelable
    public static class PistonRetractEvent extends PistonEvent {
        public PistonRetractEvent (int x, int y, int z, World world, Block block, int blockMetadata, boolean sticky, EnumFacing facing) {

            super(x, y, z, world, block, blockMetadata, sticky, facing);
        }
    }

    @HasResult
    public static class PistonPushBlock extends PistonEvent {
        public final Block pushedBlock;

        public PistonPushBlock (Block block, World world, int x, int y, int z, int blockMetadata, boolean sticky, Block pushedBlock, EnumFacing facing) {

            super(x, y, z, world, block, blockMetadata, sticky, facing);

            this.pushedBlock = pushedBlock;
        }
    }
}