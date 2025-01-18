package net.darkhax.bookshelf.common.mixin.access.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorCropBlock {

    @Invoker("getBaseSeedId")
    ItemLike bookshelf$getSeed();
}