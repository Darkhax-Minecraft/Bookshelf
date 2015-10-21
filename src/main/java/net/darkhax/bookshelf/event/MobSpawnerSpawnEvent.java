package net.darkhax.bookshelf.event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

@HasResult
public class MobSpawnerSpawnEvent extends EntityEvent {
    
    /**
     * Constructs a new MobSpawnerSpawnEvent that is fired whenever a monster spawner attempts
     * to spawn an entity. The result of this event can be updated to alter the outcome of the
     * event. Allow will allow the event to continue, default will set the default vanilla
     * logic, and deny will prevent the mob from spawning. You do not need to set a result in
     * order for this event to work.
     * 
     * @param entity: Instance of the entity being spawned by the spawner.
     */
    public MobSpawnerSpawnEvent(Entity entity) {
        
        super(entity);
    }
}