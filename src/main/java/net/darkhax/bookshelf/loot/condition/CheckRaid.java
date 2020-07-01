package net.darkhax.bookshelf.loot.condition;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.loot.LootContext;

/**
 * A loot condition that checks for an active raid.
 */
public class CheckRaid extends LootConditionPositional {
    
    /**
     * A singleton instance for the condition.
     */
    public static final CheckRaid INSTANCE = new CheckRaid();
    
    /**
     * A serializer for the condition.
     */
    public static final AbstractSerializer<CheckRaid> SERIALIZER = new SerializerSingleton<>(Bookshelf.MOD_ID, "check_raid", CheckRaid.class, INSTANCE);
    
    private CheckRaid() {
        
        super(CheckRaid::test);
    }
    
    private static boolean test (LootContext ctx, BlockPos pos) {
        
        return ctx.getWorld().hasRaid(pos);
    }
}