package net.darkhax.bookshelf.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EntityHelper {

    /**
     * A cache of spawn egg colors mapped to the entity type. Populated by {@link #getEggColors(EntityType)}.
     */
    private static final Map<EntityType<?>, Tuple<Integer, Integer>> eggColorCache = new HashMap<>();

    /**
     * Calculates the distance between two entities.
     *
     * @param firstEntity  The first entity to use.
     * @param secondEntity The second entity to use.
     * @return double The distance between the two entities passed.
     */
    public static double getDistanceFromEntity(Entity firstEntity, Entity secondEntity) {

        return MathsHelper.getDistanceBetweenPoints(firstEntity.position(), secondEntity.position());
    }

    /**
     * Calculates the distance between an entity and a BlockPos.
     *
     * @param entity The Entity to use for the first position.
     * @param pos    The BlockPos to use for the second position.
     * @return double The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos(Entity entity, BlockPos pos) {

        return MathsHelper.getDistanceBetweenPoints(entity.position(), Vec3.atCenterOf(pos));
    }

    /**
     * Pushes an entity towards a specific direction.
     *
     * @param entityToMove The entity that you want to push.
     * @param direction    The direction to push the entity.
     * @param force        The amount of force to push the entity with.
     */
    public static void pushTowards(Entity entityToMove, Direction direction, double force) {

        pushTowards(entityToMove, entityToMove.blockPosition().relative(direction.getOpposite(), 1), force);
    }

    /**
     * Pushes an Entity towards a BlockPos.
     *
     * @param entityToMove The entity that you want to push.
     * @param pos          The BlockPos to push the entity towards.
     * @param force        The amount of force to push the entity with.
     */
    public static void pushTowards(Entity entityToMove, BlockPos pos, double force) {

        final BlockPos entityPos = entityToMove.blockPosition();
        final double distanceX = (double) pos.getX() - entityPos.getX();
        final double distanceY = (double) pos.getY() - entityPos.getY();
        final double distanceZ = (double) pos.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vec3(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }

    /**
     * Pushes an entity towards another one.
     *
     * @param entityToMove The entity that should be pushed towards the other entity.
     * @param destination  The destination entity, that the entity to move should be pushed towards.
     * @param force        The amount of force to push the entityToMove with.
     */
    public static void pushTowards(Entity entityToMove, Entity destination, double force) {

        final double distanceX = destination.getX() - entityToMove.getX();
        final double distanceY = destination.getY() - entityToMove.getY();
        final double distanceZ = destination.getZ() - entityToMove.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vec3(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }

    /**
     * Creates a Vector3d that represents the additional motion that would be needed to push an entity towards a
     * destination.
     *
     * @param entityToMove The entity to push.
     * @param direction    The direction to push the entity.
     * @param force        The amount of force to use.
     */
    public static void pushTowardsDirection(Entity entityToMove, Direction direction, double force) {

        final BlockPos entityPos = entityToMove.blockPosition();
        final BlockPos destination = entityToMove.blockPosition().relative(direction.getOpposite(), 1);

        final double distanceX = (double) destination.getX() - entityPos.getX();
        final double distanceY = (double) destination.getY() - entityPos.getY();
        final double distanceZ = (double) destination.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (distance > 0) {
            entityToMove.setDeltaMovement(new Vec3(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
        }
    }

    /**
     * Checks if two entities are close enough together.
     *
     * @param firstEntity  The first entity to check.
     * @param secondEntity The second entity to check.
     * @param maxDistance  The maximum distance that the entities can be apart.
     * @return boolean True if the distance between the entities are within range of the maxDistance.
     */
    public static boolean areEntitiesCloseEnough(Entity firstEntity, Entity secondEntity, double maxDistance) {

        return getDistanceFromEntity(firstEntity, secondEntity) < maxDistance * maxDistance;
    }

    /**
     * Gets a List of entities that are within the provided area.
     *
     * @param <T>         The type of entities to look for.
     * @param entityClass The type of entity you are looking for.
     * @param world       The world to search in.
     * @param pos         The position to start the search around.
     * @param range       The range of the search.
     * @return A List containing all entities of the specified type that are within the range.
     */
    public static <T extends Entity> List<T> getEntitiesInArea(Class<T> entityClass, Level world, BlockPos pos, int range) {

        return world.getEntitiesOfClass(entityClass, new AABB(MathsHelper.vec3(pos.offset(-range, -range, -range)), MathsHelper.vec3(pos.offset(range + 1, range + 1, range + 1))));
    }

    /**
     * A check to see if an entity is wearing a full suit of the armor. This check is based on the class names of
     * armor.
     *
     * @param living:     The living entity to check the armor of.
     * @param armorClass: The class of the armor to check against.
     * @return boolean: True if every piece of armor the entity is wearing are the same class as the provied armor
     * class.
     */
    public static boolean isWearingFullSet(Mob living, Class<Item> armorClass) {

        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType().equals(EquipmentSlot.Type.ARMOR)) {

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
     * @param entity    The entity to perform a ray trace on.
     * @param length    The distance to cast the rays.
     * @param blockMode The mode used when detecting blocks.
     * @param fluidMode The mode used when detecting fluids.
     * @return An object containing the results of the ray trace.
     */
    public static HitResult rayTrace(LivingEntity entity, double length, Block blockMode, Fluid fluidMode) {

        final Vec3 startingPosition = new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        final Vec3 lookVector = entity.getLookAngle();
        final Vec3 endingPosition = startingPosition.add(lookVector.x * length, lookVector.y * length, lookVector.z * length);
        return entity.level().clip(new ClipContext(startingPosition, endingPosition, blockMode, fluidMode, entity));
    }

    /**
     * Checks if an entity can be affected by fire. While fire immune entities can already negate the effects of fire
     * doing prechecks using this method can be used to avoid rendering flickers or filter out these types of entities.
     *
     * @param toCheck The entity to check.
     * @return Whether or not this entity can be affected by fire.
     */
    public static boolean isAffectedByFire(LivingEntity toCheck) {

        return !toCheck.fireImmune() && !toCheck.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    /**
     * Clears potion effect from an entity based on whether or not the effects are positive or negative.
     *
     * @param entity         The entity to remove effects from.
     * @param removePositive Should positive effects be cleared?
     * @param removeNegative Should negative effects be cleared?
     */
    public static void clearEffects(LivingEntity entity, boolean removePositive, boolean removeNegative) {

        final Set<MobEffect> toClear = new HashSet<>();

        for (final MobEffectInstance effect : entity.getActiveEffects()) {

            final boolean isGood = effect.getEffect().isBeneficial();

            if (isGood && removePositive || !isGood && removeNegative) {

                toClear.add(effect.getEffect());
            }
        }

        for (final MobEffect effect : toClear) {

            entity.removeEffect(effect);
        }
    }

    /**
     * Get the egg color associated with an entity type. If the entity does not have an egg type this will be 0 for both
     * values.
     *
     * @param type The entity type to get a color for.
     * @return A Tuple containing the primary and secondary egg colors.
     */
    public static Tuple<Integer, Integer> getEggColors(EntityType<?> type) {

        return eggColorCache.computeIfAbsent(type, key -> {

            SpawnEggItem item = SpawnEggItem.byId(key);

            if (item != null) {

                return new Tuple<>(item.getColor(0), item.getColor(1));
            }

            return new Tuple<>(0, 0);
        });
    }
}