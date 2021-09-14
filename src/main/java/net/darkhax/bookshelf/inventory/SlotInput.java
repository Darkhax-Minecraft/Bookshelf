/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.inventory;

import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * This class provides a basic Slot with an optional predicate for item validation, and an
 * optional listener for when an item is inputed.
 */
public class SlotInput extends Slot {
    
    /**
     * A predicate used to determine what can be placed in the slot.
     */
    private final Predicate<ItemStack> inputValidator;
    
    /**
     * A listener for when an item is placed into the slot.
     */
    @Nullable
    private final Consumer<ItemStack> inputListener;
    
    public SlotInput(IInventory inventory, int index, int xPosition, int yPosition, @Nullable Predicate<ItemStack> inputValidator, Consumer<ItemStack> inputListener) {
        
        super(inventory, index, xPosition, yPosition);
        this.inputValidator = inputValidator;
        this.inputListener = inputListener;
    }
    
    @Override
    public boolean mayPlace (ItemStack stack) {
        
        return this.inputValidator.test(stack);
    }
    
    @Override
    public void set (ItemStack stack) {
        
        if (this.inputListener != null) {
            
            this.inputListener.accept(stack);
        }
        
        super.set(stack);
    }
}