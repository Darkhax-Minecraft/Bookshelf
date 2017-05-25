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
package net.darkhax.bookshelf.item;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.block.BlockBasicChest;
import net.darkhax.bookshelf.block.tileentity.TileEntityBasicChest;
import net.darkhax.bookshelf.builder.ChestBuilder;
import net.darkhax.bookshelf.builder.ChestBuilder.IChestType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is part of the ChestBuilder system. Please see
 * {@link net.darkhax.bookshelf.builder.ChestBuilder} for more info.
 */
public class ItemBlockChest extends ItemBlock implements ICustomMesh {

    /**
     * The chest builder that this belongs to.
     */
    private final ChestBuilder builder;

    public ItemBlockChest (Block block, ChestBuilder builder) {

        super(block);
        this.setHasSubtypes(true);
        this.builder = builder;
    }

    @Override
    public String getUnlocalizedName (ItemStack stack) {

        final BlockBasicChest cChest = (BlockBasicChest) this.getBlock();
        final IChestType myType = cChest.getCustomType(stack);

        return myType != null ? this.block.getUnlocalizedName() + "." + myType.getName() + (cChest.isTrapChest() ? ".trap" : "") : this.block.getUnlocalizedName();
    }

    @Override
    public int getMetadata (int damage) {

        return 0;
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {

        int typeCnt = 0;

        final BlockPos posN = pos.north();
        final BlockPos posS = pos.south();
        final BlockPos posW = pos.west();
        final BlockPos posE = pos.east();

        final BlockBasicChest cChest = (BlockBasicChest) this.getBlock();
        final IChestType myType = cChest.getCustomType(stack);

        if (world.getBlockState(posN).getBlock() == this.block && cChest.getCustomType(world, posN) == myType) {
            typeCnt += cChest.isDoubleChest(world, posN, myType) ? 2 : 1;
        }
        if (world.getBlockState(posS).getBlock() == this.block && cChest.getCustomType(world, posS) == myType) {
            typeCnt += cChest.isDoubleChest(world, posS, myType) ? 2 : 1;
        }
        if (world.getBlockState(posW).getBlock() == this.block && cChest.getCustomType(world, posW) == myType) {
            typeCnt += cChest.isDoubleChest(world, posW, myType) ? 2 : 1;
        }
        if (world.getBlockState(posE).getBlock() == this.block && cChest.getCustomType(world, posE) == myType) {
            typeCnt += cChest.isDoubleChest(world, posE, myType) ? 2 : 1;
        }

        if (typeCnt <= 1 && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            final TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBasicChest) {
                ((TileEntityBasicChest) te).setType(myType);
                return true;
            }
        }

        return false;
    }

    @Override
    public void getSubItems (Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {

        final BlockBasicChest chest = (BlockBasicChest) Block.getBlockFromItem(itemIn);

        for (final IChestType type : chest.getBuilder().getTypes()) {
            subItems.add(chest.setCustomType(new ItemStack(itemIn, 1), type));
        }
    }

    @Override
    public ItemMeshDefinition getCustomMesh () {

        return stack -> {

            final BlockBasicChest chest = (BlockBasicChest) ItemBlockChest.this.getBlock();
            final IChestType type = ((BlockBasicChest) ItemBlockChest.this.getBlock()).getCustomType(stack);
            return chest.chestType == chest.getBuilder().getNormalType() ? type.getNormalItemModel() : type.getTrapItemModel();
        };
    }

    @Override
    public void registerMeshModels () {

        final List<ResourceLocation> models = new ArrayList<>();

        for (final IChestType type : this.builder.getTypes()) {

            models.add(new ResourceLocation(this.builder.getModid(), "chest_" + type.getName()));

            if (this.builder.useTraps()) {

                models.add(new ResourceLocation(this.builder.getModid(), "chest_trap_" + type.getName()));
            }
        }

        ModelBakery.registerItemVariants(this, models.toArray(new ResourceLocation[models.size()]));
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