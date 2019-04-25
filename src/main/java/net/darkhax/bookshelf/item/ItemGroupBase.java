/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.function.Supplier;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupBase extends ItemGroup {

    private final Supplier<ItemStack> iconProvider; 
    
    public ItemGroupBase (String label, Supplier<ItemStack> iconProvider) {
        
        super(label);
        this.iconProvider = iconProvider;
    }

    @Override
    public ItemStack createIcon () {
        
        return this.iconProvider.get();
    }
}