/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * A basic creative tab class that can be used to quickly create a new creative tab for your
 * mod.
 */
public class ItemGroupBase extends CreativeModeTab {

    /**
     * A supplier for the icon of the creative tab.
     */
    private final Supplier<ItemStack> iconSupplier;

    public ItemGroupBase (String label, Block block) {

        this(label, Item.byBlock(block));
    }

    public ItemGroupBase (String label, Item item) {

        this(label, () -> new ItemStack(item));
    }

    /**
     * The base constructor for creating a new ItemGroup.
     *
     * @param label The translation string used for the name of the group.
     * @param iconSupplier A supplier for the icon stack.
     */
    public ItemGroupBase (String label, Supplier<ItemStack> iconSupplier) {

        super(label);
        this.iconSupplier = iconSupplier;
    }

    @Override
    public ItemStack makeIcon () {

        return this.iconSupplier.get();
    }
}