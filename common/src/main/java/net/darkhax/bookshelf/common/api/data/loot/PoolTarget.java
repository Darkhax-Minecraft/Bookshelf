package net.darkhax.bookshelf.common.api.data.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Represents a specific loot pool target within a loot table. This class also provides predefined constants for some of
 * the commonly modified loot pools.
 *
 * @param table The id of the loot table to target.
 * @param index The index of the pool within the loot table. This is usually based on the order pools appear in the JSON
 *              data.
 * @param hash  A hash of the pools JSON data. This can be obtained using the bookshelf debug command in a development
 *              environment.
 */
public record PoolTarget(ResourceLocation table, int index, int hash) {

    public static final PoolTarget MINESHAFT_RARE = of(BuiltInLootTables.ABANDONED_MINESHAFT, 0, 1537257923);
    public static final PoolTarget MINESHAFT_UNCOMMON = of(BuiltInLootTables.ABANDONED_MINESHAFT, 1, -444048389);
    public static final PoolTarget MINESHAFT_COMMON = of(BuiltInLootTables.ABANDONED_MINESHAFT, 2, 634581377);

    public static final PoolTarget SIMPLE_DUNGEON_RARE = of(BuiltInLootTables.SIMPLE_DUNGEON, 0, -66091299);
    public static final PoolTarget SIMPLE_DUNGEON_UNCOMMON = of(BuiltInLootTables.SIMPLE_DUNGEON, 1, 1870100239);
    public static final PoolTarget SIMPLE_DUNGEON_COMMON = of(BuiltInLootTables.SIMPLE_DUNGEON, 2, 2004993944);

    public static final PoolTarget CAT_GIFT = of(BuiltInLootTables.CAT_MORNING_GIFT, 0, 234355958);

    public static final PoolTarget FISHING = of(BuiltInLootTables.FISHING, 0, 1127209674);
    public static final PoolTarget FISHING_FISH = of(BuiltInLootTables.FISHING_FISH, 0, -190358337);
    public static final PoolTarget FISHING_JUNK = of(BuiltInLootTables.FISHING_JUNK, 0, 1154453499);
    public static final PoolTarget FISHING_TREASURE = of(BuiltInLootTables.FISHING_TREASURE, 0, 1729324233);

    public static final PoolTarget PIGLIN_BARTERING = of(BuiltInLootTables.PIGLIN_BARTERING, 0, 718156885);

    public static final PoolTarget SNIFFER_DIGGING = of(BuiltInLootTables.SNIFFER_DIGGING, 0, 1185470198);

    public static final PoolTarget ARCHAEOLOGY_PYRAMID = of(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, 0, -1867551069);
    public static final PoolTarget ARCHAEOLOGY_DESERT_WELL = of(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, 0, -1508422416);
    public static final PoolTarget ARCHAEOLOGY_OCEAN_RUIN_COLD = of(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, 0, -1117683719);
    public static final PoolTarget ARCHAEOLOGY_OCEAN_RUIN_WARM = of(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, 0, 153317912);
    public static final PoolTarget ARCHAEOLOGY_TRAIL_RUINS_COMMON = of(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 0, 300798809);
    public static final PoolTarget ARCHAEOLOGY_TRAIL_RUINS_RARE = of(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 0, 1848809003);

    public static PoolTarget of(ResourceKey<LootTable> table, int index, int hash) {
        return new PoolTarget(table.location(), index, hash);
    }
}