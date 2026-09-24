package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UniformContainerBase.class)
public interface AccessorLootPoolSingletonContainer {

    @Accessor("weight")
    int bookshelf$weight();

    @Accessor("quality")
    int bookshelf$quality();
}