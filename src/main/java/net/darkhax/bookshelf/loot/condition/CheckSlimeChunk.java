package net.darkhax.bookshelf.loot.condition;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.WorldUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;

/**
 * A loot condition that checks for a slime chunk.
 */
public class CheckSlimeChunk extends LootConditionPositional {
    
    /**
     * A singleton instance for the condition.
     */
    public static final CheckSlimeChunk INSTANCE = new CheckSlimeChunk();
    
    /**
     * A serializer for the condition.
     */
    public static final AbstractSerializer<CheckSlimeChunk> SERIALIZER = new SerializerSingleton<>(Bookshelf.MOD_ID, "slime_chunk", CheckSlimeChunk.class, INSTANCE);
    
    private CheckSlimeChunk() {
        
        super(CheckSlimeChunk::test);
    }
    
    private static boolean test (LootContext ctx, BlockPos pos) {
        
        return WorldUtils.isSlimeChunk(ctx.getWorld(), pos);
    }
}