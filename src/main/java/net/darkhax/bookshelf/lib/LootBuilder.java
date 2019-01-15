/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * This serves as a builder for {@link net.minecraft.world.storage.loot.LootEntryItem}. It
 * allows for cleaner building of loot entries, and for things to be done in a flexible way.
 */
public class LootBuilder {
    
    /**
     * The name of the loot entry. This can not be changed once set.
     */
    private final String name;
    
    /**
     * The name of the pool this entry targets.
     */
    private String pool;
    
    /**
     * The weight of the entry.
     */
    private int weight;
    
    /**
     * The quality of the entry. Affects how looting impacts the entry's weight.
     */
    private int quality;
    
    /**
     * The item to generate with the entry.
     */
    private Item item;
    
    /**
     * The conditions for the loot entry.
     */
    private final List<LootCondition> conditions;
    
    /**
     * The functions for the loot entry.
     */
    private final List<LootFunction> functions;
    
    public LootBuilder(String name, String pool, int weight, Item item) {
        
        this(name, pool, weight, 0, item, new ArrayList<LootCondition>(), new ArrayList<LootFunction>());
    }
    
    public LootBuilder(String name, String pool, int weight, int quality, Item item, List<LootCondition> conditions, List<LootFunction> functions) {
        
        this.name = name;
        this.pool = pool;
        this.weight = weight;
        this.quality = quality;
        this.item = item;
        this.conditions = conditions;
        this.functions = functions;
    }
    
    /**
     * Gets the name of the loot entry.
     *
     * @return The name of the loot entry.
     */
    public String getName () {
        
        return this.name;
    }
    
    /**
     * Gets the name of the target pool.
     *
     * @return The name of the target pool.
     */
    public String getPool () {
        
        return this.pool;
    }
    
    /**
     * Sets the name of the target pool.
     *
     * @param pool The name of the target pool.
     * @return The LootBuilder object.
     */
    public LootBuilder setPool (String pool) {
        
        this.pool = pool;
        return this;
    }
    
    /**
     * Gets the weight of the entry.
     *
     * @return The weight of the entry.
     */
    public int getWeight () {
        
        return this.weight;
    }
    
    /**
     * Sets the weight of the entry.
     *
     * @param weight The weight of the entry.
     * @return The LootBuilder object.
     */
    public LootBuilder setWeight (int weight) {
        
        this.weight = weight;
        return this;
    }
    
    /**
     * Gets the quality of the entry.
     *
     * @return The quality of the entry.
     */
    public int getQuality () {
        
        return this.quality;
    }
    
    /**
     * Sets the quality of the entry.
     *
     * @param quality The quality of the entry.
     * @return The LootBuilder object.
     */
    public LootBuilder setQuality (int quality) {
        
        this.quality = quality;
        return this;
    }
    
    /**
     * Gets the item for the entry.
     *
     * @return The item for the entry.
     */
    public Item getItem () {
        
        return this.item;
    }
    
    /**
     * Sets the item for the entry.
     *
     * @param item The item for the entry.
     * @return The LootBuilder object.
     */
    public LootBuilder setItem (Item item) {
        
        this.item = item;
        return this;
    }
    
    /**
     * Gets a List of conditions for the entry.
     *
     * @return A List of conditions for the entry.
     */
    public List<LootCondition> getConditions () {
        
        return this.conditions;
    }
    
    /**
     * Adds a condition to the entry.
     *
     * @param conditon The condition to add.
     * @return The LootBuilder object.
     */
    public LootBuilder addCondition (LootCondition conditon) {
        
        this.conditions.add(conditon);
        return this;
    }
    
    /**
     * Gets a List of functions for the entry.
     *
     * @return A List of functions for the entry.
     */
    public List<LootFunction> getFunctions () {
        
        return this.functions;
    }
    
    /**
     * Adds a function to the entry.
     *
     * @param function The function to add.
     * @return The LootBuilder object.
     */
    public LootBuilder addFunction (LootFunction function) {
        
        this.functions.add(function);
        return this;
    }
    
    /**
     * Builds the LootEntryItem so it can be used with vanilla MC.
     *
     * @return The LootEntryItem that was built.
     */
    public LootEntryItem build () {
        
        return new LootEntryItem(this.item, this.weight, this.quality, this.functions.toArray(new LootFunction[0]), this.conditions.toArray(new LootCondition[0]), this.name);
    }
    
    @Override
    public String toString () {
        
        return String.format("Name: %s - Pool: %s - Weight: %d - Quality: %d Item: %s", this.name, this.pool, this.weight, this.quality, this.item.getRegistryName());
    }
}