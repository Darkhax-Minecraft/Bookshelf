package net.darkhax.bookshelf.features.bookshelves;

import net.darkhax.bookshelf.config.Config;
import net.darkhax.bookshelf.config.Configurable;
import net.darkhax.bookshelf.features.BookshelfFeature;
import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.item.ItemBlockBasic;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Config(name = "bookshelf")
@BookshelfFeature(name = "bookshelves", description = "Adds bookshelves of different wood types")
public class FeatureBookshelves extends Feature {

    @Configurable(category = "bookshelves", description = "Allow crafting of the wooden bookshelf variants?")
    public static boolean craftShelves = true;

    @Configurable(category = "bookshelves", description = "Put bookshelves in the oredict?")
    public static boolean useOredict = true;

    private final Block blockShelf = new BlockWoodenShelf();

    @Override
    public void onPreInit () {

        if (this.enabled) {

            GameRegistry.register(this.blockShelf);
            GameRegistry.register(new ItemBlockBasic(this.blockShelf, BlockWoodenShelf.types, true));

            if (craftShelves) {
                for (int meta = 1; meta < 6; meta++) {
                    GameRegistry.addShapedRecipe(new ItemStack(this.blockShelf, 1, meta - 1), new Object[] { "xxx", "yyy", "xxx", Character.valueOf('x'), new ItemStack(Blocks.PLANKS, 1, meta), Character.valueOf('y'), Items.BOOK });
                }
            }

            if (useOredict) {
                OreDictionary.registerOre(OreDictUtils.BOOKSHELF, new ItemStack(this.blockShelf, 1, OreDictionary.WILDCARD_VALUE));

                for (final BlockWoodenShelf.EnumType type : BlockWoodenShelf.EnumType.values()) {
                    OreDictionary.registerOre(OreDictUtils.BOOKSHELF + type.getOreName(), new ItemStack(this.blockShelf, 1, type.getMetadata()));
                }
            }
        }
    }

    @Override
    public void onClientPreInit () {

        if (this.enabled) {

            final Item item = Item.getItemFromBlock(this.blockShelf);

            for (int meta = 0; meta < 5; meta++) {
                ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("bookshelf:bookshelf_" + BlockWoodenShelf.types[meta], "inventory"));
            }
        }
    }
}