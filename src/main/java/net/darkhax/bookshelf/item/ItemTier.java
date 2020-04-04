/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.function.Supplier;

import net.darkhax.bookshelf.util.RecipeUtils;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;

/**
 * A basic implementation of IItemTier.
 */
public class ItemTier implements IItemTier {
    
    private final int maxUses;
    private final float efficiency;
    private final float damage;
    private final int harvestLevel;
    private final int enchantability;
    private final Supplier<Ingredient> repairSupplier;
    private Ingredient repairIngredient;
    
    @SafeVarargs
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Tag<Item>... repairItems) {
        
        this(maxUses, efficiency, damage, harvestLevel, enchantability, () -> RecipeUtils.ingredientFromTags(repairItems));
    }
    
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Tag<Item> repairItems) {
        
        this(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.fromTag(repairItems));
    }
    
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Item... repairItems) {
        
        this(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.fromItems(repairItems));
    }
    
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, ItemStack... repairItems) {
        
        this(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.fromStacks(repairItems));
    }
    
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<Ingredient> repairItems) {
        
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.damage = damage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.repairSupplier = repairItems;
    }
    
    @Override
    public int getMaxUses () {
        
        return this.maxUses;
    }
    
    @Override
    public float getEfficiency () {
        
        return this.efficiency;
    }
    
    @Override
    public float getAttackDamage () {
        
        return this.damage;
    }
    
    @Override
    public int getHarvestLevel () {
        
        return this.harvestLevel;
    }
    
    @Override
    public int getEnchantability () {
        
        return this.enchantability;
    }
    
    @Override
    public Ingredient getRepairMaterial () {
        
        if (this.repairIngredient == null) {
            
            this.repairIngredient = this.repairSupplier.get();
        }
        
        return this.repairIngredient;
    }
}
