package net.darkhax.bookshelf.utils;

import net.darkhax.bookshelf.internal.mixin.item.AccessorItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

/**
 * This class contains various helper functions for working with items and their data.
 */
public final class ItemHelper {
    
    /**
     * Sets the creative screen group for an item.
     * 
     * @param item The item to modify.
     * @param group The item group to set.
     * @return The item being modified.
     */
    public static Item setItemGroup (Item item, ItemGroup group) {
        
        ((AccessorItem) item).setItemGroup(group);
        return item;
    }
    
    /**
     * Sets the rarity of an item.
     * 
     * @param item The item to modify.
     * @param rarity The new rarity for the item.
     * @return The item being modified.
     */
    public static Item setRarity (Item item, Rarity rarity) {
        
        ((AccessorItem) item).setRarity(rarity);
        return item;
    }
    
    /**
     * Sets an item as being fire proof.
     * 
     * @param item The item to modify.
     * @param fireproof Whether or not the item is fireproof.
     * @return The item being modified.
     */
    public static Item setFireproof (Item item, boolean fireproof) {
        
        ((AccessorItem) item).setFireproof(fireproof);
        return item;
    }
    
    /**
     * Sets the food data for an item.
     * 
     * @param item The item to modify.
     * @param food The new food property.
     * @return The item being modified.
     */
    public static Item setFood (Item item, FoodComponent food) {
        
        ((AccessorItem) item).setFood(food);
        return item;
    }
}