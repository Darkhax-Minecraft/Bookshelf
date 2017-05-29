package net.darkhax.bookshelftest;

import net.darkhax.bookshelf.block.BlockSubType;
import net.darkhax.bookshelf.block.property.PropertyString;
import net.darkhax.bookshelf.item.ItemBlockBasic;
import net.darkhax.bookshelf.item.ItemSubType;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.registry.RegistryListener;
import net.darkhax.bookshelf.registry.RegistrySubscribe;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;

@RegistrySubscribe
@Mod(modid = "registrytest", name = "Registry Test", version = "1.0.0.0")
public class RegistryTest implements RegistryListener {

    public static RegistryHelper REGISTRY = new RegistryHelper("registrytest").setTab(CreativeTabs.MISC);

    @Override
    public void onRegistry () {

        REGISTRY.registerItem(new Item(), "item");
        REGISTRY.registerBlock(new Block(Material.GLASS), "block");

        REGISTRY.registerItem(new ItemSubType("first", "second", "third"), "sub_item");

        final Block subBlock = new BlockTest();
        REGISTRY.registerBlock(subBlock, new ItemBlockBasic(subBlock, BlockTest.types), "sub_block");
    }

    public static class BlockTest extends BlockSubType {

        public static String[] types = { "one", "two" };

        public BlockTest () {

            super(Material.ROCK, MapColor.GRAY, types);
        }

        @Override
        public PropertyString getVariantProp () {

            return new PropertyString("variant", types);
        }
    }
}