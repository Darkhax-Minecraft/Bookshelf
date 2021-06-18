package net.darkhax.bookshelf.internal.mixin.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.darkhax.bookshelf.data.NBT;
import net.darkhax.bookshelf.internal.entity.IBookshelfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class MixinEntity implements IBookshelfEntity {
    
    @Shadow
    private World world;
    
    @Unique
    private Map<Identifier, CompoundTag> persistentData = new HashMap<>();
    
    @Unique
    @Override
    public Map<Identifier, CompoundTag> bookshelf$getPersistentData () {
        
        if (this.world.isClient) {
            
            throw new IllegalStateException("Persistent data can not be accessed on the client.");
        }
        
        return this.persistentData;
    }
    
    @Unique
    @Override
    public void bookshelf$setPersistentData (Map<Identifier, CompoundTag> data) {
        
        if (this.world.isClient) {
            
            throw new IllegalStateException("Persistent data can not be accessed on the client.");
        }
        
        this.persistentData = data;
    }
    
    @Inject(method = "toTag", at = @At("HEAD"))
    public void toTag (CompoundTag tag, CallbackInfoReturnable<CompoundTag> callback) {
        
        if (!this.world.isClient && this.persistentData != null && !this.persistentData.isEmpty()) {
            
            final CompoundTag data = new CompoundTag();
            
            for (final Entry<Identifier, CompoundTag> entry : this.persistentData.entrySet()) {
                
                data.put(entry.getKey().toString(), entry.getValue());
            }
            
            tag.put(IBookshelfEntity.PERSISTENT_TAG, data);
        }
    }
    
    @Inject(method = "fromTag", at = @At("HEAD"))
    public void fromTag (CompoundTag tag, CallbackInfo callback) {
        
        if (!this.world.isClient && tag.contains(IBookshelfEntity.PERSISTENT_TAG, NBT.COMPOUND)) {
            
            this.persistentData = new HashMap<>();
            final CompoundTag data = tag.getCompound(IBookshelfEntity.PERSISTENT_TAG);
            
            for (final String key : data.getKeys()) {
                
                final Identifier id = Identifier.tryParse(key);
                
                if (id != null) {
                    
                    this.persistentData.put(id, data.getCompound(key));
                }
            }
        }
    }
}