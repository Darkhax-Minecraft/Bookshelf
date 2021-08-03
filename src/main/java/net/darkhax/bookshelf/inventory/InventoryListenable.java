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

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;

/**
 * This class provides a simple inventory that can have a listener called by
 * {@link #setChanged()}. This inventory is intended for internal systems like a crafting GUI.
 * It should not be used for things like TileEntity inventory storage. Use the capability
 * system instead!
 */
public class InventoryListenable extends SimpleContainer {

    /**
     * The listener. This may be null.
     */
    @Nullable
    private Consumer<Container> listener;

    public InventoryListenable (int size) {

        this(size, null);
    }

    public InventoryListenable (int size, Consumer<Container> listener) {

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
    public void setInventoryListener (@Nullable Consumer<Container> listener) {

        this.listener = listener;
    }
}