package net.darkhax.bookshelf.features.bookshelves;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWoodenShelf extends BlockBookshelf {

    public static String[] types = new String[] { "spruce", "birch", "jungle", "acacia", "dark_oak" };

    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType> create("variant", EnumType.class);

    public BlockWoodenShelf () {

        this.setUnlocalizedName("bookshelf.bookshelf");
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.SPRUCE));
        this.setRegistryName("bookshelf");
        this.setHardness(1.5f);
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {

        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState (IBlockState state) {

        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState () {

        return new BlockStateContainer(this, new IProperty[] { VARIANT });
    }

    @Override
    public float getEnchantPowerBonus (World world, BlockPos pos) {

        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

        for (int meta = 0; meta < 5; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    public static enum EnumType implements IStringSerializable {

        SPRUCE(0, "spruce", "Spruce"),
        BIRCH(1, "birch", "Birch"),
        JUNGLE(2, "jungle", "Jungle"),
        ACACIA(3, "acacia", "Acacia"),
        DARK_OAK(4, "dark_oak", "DarkOak");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];

        private final int meta;

        private final String name;

        private final String oreName;

        private EnumType (int meta, String name, String oredict) {

            this.meta = meta;
            this.name = name;
            this.oreName = oredict;
        }

        public int getMetadata () {

            return this.meta;
        }

        @Override
        public String toString () {

            return this.name;
        }

        public static EnumType byMetadata (int meta) {

            int lookupMeta = meta;

            if (meta < 0 || meta >= META_LOOKUP.length) {
                lookupMeta = 0;
            }

            return META_LOOKUP[lookupMeta];
        }

        @Override
        public String getName () {

            return this.name;
        }

        public String getOreName () {

            return this.oreName;
        }

        static {

            for (final EnumType type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }
}
