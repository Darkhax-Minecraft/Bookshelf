/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class LootUtils {
    
    /**
     * Maps a list of loot pools using their names.
     * 
     * @param pools The pools to map.
     * @return An immutable map of loot pools.
     */
    public static Map<String, LootPool> mapPools (List<LootPool> pools) {
        
        return ImmutableMap.copyOf(pools.stream().collect(Collectors.toMap(LootPool::getName, p -> p)));
    }
    
    /**
     * Gets all loot pools within a table.
     * 
     * @param table The table to pull from.
     * @return The list of pools within a table.
     */
    public static List<LootPool> getPools (LootTable table) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
    }
    
    /**
     * Gets all loot entries from a loot pool.
     * 
     * @param pool The loot pool to pull from.
     * @return The list of entries within the pool.
     */
    public static List<LootEntry> getEntries (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
    }
    
    /**
     * Gets all loot conditions from a loot pool.
     * 
     * @param pool The loot pool to pull from.
     * @return The list of loot conditions.
     */
    public static List<ILootCondition> getConditions (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186454_b");
    }
}