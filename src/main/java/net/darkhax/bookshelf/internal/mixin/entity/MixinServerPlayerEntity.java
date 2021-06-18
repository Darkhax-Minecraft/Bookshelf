package net.darkhax.bookshelf.internal.mixin.entity;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.darkhax.bookshelf.internal.entity.IBookshelfEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {
    
    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom (ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callback) {
        
        if (oldPlayer != null) {
            
            final IBookshelfEntity old = IBookshelfEntity.from(oldPlayer);
            final Map<Identifier, CompoundTag> oldData = old.bookshelf$getPersistentData();
            
            if (oldData != null && !oldData.isEmpty()) {
                
                final IBookshelfEntity current = IBookshelfEntity.from(this);
                current.bookshelf$setPersistentData(old.bookshelf$getPersistentData());
            }
        }
    }
}