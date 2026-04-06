package net.darkhax.bookshelf.common.mixin.access.loot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DynamicLoot.class)
public interface AccessorDynamicLoot {

    @Accessor("name")
    Identifier bookshelf$name();
}