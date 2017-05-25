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
package net.darkhax.bookshelf.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.block.tileentity.TileEntityBasicChest;
import net.darkhax.bookshelf.builder.ChestBuilder;
import net.darkhax.bookshelf.builder.ChestBuilder.IChestType;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

/**
 * This class is part of the ChestBuilder system. Please see
 * {@link net.darkhax.bookshelf.builder.ChestBuilder} for more info.
 */
public class BlockBasicChest extends BlockChest {

    /**
     * The builder that made this chest.
     */
    private final ChestBuilder builder;

    public BlockBasicChest (ChestBuilder builder, Type type) {

        super(type);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.builder = builder;
    }

    @Override
    public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {

        final IChestType myType = this.getCustomType(source, pos);
        return this.getCustomType(source, pos.north()) == myType ? NORTH_CHEST_AABB : this.getCustomType(source, pos.south()) == myType ? SOUTH_CHEST_AABB : this.getCustomType(source, pos.west()) == myType ? WEST_CHEST_AABB : this.getCustomType(source, pos.east()) == myType ? EAST_CHEST_AABB : NOT_CONNECTED_AABB;
    }

    @Override
    public void onBlockAdded (World worldIn, BlockPos pos, IBlockState state) {

        // NO-OP
    }

    @Override
    public IBlockState getStateForPlacement (World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        final EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, facing);
        final BlockPos northPos = pos.north();
        final BlockPos southPos = pos.south();
        final BlockPos westPos = pos.west();
        final BlockPos eastPos = pos.east();

        final IChestType myType = this.getCustomType(stack);

        final boolean northChest = myType == this.getCustomType(worldIn, northPos);
        final boolean southChest = myType == this.getCustomType(worldIn, southPos);
        final boolean westChest = myType == this.getCustomType(worldIn, westPos);
        final boolean eastChest = myType == this.getCustomType(worldIn, eastPos);

        if (!northChest && !southChest && !westChest && !eastChest) {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (facing.getAxis() != EnumFacing.Axis.X || !northChest && !southChest) {
            if (facing.getAxis() == EnumFacing.Axis.Z && (westChest || eastChest)) {
                if (westChest) {
                    this.setState(worldIn, westPos, state, 3);
                }
                else {
                    this.setState(worldIn, eastPos, state, 3);
                }

                worldIn.setBlockState(pos, state, 3);
            }
            else {
                final EnumFacing corrected = facing.rotateY();
                this.setState(worldIn, pos, state.withProperty(FACING, corrected), 3);
                if (northChest) {
                    this.setState(worldIn, northPos, state.withProperty(FACING, corrected), 3);
                }
                else if (southChest) {
                    this.setState(worldIn, southPos, state.withProperty(FACING, corrected), 3);
                }
                else if (westChest) {
                    this.setState(worldIn, westPos, state.withProperty(FACING, corrected), 3);
                }
                else if (eastChest) {
                    this.setState(worldIn, eastPos, state.withProperty(FACING, corrected), 3);
                }
            }
        }
        else {
            if (northChest) {
                this.setState(worldIn, northPos, state, 3);
            }
            else {
                this.setState(worldIn, southPos, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }

        final TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityBasicChest) {
            final TileEntityBasicChest chest = (TileEntityBasicChest) te;
            if (stack.hasDisplayName()) {
                chest.setCustomName(stack.getDisplayName());
            }

            chest.setType(myType);
        }

        this.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public boolean canProvidePower (IBlockState state) {

        return this.isTrapChest();
    }

    @Override
    @Deprecated
    public IBlockState checkForSurroundingChests (World worldIn, BlockPos pos, IBlockState state) {

        return state;
    }

    @Override
    @Deprecated
    public IBlockState correctFacing (World worldIn, BlockPos pos, IBlockState state) {

        return this.correctFacing(worldIn, pos, state, ChestBuilder.NONE);
    }

    @Override
    public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {

        return true;
    }

    @Override
    public boolean removedByPlayer (IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock (World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {

        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);

        if (te instanceof TileEntityBasicChest) {

            te.invalidate();
        }
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {

        return new TileEntityBasicChest(this.builder);
    }

    @Override
    public List<ItemStack> getDrops (IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

        return new ArrayList<>(Collections.singletonList(this.setCustomType(new ItemStack(this, 1), this.getCustomType(world, pos))));
    }

    @Override
    public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

        return this.setCustomType(new ItemStack(this, 1), this.getCustomType(world, pos));
    }

    @Override
    public ILockableContainer getContainer (World world, BlockPos pos, boolean locked) {

        final TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof TileEntityBasicChest)) {
            return null;
        }
        else {
            ILockableContainer myChest = (TileEntityBasicChest) tile;
            final IChestType myType = ((TileEntityBasicChest) tile).getType();

            if (!locked && this.isBlocked(world, pos)) {
                return null;
            }
            else {
                for (final EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
                    final BlockPos adjPos = pos.offset(facing);

                    final TileEntity adjTile = world.getTileEntity(adjPos);

                    if (world.getBlockState(adjPos).getBlock() == this && adjTile instanceof TileEntityBasicChest && ((TileEntityBasicChest) adjTile).getType() == myType) {
                        if (this.isBlocked(world, adjPos)) {
                            return null;
                        }

                        if (facing != EnumFacing.WEST && facing != EnumFacing.NORTH) {
                            myChest = new InventoryLargeChest("container.chestDouble", myChest, (TileEntityBasicChest) adjTile);
                        }
                        else {
                            myChest = new InventoryLargeChest("container.chestDouble", (TileEntityBasicChest) adjTile, myChest);
                        }
                    }
                }

                return myChest;
            }
        }
    }

    /**
     * Checks if the chest is a trap chest or not.
     *
     * @return Whether or not the chest is a trap chest.
     */
    public boolean isTrapChest () {

        return this.chestType == this.builder.getTrapType();
    }

    /**
     * Sets the state of the block, and updates the chest connections.
     *
     * @param worldIn The world instance.
     * @param pos The position.
     * @param state The state.
     * @param flag The update flags.
     */
    public void setState (World worldIn, BlockPos pos, IBlockState state, int flag) {

        final TileEntity te = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, state, flag);
        if (te != null) {
            te.validate();
            worldIn.setTileEntity(pos, te);

            if (te instanceof TileEntityBasicChest) {
                ((TileEntityBasicChest) te).adjacentChestChecked = false;
            }
        }
    }

