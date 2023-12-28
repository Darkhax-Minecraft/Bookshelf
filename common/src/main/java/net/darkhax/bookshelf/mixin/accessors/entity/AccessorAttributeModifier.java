package net.darkhax.bookshelf.mixin.accessors.entity;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeModifier.class)
public interface AccessorAttributeModifier {

    @Accessor("name")
    String bookshelf$getName();
}
