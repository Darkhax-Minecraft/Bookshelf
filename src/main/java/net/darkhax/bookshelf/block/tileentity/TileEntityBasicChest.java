/**
 * This class was created by <SanAndreasP>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 22:33:44 (GMT)]
 *
 * Changes
 * - Added support for the ChestBuilder system
 * - Repackaged and formated for Bookshelf standards.
 * - Added javadocs.
 */
package net.darkhax.bookshelf.block.tileentity;

import net.darkhax.bookshelf.builder.ChestBuilder;
import net.darkhax.bookshelf.builder.ChestBuilder.IChestType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is part of the ChestBuilder system. Please see
 * {@link net.darkhax.bookshelf.builder.ChestBuilder} for more info.
 */
public class TileEntityBasicChest extends TileEntityChest {

    /**
     * The builder that this tile entity belongs to.
     */
    private ChestBuilder builder;

    /**
     * The custom type for the chest.
     */
    private IChestType type = ChestBuilder.NONE;

    /**
     * The last known chest type string.
     */
    private String typeString = "";

    public TileEntityBasicChest () {

    }

    public TileEntityBasicChest (ChestBuilder builder) {

        this.builder = builder;
    }

    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound nbt) {

        super.writeToNBT(nbt);
        nbt.setString("builder", this.builder.getModid());
        nbt.setString("type", this.type == null ? this.typeString : this.type.getName());
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag () {

        final NBTTagCompound nbt = super.getUpdateTag();
        nbt.setString("builder", this.builder.getModid());
        nbt.setString("type", this.type == null ? this.typeString : this.type.getName());
        return nbt;
    }

    @Override
    public void handleUpdateTag (NBTTagCompound tag) {

        super.handleUpdateTag(tag);
        this.builder = ChestBuilder.BUILDERS.get(tag.getString("builder"));
        this.type = this.builder.getChestType(tag.getString("type"));

        if (this.type != null) {

            this.typeString = this.type.getName();
        }
    }

    @Override
    public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newState) {

        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket () {

        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("builder", this.builder.getModid());
        nbt.setString("type", this.type == null ? this.typeString : this.type.getName());
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {

        this.builder = ChestBuilder.BUILDERS.get(pkt.getNbtCompound().getString("builder"));
        this.type = this.builder.getChestType(pkt.getNbtCompound().getString("type"));

        if (this.type != null) {

            this.typeString = this.type.getName();
        }
    }

    @Override
    public void readFromNBT (NBTTagCompound nbt) {

        super.readFromNBT(nbt);
        this.builder = ChestBuilder.BUILDERS.get(nbt.getString("builder"));
        this.type = this.builder.getChestType(nbt.getString("type"));

        if (this.type != null) {

            this.typeString = this.type.getName();
        }
    }

    @Override
    protected TileEntityChest getAdjacentChest (EnumFacing side) {

        final BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos)) {

            final TileEntity tileentity = this.getWorld().getTileEntity(blockpos);

            if (tileentity instanceof TileEntityBasicChest) {

                final TileEntityBasicChest tileentitychest = (TileEntityBasicChest) tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox () {

        return new AxisAlignedBB(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ() - 1, this.pos.getX() + 2, this.pos.getY() + 2, this.pos.getZ() + 2);
    }

    /**
     * Gets the custom chest type.
     *
     * @return The custom chest type.
     */
    public IChestType getType () {

        return this.type;
    }

    /**
     * Sets the custom chest type.
     *
     * @param type The custom chest type.
     */
    public void setType (IChestType type) {

        this.type = type;
    }

    /**
     * Checks if there is a chest at the target position.
     *
     * @param posIn The position to check.
     * @return Whether or not there is a chest at the position, and it's type is equal to this
     *         type.
     */
    private boolean isChestAt (BlockPos posIn) {

        if (this.getWorld() == null) {

            return false;
        }

        else {

            final Block block = this.getWorld().getBlockState(posIn).getBlock();
            final TileEntity te = this.getWorld().getTileEntity(posIn);
            return block instanceof BlockChest && ((BlockChest) block).chestType == this.getChestType() && te instanceof TileEntityBasicChest && ((TileEntityBasicChest) te).type == this.type;
        }
    }

    /**
     * Sets the neighbor chest.
     *
     * @param chestTe The chest tile entity to set.
     * @param side The face to set it to.
     */
    @SuppressWarnings("incomplete-switch")
    private void setNeighbor (TileEntityChest chestTe, EnumFacing side) {

        if (chestTe.isInvalid()) {

            this.adjacentChestChecked = false;
        }

        else if (this.adjacentChestChecked) {

            switch (side) {

                case NORTH:
                    if (this.adjacentChestZNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }
                    break;

                case SOUTH:
                    if (this.adjacentChestZPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }
                    break;

                case EAST:
                    if (this.adjacentChestXPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }
                    break;

                case WEST:
                    if (this.adjacentChestXNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    /**
     * Gets the chest builder.
     *
     * @return The chest builder.
     */
    public ChestBuilder getBuilder () {

        return this.builder;
    }
}