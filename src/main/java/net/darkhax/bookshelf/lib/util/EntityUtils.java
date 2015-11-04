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
}