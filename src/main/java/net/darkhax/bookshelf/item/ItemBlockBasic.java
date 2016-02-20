package net.darkhax.bookshelf.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasic extends ItemBlock {
    
    public final Block theBlock;
    public final String[] names;
    
    public ItemBlockBasic(Block block, String[] names) {
        
        super(block);
        this.theBlock = block;
        this.names = names;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata (int damage) {
        
        return damage;
    }
    
    @Override
    public String getUnlocalizedName (ItemStack stack) {
        
        if (stack.getMetadata() > names.length)
            return super.getUnlocalizedName() + "." + names[0];
            
        return super.getUnlocalizedName() + "." + names[stack.getMetadata()];
    }
}