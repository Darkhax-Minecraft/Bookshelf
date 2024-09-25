package net.darkhax.bookshelf.common.mixin.access.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BannerBlockEntity.class)
public interface AccessorBannerBlockEntity {

    @Accessor("name")
    void setName(Component name);
}
