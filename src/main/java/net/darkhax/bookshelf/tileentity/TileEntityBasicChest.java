package net.darkhax.bookshelf.tileentity;

import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;

/**
 * This class is simply an access point to bind the custom chest renderer. It doesn't add any
 * unique functionality beyond that.
 */
public class TileEntityBasicChest extends TileEntityChest {
    
    /**
     * Base constructor, used internally by mc/forge. Don't use this!
     */
    public TileEntityBasicChest() {
        
        super();
    }
    
    /**
     * Delegate constructor for the chest one.
     * 
     * @param type The type of chest being created.
     */
    public TileEntityBasicChest(BlockChest.Type type) {
        
        super(type);
    }
}