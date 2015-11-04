package net.darkhax.bookshelf.lib.util;

import net.minecraft.entity.Entity;

public class EntityUtils {
    
    /**
     * Calculates the distance between two entities. This is done by getting the square root of
     * the entities positions.
     * 
     * @param firstEntity: The first entity to use.
     * @param secondEntity: The second entity to use.
     * @return double: The distance between the two entities passed.
     */
    public static double getDistanceBetweenEntities (Entity firstEntity, Entity secondEntity) {
        
        double distanceX = firstEntity.posX - secondEntity.posX;
        double distanceY = firstEntity.posY - secondEntity.posY;
        double distanceZ = firstEntity.posZ - secondEntity.posZ;
        
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
    }
    
    /**
     * Pushes an entity towards another one.
     * 
     * @param entityToMove: The entity that should be pushed towards the other entity.
     * @param destination: The destination entity, that the entity to move should be pushed
     *            towards.
     * @param force: The amount of force to push the entityToMove with.
     */
    public static void pushTowards (Entity entityToMove, Entity destination, double force) {
        
        double distanceX = destination.posX - entityToMove.posX;
        double distanceY = destination.posY - entityToMove.posY;
        double distanceZ = destination.posZ - entityToMove.posZ;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            
            entityToMove.motionX = distanceX / distance * force;
            entityToMove.motionY = distanceY / distance * force;
            entityToMove.motionZ = distanceZ / distance * force;
        }
    }
}