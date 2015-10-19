package net.darkhax.bookshelf.event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

@HasResult
public class MobSpawnerSpawnEvent extends EntityEvent {
    
    public MobSpawnerSpawnEvent(Entity entity) {
        
        super(entity);
    }
}