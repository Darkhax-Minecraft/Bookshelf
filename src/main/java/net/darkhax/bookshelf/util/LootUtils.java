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

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
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
        
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "pools");
    }
    
    /**
     * Gets all loot entries from a loot pool.
     *
     * @param pool The loot pool to pull from.
     * @return The list of entries within the pool.
     */
    public static List<LootEntry> getEntries (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "entries");
    }
    
    /**
     * Gets all loot conditions from a loot pool.
     *
     * @param pool The loot pool to pull from.
     * @return The list of loot conditions.
     */
    public static List<ILootCondition> getConditions (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "conditions");
    }
    
    /**
     * Gets an ItemStack used with a given loot context. It first checks for the tool context.
     * If no tool is found it will check for the killer's held item. If there is no killer it
     * will check the direct killer.
     *
     * @param ctx The loot context to read from.
     * @return The stack that was found for the given context. This may be null.
     */
    @Nullable
    public static ItemStack getItemContext (LootContext ctx) {
        
        ItemStack stack = ctx.getParamOrNull(LootParameters.TOOL);
        
        // In some cases like killing an entity the tool is null rather than ItemStack.EMPTY.
        if (stack == null) {
            
            Entity killer = ctx.getParamOrNull(LootParameters.KILLER_ENTITY);
            
            if (killer == null) {
                
                killer = ctx.getParamOrNull(LootParameters.DIRECT_KILLER_ENTITY);
            }
            
            if (killer instanceof LivingEntity) {
                
                stack = ((LivingEntity) killer).getMainHandItem();
            }
        }
        
        return stack;
    }
}