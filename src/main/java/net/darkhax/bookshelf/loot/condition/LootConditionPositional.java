package net.darkhax.bookshelf.loot.condition;

import java.util.function.BiPredicate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * The base class for a loot condition that requires a world position. If no position is found
 * the test will fail.
 */
public class LootConditionPositional implements ILootCondition {
    
    /**
     * The predicate to apply at the given position.
     */
    private final BiPredicate<LootContext, BlockPos> predicate;
    
    public LootConditionPositional(BiPredicate<LootContext, BlockPos> predicate) {
        
        this.predicate = predicate;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final BlockPos pos = ctx.get(LootParameters.POSITION);
        return pos != null && this.predicate.test(ctx, pos);
    }
}