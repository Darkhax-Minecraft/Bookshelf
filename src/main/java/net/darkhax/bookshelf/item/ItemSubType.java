/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A base class which handles making items with sub types.
 */
public abstract class ItemSubType extends Item implements IVariant {

    /**
     * Base constructor for this class.
     */
    public ItemSubType () {

        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int damage) {

        return damage;
    }

    @Override
    public String getUnlocalizedName (ItemStack stack) {

        final int meta = stack.getMetadata();
        final String[] variants = this.getVariant();
        return (super.getUnlocalizedName() + "." + this.getPrefix() + (!(meta >= 0 && meta < variants.length) ? variants[0] : variants[meta])).replace("_", ".");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {

        for (int meta = 0; meta < this.getVariant().length; meta++) {
            subItems.add(new ItemStack(this, 1, meta));
        }
    }
}