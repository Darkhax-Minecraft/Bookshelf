package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPoolSingletonContainer.class)
public interface AccessorLootPoolSingletonContainer {

    @Accessor("weight")
    int bookshelf$weight();

    @Accessor("quality")
    int bookshelf$quality();
}