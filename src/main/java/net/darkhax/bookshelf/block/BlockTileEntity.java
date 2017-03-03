package net.darkhax.bookshelf.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An alternative to {@link BlockContainer}. This version does not have the invisible render
 * type or weird invalid neighbor code.
 */
public abstract class BlockTileEntity extends Block implements ITileEntityProvider {

    /**
     * Basic block constructor.
     *
     * @param material The material type of the block.
     */
    protected BlockTileEntity (Material material) {

        this(material, material.getMaterialMapColor());
    }

    /**
     * Basic block constructor.
     *
     * @param material The material type of the block.
     * @param color The color to use for the block on the map.
     */
    protected BlockTileEntity (Material material, MapColor color) {

        super(material, color);
        this.isBlockContainer = true;
    }

    @Override
    public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {

        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived (IBlockState state, World worldIn, BlockPos pos, int id, int param) {

        super.eventReceived(state, worldIn, pos, id, param);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
}
