package net.darkhax.bookshelf.mixin.accessors.util.random;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.random.WeightedRandomList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandomList.class)
public interface AccessorWeightedRandomList<E> {

    @Accessor("totalWeight")
    int bookshelf$getTotalWeight();

    @Accessor("items")
    ImmutableList<E> bookshelf$getEntries();
}
