/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public final class EntityUtils {
    
    /**
     * A cache of spawn egg colors mapped to the entity type. Populated by
     * {@link #getEggColors(EntityType)}.
     */
    private static Map<EntityType<?>, Tuple<Integer, Integer>> eggColorCache = new HashMap<>();
    
    /**
     * Calculates the distance between two entities.
     *
     * @param firstEntity The first entity to use.
     * @param secondEntity The second entity to use.
     * @return double The distance between the two entities passed.
     */
    public static double getDistanceFromEntity (Entity firstEntity, Entity secondEntity) {
        
        return MathsUtils.getDistanceBetweenPoints(firstEntity.position(), secondEntity.position());
    }
    
    /**
     * Calculates the distance between an entity and a BlockPos.
     *
     * @param entity The Entity to use for the first position.
     * @param pos The BlockPos to use for the second position.
     * @return double The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos (Entity entity, BlockPos pos) {
        
        return MathsUtils.getDistanceBetweenPoints(entity.position(), Vector3d.atCenterOf(pos));
    }
    
    /**
     * Pushes an entity towards a specific direction.
     *
     * @param entityToMove The entity that you want to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, Direction direction, double force) {
        
        pushTowards(entityToMove, entityToMove.blockPosition().relative(direction.getOpposite(), 1), force);
    }
    
    /**
     * Pushes an Entity towards a BlockPos.
     *
     * @param entityToMove The entity that you want to push.
     * @param pos The BlockPos to push the entity towards.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, BlockPos pos, double force) {
        
        final BlockPos entityPos = entityToMove.blockPosition();
        final double distanceX = (double) pos.getX() - entityPos.getX();
        final double distanceY = (double) pos.getY() - entityPos.getY();
        final double distanceZ = (double) pos.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }
    
    /**
     * Pushes an entity towards another one.
     *
     * @param entityToMove The entity that should be pushed towards the other entity.
     * @param destination The destination entity, that the entity to move should be pushed
     *        towards.
     * @param force The amount of force to push the entityToMove with.
     */
    public static void pushTowards (Entity entityToMove, Entity destination, double force) {
        
        final double distanceX = destination.getX() - entityToMove.getX();
        final double distanceY = destination.getY() - entityToMove.getY();
        final double distanceZ = destination.getZ() - entityToMove.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }
    
    /**
     * Creates a Vector3d that represents the additional motion that would be needed to push an
     * entity towards a destination.
     *
     * @param entityToMove The entity to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to use.
     * 
     */
    public static void pushTowardsDirection (Entity entityToMove, Direction direction, double force) {
        
        final BlockPos entityPos = entityToMove.blockPosition();
        final BlockPos destination = entityToMove.blockPosition().relative(direction.getOpposite(), 1);
        
        final double distanceX = (double) destination.getX() - entityPos.getX();
        final double distanceY = (double) destination.getY() - entityPos.getY();
        final double distanceZ = (double) destination.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }
    
    /**
     * Checks if two entities are close enough together.
     *
     * @param firstEntity The first entity to check.
     * @param secondEntity The second entity to check.
     * @param maxDistance The maximum distance that the entities can be apart.
     * @return boolean True if the distance between the entities are within range of the
     *         maxDistance.
     */
    public static boolean areEntitiesCloseEnough (Entity firstEntity, Entity secondEntity, double maxDistance) {
        
        return getDistanceFromEntity(firstEntity, secondEntity) < maxDistance * maxDistance;
    }
    
    /**
     * Gets a List of entities that are within the provided area.
     *
     * @param <T> The type of entities to look for.
     * @param entityClass The type of entity you are looking for.
     * @param world The world to search in.
     * @param pos The position to start the search around.
     * @param range The range of the search.
     * @return A List containing all entities of the specified type that are within the range.
     */
    public static <T extends Entity> List<T> getEntitiesInArea (Class<T> entityClass, World world, BlockPos pos, int range) {
        
        return getEntitiesInArea(entityClass, world, pos, (float) range);
    }
    
    /**
     * Gets a List of entities that are within the provided area.
     *
     * @param <T> The type of entities to look for.
     * @param entityClass The type of entity you are looking for.
     * @param world The world to search in.
     * @param pos The position to start the search around.
     * @param range The range of the search.
     * @return A List containing all entities of the specified type that are within the range.
     */
    public static <T extends Entity> List<T> getEntitiesInArea (Class<T> entityClass, World world, BlockPos pos, float range) {
        
        return world.getEntitiesOfClass(entityClass, new AxisAlignedBB(pos.offset(-range, -range, -range), pos.offset(range + 1, range + 1, range + 1)));
    }
    
    /**
     * A check to see if an entity is wearing a full suit of the armor. This check is based on
     * the class names of armor.
     *
     * @param living: The living entity to check the armor of.
     * @param armorClass: The class of the armor to check against.
     * @return boolean: True if every piece of armor the entity is wearing are the same class
     *         as the provied armor class.
     */
    public static boolean isWearingFullSet (MobEntity living, Class<Item> armorClass) {
        
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType().equals(EquipmentSlotType.Group.ARMOR)) {
                
                final ItemStack armor = living.getItemBySlot(slot);
                
                if (armor.isEmpty() || !armor.getItem().getClass().equals(armorClass)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Performs a ray trace for the look vector of an entity.
     * 
     * @param entity The entity to perform a ray trace on.
     * @param length The distance to cast the rays.
     * @param blockMode The mode used when detecting blocks.
     * @param fluidMode The mode used when detecting fluids.
     * @return An object containing the results of the ray trace.
     */
    public static RayTraceResult rayTrace (LivingEntity entity, double length, BlockMode blockMode, FluidMode fluidMode) {
        
        final Vector3d startingPosition = new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        final Vector3d lookVector = entity.getLookAngle();
        final Vector3d endingPosition = startingPosition.add(lookVector.x * length, lookVector.y * length, lookVector.z * length);
        return entity.level.clip(new RayTraceContext(startingPosition, endingPosition, blockMode, fluidMode, entity));
    }
    
    /**
     * Checks if an entity can be affected by fire. While fire immune entities can already
     * negate the effects of fire doing prechecks using this method can be used to avoid
     * rendering flickers or filter out these types of entities.
     * 
     * @param toCheck The entity to check.
     * @return Whether or not this entity can be affected by fire.
     */
    public static boolean isAffectedByFire (LivingEntity toCheck) {
        
        return !toCheck.fireImmune() && !toCheck.hasEffect(Effects.FIRE_RESISTANCE);
    }
    
    /**
     * Clears potion effect from an entity based on whether or not the effects are positive or
     * negative.
     * 
     * @param entity The entity to remove effects from.
     * @param removePositive Should positive effects be cleared?
     * @param removeNegative Should negative effects be cleared?
     */
    public static void clearEffects (LivingEntity entity, boolean removePositive, boolean removeNegative) {
        
        final Set<Effect> toClear = new HashSet<>();
        
        for (final EffectInstance effect : entity.getActiveEffects()) {
            
            final boolean isGood = effect.getEffect().isBeneficial();
            
            if (isGood && removePositive || !isGood && removeNegative) {
                
                toClear.add(effect.getEffect());
            }
        }
        
        for (final Effect effect : toClear) {
            
            entity.removeEffect(effect);
        }
    }
    
    /**
     * Get the egg color associated with an entity type. If the entity does not have an egg
     * type this will be 0 for both values.
     * 
     * @param type The entity type to get a color for.
     * @return A Tuple containing the primary and secondary egg colors.
     */
    public static Tuple<Integer, Integer> getEggColors (EntityType<?> type) {
        
        return eggColorCache.computeIfAbsent(type, key -> {
            
            final SpawnEggItem item = SpawnEggItem.byId(key);
            
            if (item != null) {
                
                Bookshelf.LOG.debug("Updating color cache for {} to primary={} secondary={}", type.getRegistryName(), item.color1, item.color2);
                return new Tuple<>(item.color1, item.color2);
            }
            
            return new Tuple<>(0, 0);
        });
    }
}