package net.darkhax.bookshelf.lib.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class EntityUtils {
    
    /**
     * Calculates the distance between two entities.
     * 
     * @param firstEntity: The first entity to use.
     * @param secondEntity: The second entity to use.
     * @return double: The distance between the two entities passed.
     */
    public static double getDistanceFromEntity (Entity firstEntity, Entity secondEntity) {
        
        return MathsUtils.getDistanceBetweenPoints(firstEntity.getPositionVector(), secondEntity.getPositionVector());
    }
    
    /**
     * Calculates the distance between an entity and a BlockPos.
     * 
     * @param entity: The Entity to use for the first position.
     * @param pos: The BlockPos to use for the second position.
     * @return double: The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos (Entity entity, BlockPos pos) {
        
        return MathsUtils.getDistanceBetweenPoints(entity.getPositionVector(), new Vec3d(pos));
    }
    
    /**
     * Pushes an entity towards a specific direction.
     * 
     * @param entityToMove: The entity that you want to push.
     * @param direction: The direction to push the entity.
     * @param force: The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, EnumFacing direction, double force) {
        
        pushTowards(entityToMove, entityToMove.getPosition().offset(direction.getOpposite(), 1), force);
    }
    
    /**
     * Pushes an Entity towards a BlockPos.
     * 
     * @param entityToMove: The entity that you want to push.
     * @param pos: The BlockPos to push the entity towards.
     * @param force: The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, BlockPos pos, double force) {
        
        BlockPos entityPos = entityToMove.getPosition();
        double distanceX = pos.getX() - entityPos.getX();
        double distanceY = pos.getY() - entityPos.getY();
        double distanceZ = pos.getZ() - entityPos.getZ();
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            
            entityToMove.motionX = distanceX / distance * force;
            entityToMove.motionY = distanceY / distance * force;
            entityToMove.motionZ = distanceZ / distance * force;
        }
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
    
    /**
     * Checks if two entities are close enough together.
     * 
     * @param firstEntity: The first entity to check.
     * @param secondEntity: The second entity to check.
     * @param maxDistance: The maximum distance that the entities can be apart.
     * @return boolean: True if the distance between the entities are within range of the
     *         maxDistance.
     */
    public static boolean areEntitiesCloseEnough (Entity firstEntity, Entity secondEntity, double maxDistance) {
        
        return getDistanceFromEntity(firstEntity, secondEntity) < (maxDistance * maxDistance);
    }
    
    /**
     * Gets a List of entities that are within the provided area.
     * 
     * @param entityClass: The type of entity you are looking for.
     * @param world: The world to search in.
     * @param pos: The position to start the search around.
     * @param range: The range of the search.
     * @return List<Entity>: A List containing all entities of the specified type that are
     *         within the range.
     */
    public static <T> List<T> getEntitiesInArea (Class<? extends Entity> entityClass, World world, BlockPos pos, int range) {
        
        return (List<T>) world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
    }
}