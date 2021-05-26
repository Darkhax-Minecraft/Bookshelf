/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * This class provides a decent base for implementing custom recipe objects. The default
 * interface defines many methods which are not useful or relevant to modded recipes, those
 * have been overridden with default and disabled values. The class also has some internal
 * checks to make sure the recipe type and serializer have been set up in an appropriate way.
 */
public abstract class RecipeDataBase implements IRecipe<IInventory> {
    
    private final ResourceLocation identifier;
    
    public RecipeDataBase(ResourceLocation identifier) {
        
        this.identifier = identifier;
        
        if (this.getSerializer() == null) {
            
            throw new IllegalStateException("No serializer found for " + this.getClass().getName());
        }
        
        if (this.getType() == null) {
            
            throw new IllegalStateException("No recipe type found for " + this.getClass().getName());
        }
    }
    
    @Override
    public ResourceLocation getId () {
        
        return this.identifier;
    }
    
    @Override
    public boolean matches (IInventory inv, World worldIn) {
        
        // Not used
        return false;
    }
    
    @Override
    public ItemStack assemble (IInventory inv) {
        
        // Not used
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canCraftInDimensions (int width, int height) {
        
        // Not used
        return false;
    }
    
    @Override
    public ItemStack getResultItem () {
        
        // Not used
        return ItemStack.EMPTY;
    }
}