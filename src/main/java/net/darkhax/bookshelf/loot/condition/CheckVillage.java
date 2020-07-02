package net.darkhax.bookshelf.loot.condition;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.loot.LootContext;

/**
 * A loot condition that checks for a village.
 */
public class CheckVillage extends LootConditionPositional {
    
    /**
     * A singleton instance for the condition.
     */
    public static final CheckVillage INSTANCE = new CheckVillage();
    
    /**
     * A serializer for the condition.
     */
    public static final SerializerSingleton<CheckVillage> SERIALIZER = new SerializerSingleton<>(Bookshelf.MOD_ID, "check_village", CheckVillage.class, INSTANCE);
    
    private CheckVillage() {
        
        super(CheckVillage::test);
    }
    
    private static boolean test (LootContext ctx, BlockPos pos) {
        
        return ctx.getWorld().isVillage(pos);
    }

    @Override
    public LootConditionType func_230419_b_() {
        return SERIALIZER.lootConditionType;
    }
}