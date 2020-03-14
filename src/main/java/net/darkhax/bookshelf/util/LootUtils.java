package net.darkhax.bookshelf.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class LootUtils {
    
    public static Map<String, LootPool> mapPools (List<LootPool> pools) {
        
        return ImmutableMap.copyOf(pools.stream().collect(Collectors.toMap(p -> p.getName(), p -> p)));
        
    }
    
    public static List<LootPool> getPools (LootTable table) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
    }
    
    public static List<LootEntry> getEntries (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
    }
    
    public static List<LootEntry> getConditions (LootPool pool) {
        
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186454_b");
    }
}