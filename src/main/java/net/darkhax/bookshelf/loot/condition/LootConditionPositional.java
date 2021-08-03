package net.darkhax.bookshelf.loot.condition;

import java.util.function.BiPredicate;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * The base class for a loot condition that requires a world position. If no position is found
 * the test will fail.
 */
public abstract class LootConditionPositional implements LootItemCondition {

    /**
     * The predicate to apply at the given position.
     */
    private final BiPredicate<LootContext, BlockPos> predicate;

    public LootConditionPositional (BiPredicate<LootContext, BlockPos> predicate) {

        this.predicate = predicate;
    }

    @Override
    public boolean test (LootContext ctx) {

        final Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);
        return pos != null && this.predicate.test(ctx, new BlockPos(pos));
    }
}