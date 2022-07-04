package net.darkhax.bookshelf.mixin.block.entity;

import net.darkhax.bookshelf.api.block.entity.WorldlyInventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseContainerBlockEntity.class)
public abstract class MixinBaseContainerBlockEntity extends BlockEntity {

    private MixinBaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
    }

    /**
     * Our loader neutral inventory system does to have the capability to override this method directly, and the attach
     * capabilities event is not fired by Forge's method implementation. This mixin simply skips Forges implementation
     * if the block is using our system.
     */
    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true, remap = false)
    public <T> void getCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> callback) {

        final BlockEntity self = this;

        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && self instanceof WorldlyInventoryBlockEntity) {

            callback.setReturnValue(super.getCapability(cap, side));
        }
    }
}
