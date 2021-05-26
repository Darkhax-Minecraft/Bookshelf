/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.inventory;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;

/**
 * This class provides a simple inventory that can have a listener called by
 * {@link #markDirty()}. This inventory is intended for internal systems like a crafting GUI.
 * It should not be used for things like TileEntity inventory storage. Use the capability
 * system instead!
 */
public class InventoryListenable extends Inventory {
    
    /**
     * The listener. This may be null.
     */
    @Nullable
    private Consumer<IInventory> listener;
    
    public InventoryListenable(int size) {
        
        this(size, null);
    }
    
    public InventoryListenable(int size, Consumer<IInventory> listener) {
        
        super(size);
        this.listener = listener;
    }
    
    @Override
    public void setChanged () {
        
        super.setChanged();
        
        if (this.listener != null) {
            
            this.listener.accept(this);
        }
    }
    
    /**
     * Sets the inventory listener.
     * 
     * @param listener The new listener. Null can be used to remove the existing listener.
     */
    public void setInventoryListener (@Nullable Consumer<IInventory> listener) {
        
        this.listener = listener;
    }
}