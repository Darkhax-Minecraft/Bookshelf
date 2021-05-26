/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.inventory;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * This slot provides a slot with optional validator for inputs, and a listener for when the
 * item is taken from the slot.
 */
public class SlotOutput extends Slot {
    
    /**
     * A predicate that validates what items can be placed in this slot.
     */
    private final Predicate<ItemStack> inputValidator;
    
    /**
     * A listener that is called when an item is removed from the slot.
     */
    private final BiFunction<PlayerEntity, ItemStack, ItemStack> takeListener;
    
    public SlotOutput(IInventory inventory, int index, int xPosition, int yPosition, BiFunction<PlayerEntity, ItemStack, ItemStack> takeListener) {
        
        this(inventory, index, xPosition, yPosition, stack -> false, takeListener);
    }
    
    public SlotOutput(IInventory inventory, int index, int xPosition, int yPosition, Predicate<ItemStack> inputValidator, BiFunction<PlayerEntity, ItemStack, ItemStack> takeListener) {
        
        super(inventory, index, xPosition, yPosition);
        this.inputValidator = inputValidator;
        this.takeListener = takeListener;
    }
    
    @Override
    public boolean mayPlace (ItemStack stack) {
        
        return this.inputValidator.test(stack);
    }
    
    @Override
    public ItemStack onTake (PlayerEntity player, ItemStack stack) {
        
        final ItemStack stackToTake = this.takeListener.apply(player, stack);
        this.setChanged();
        return stackToTake;
    }
}