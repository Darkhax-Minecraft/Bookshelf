/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.darkhax.bookshelf.registry.IVariant;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A base class which handles making items with sub types.
 */
public class ItemSubType extends Item implements IVariant {

    private final String[] variants;

    /**
     * Base constructor for this class.
     */
    public ItemSubType (String... variants) {

        this.variants = variants;
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int damage) {

        return damage;
    }

    @Override
    public String getTranslationKey (ItemStack stack) {

        final int meta = stack.getMetadata();
        final String[] variants = this.getVariant();
        return (super.getTranslationKey() + "." + this.getPrefix() + (!(meta >= 0 && meta < variants.length) ? variants[0] : variants[meta])).replace("_", ".");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (CreativeTabs tab, NonNullList<ItemStack> subItems) {

        if (this.isInCreativeTab(tab)) {

            for (int meta = 0; meta < this.getVariant().length; meta++) {

                subItems.add(new ItemStack(this, 1, meta));
            }
        }
    }

    @Override
    public String[] getVariant () {

        return this.variants;
    }
}