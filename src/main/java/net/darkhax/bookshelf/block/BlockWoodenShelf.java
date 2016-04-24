package net.darkhax.bookshelf.block;

import java.util.List;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWoodenShelf extends BlockBookshelf {
    
    public static String[] types = new String[] { "spruce", "birch", "jungle", "acacia", "dark_oak" };
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType> create("variant", EnumType.class);
    
    public BlockWoodenShelf() {
        
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
    public void getSubBlocks (Item item, CreativeTabs tab, List<ItemStack> list) {
        
        for (int meta = 0; meta < 5; meta++)
            list.add(new ItemStack(item, 1, meta));
    }
    
    public static enum EnumType implements IStringSerializable {
        
        SPRUCE(0, "spruce"),
        BIRCH(1, "birch"),
        JUNGLE(2, "jungle"),
        ACACIA(3, "acacia"),
        DARK_OAK(4, "dark_oak");
        
        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String name;
        
        private EnumType(int meta, String name) {
            
            this.meta = meta;
            this.name = name;
        }
        
        public int getMetadata () {
            
            return this.meta;
        }
        
        @Override
        public String toString () {
            
            return this.name;
        }
        
        public static EnumType byMetadata (int meta) {
            
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;
                
            return META_LOOKUP[meta];
        }
        
        @Override
        public String getName () {
            
            return this.name;
        }
        
        static {
            
            for (final EnumType type : values())
                META_LOOKUP[type.getMetadata()] = type;
        }
    }
}
