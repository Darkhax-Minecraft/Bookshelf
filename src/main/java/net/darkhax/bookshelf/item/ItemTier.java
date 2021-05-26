/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.darkhax.bookshelf.util.RecipeUtils;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.util.Lazy;

/**
 * A basic implementation of IItemTier.
 */
public class ItemTier implements IItemTier {
    
    /**
     * The maximum amount of durability.
     */
    private final int maxUses;
    
    /**
     * The block break efficiency.
     */
    private final float efficiency;
    
    /**
     * The base damage value.
     */
    private final float damage;
    
    /**
     * The block harvesting level.
     */
    private final int harvestLevel;
    
    /**
     * The material enchantability.
     */
    private final int enchantability;
    
    /**
     * A supplier used to check if an item can repair other items of this tier.
     */
    private final Lazy<Ingredient> repairSupplier;
    
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static ItemTier createFromTags (int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<Tag<Item>>... repairItems) {
        
        return new ItemTier(maxUses, efficiency, damage, harvestLevel, enchantability, () -> RecipeUtils.ingredientFromTags(Stream.of(repairItems).map(Supplier::get).toArray(Tag[]::new)));
    }
    
    public static ItemTier createFromTag (int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<Tag<Item>> repairItems) {
        
        return new ItemTier(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.of(repairItems.get()));
    }
    
    @SafeVarargs
    public static ItemTier createFromItem (int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<Item>... repairItems) {
        
        return new ItemTier(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.of(Stream.of(repairItems).map(Supplier::get).toArray(Item[]::new)));
    }
    
    @SafeVarargs
    public static ItemTier createFromStack (int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<ItemStack>... repairItems) {
        
        return new ItemTier(maxUses, efficiency, damage, harvestLevel, enchantability, () -> Ingredient.of(Stream.of(repairItems).map(Supplier::get).toArray(ItemStack[]::new)));
    }
    
    public ItemTier(int maxUses, float efficiency, float damage, int harvestLevel, int enchantability, Supplier<Ingredient> repairItems) {
        
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.damage = damage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.repairSupplier = Lazy.of(repairItems);
    }
    
    @Override
    public int getUses () {
        
        return this.maxUses;
    }
    
    @Override
    public float getSpeed () {
        
        return this.efficiency;
    }
    
    @Override
    public float getAttackDamageBonus () {
        
        return this.damage;
    }
    
    @Override
    public int getLevel () {
        
        return this.harvestLevel;
    }
    
    @Override
    public int getEnchantmentValue () {
        
        return this.enchantability;
    }
    
    @Override
    public Ingredient getRepairIngredient () {
        
        return this.repairSupplier.get();
    }
}
