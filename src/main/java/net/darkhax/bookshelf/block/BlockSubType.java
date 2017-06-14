package net.darkhax.bookshelf.block;

import net.darkhax.bookshelf.block.property.PropertyString;
import net.darkhax.bookshelf.registry.IVariant;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockSubType extends Block implements IVariant {

    private final String[] variants;

    public BlockSubType (Material blockMaterialIn, MapColor blockMapColorIn, String... variants) {

        super(blockMaterialIn, blockMapColorIn);
        this.variants = variants;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {

        for (int meta = 0; meta < this.variants.length; meta++) {

            list.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {

        return this.getDefaultState().withProperty(this.getVariantProp(), this.variants[meta]);
    }

    @Override
    public int getMetaFromState (IBlockState state) {

        return this.getVariantProp().getMetaData(state.getValue(this.getVariantProp()));
    }

    @Override
    protected BlockStateContainer createBlockState () {

        return new BlockStateContainer(this, new IProperty[] { this.getVariantProp() });
    }

    @Override
    public int damageDropped (IBlockState state) {

        return this.getMetaFromState(state);
    }

    @Override
    public String[] getVariant () {

        return this.variants;
    }

    public abstract PropertyString getVariantProp ();
}