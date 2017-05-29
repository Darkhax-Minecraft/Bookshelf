package net.darkhax.bookshelftest;

import net.darkhax.bookshelf.item.ItemSubType;
import net.darkhax.bookshelf.lib.RegistryHelper;
import net.darkhax.bookshelf.lib.RegistryListener;
import net.darkhax.bookshelf.lib.RegistrySubscribe;
import net.minecraft.block.Block;
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
    }
}