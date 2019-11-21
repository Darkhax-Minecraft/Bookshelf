/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.block.tileentity;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityBasicTickable extends TileEntityBasic implements ITickableTileEntity {
    
    public TileEntityBasicTickable(TileEntityType<?> tileEntityType) {
        
        super(tileEntityType);
    }
    
    @Override
    public void tick () {
        
        if (this.hasWorld() && this.isLoaded() && !this.isRemoved()) {
            try {
                
                this.onTileTick();
            }
            
            catch (final Exception exception) {
                
                Bookshelf.LOG.warn("A TileEntity with ID {} at {} in world {} failed a client update tick!", this.getType().getRegistryName(), this.getPos(), this.getWorld().getWorldInfo().getWorldName());
                Bookshelf.LOG.catching(exception);
            }
        }
    }
    
    /**
     * Handles the TileEntity update ticks. This method will only be called in a safe
     * environment.
     */
    public void onTileTick () {
        
    }
}