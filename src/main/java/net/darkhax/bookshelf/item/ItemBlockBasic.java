/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * A basic implementation of ItemBlock that has support for multiple meta varients.
 */
public class ItemBlockBasic extends ItemBlock {

    /**
     * An array of names which represents the variants held by the Item.
     */
    public final String[] names;

    /**
     * Constructs the basic ItemBlock without automatically setting the registry name.
     *
     * @param block The Block to represent.
     * @param names The variants for the item.
     */
    public ItemBlockBasic (Block block, String[] names) {

        this(block, names, false);
    }

    /**
     * Constructs a basic ItemBlock to represent a block.
     *
     * @param block The Block to represent.
     * @param names The variants for the item.
     * @param selfRegister Whether or not the ItemBlock should use the same registry name as
     *        the passed block.
     */
    public ItemBlockBasic (Block block, String[] names, boolean selfRegister) {

        super(block);
        this.names = names;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        if (selfRegister) {
            this.setRegistryName(block.getRegistryName());
        }
    }

    @Override
    public int getMetadata (int damage) {

        return damage;
    }

    @Override
    public String getUnlocalizedName (ItemStack stack) {

        if (stack.getMetadata() > this.names.length)
            return super.getUnlocalizedName() + "." + this.names[0];

        return super.getUnlocalizedName() + "." + this.names[stack.getMetadata()];
    }

    @Override
    public Block getBlock () {

        return this.block;
    }
}