    /**
     * Gets the correct facing state.
     *
     * @param worldIn The world instance.
     * @param pos The position.
     * @param state The state.
     * @param myType The custom chest type.
     * @return The correct facing state.
     */
    public IBlockState correctFacing (World worldIn, BlockPos pos, IBlockState state, IChestType myType) {

        EnumFacing facing = null;

        for (final EnumFacing horizFace : EnumFacing.Plane.HORIZONTAL) {
            if (this.getCustomType(worldIn, pos.offset(horizFace)) == myType) {
                return state;
            }

            if (worldIn.getBlockState(pos.offset(horizFace)).isFullBlock()) {
                if (facing != null) {
                    facing = null;
                    break;
                }

                facing = horizFace;
            }
        }

        if (facing != null) {
            return state.withProperty(FACING, facing.getOpposite());
        }
        else {
            EnumFacing enumfacing2 = state.getValue(FACING);

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
                enumfacing2 = enumfacing2.getOpposite();
            }

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
                enumfacing2 = enumfacing2.rotateY();
            }

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
                enumfacing2 = enumfacing2.getOpposite();
            }

            return state.withProperty(FACING, enumfacing2);
        }
    }

    /**
     * Checks if a chest is a double chest.
     *
     * @param worldIn The world instance.
     * @param pos The position.
     * @param myType The custom chest type.
     * @return Whether or not the chest is a double chest.
     */
    public boolean isDoubleChest (World worldIn, BlockPos pos, IChestType myType) {

        if (this.getCustomType(worldIn, pos) != myType) {
            return false;
        }
        else {
            final IChestType theType = this.getCustomType(worldIn, pos);
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.getCustomType(worldIn, pos.offset(enumfacing)) == theType) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Gets the custom chest type.
     *
     * @param world The world instance.
     * @param pos The position.
     * @return The custom chest type.
     */
    public IChestType getCustomType (IBlockAccess world, BlockPos pos) {

        if (world.getBlockState(pos).getBlock() == this) {
            final TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBasicChest) {
                return ((TileEntityBasicChest) te).getType();
            }
        }

        return ChestBuilder.NONE;
    }

    /**
     * Gets the custom chest type.
     *
     * @param stack The ItemStack to read from.
     * @return The custom chest type for the stack.
     */
    public IChestType getCustomType (ItemStack stack) {

        final NBTTagCompound tag = StackUtils.prepareStackTag(stack);
        return this.builder.getChestType(tag.getString("type"));
    }

    /**
     * Sets the type of a stack.
     *
     * @param stack The ItemStack to modify.
     * @param type The custom chest type to set.
     * @return The ItemStack, after it has been modified.
     */
    public ItemStack setCustomType (ItemStack stack, IChestType type) {

        final NBTTagCompound tag = StackUtils.prepareStackTag(stack);
        tag.setString("type", type.getName());
        return stack;
    }

    /**
     * Checks if the chest is blocked.
     *
     * @param worldIn The world instance.
     * @param pos The position of the chest.
     * @return Whether or not the chest is blocked off.
     */
    private boolean isBlocked (World worldIn, BlockPos pos) {

        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }

    /**
     * Checks if there is a solid block over the chest.
     *
     * @param worldIn The world instance.
     * @param pos The chest position.
     * @return Whether or not there is a solid block above the chest.
     */
    private boolean isBelowSolidBlock (World worldIn, BlockPos pos) {

        return worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN);
    }

    /**
     * Checks if there is an ocelot sitting on the chest.
     *
     * @param worldIn The world instance.
     * @param pos The position of the chest.
     * @return Whether or not there is a cat on the chest block.
     */
    private boolean isOcelotSittingOnChest (World worldIn, BlockPos pos) {

        for (final Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            final EntityOcelot cat = (EntityOcelot) entity;

            if (cat.isSitting()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the builder that made this chest.
     *
     * @return The builder that made this chest.
     */
    public ChestBuilder getBuilder () {

        return this.builder;
    }
}