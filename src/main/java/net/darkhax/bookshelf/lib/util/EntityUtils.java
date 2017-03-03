package net.darkhax.bookshelf.lib.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class EntityUtils {

    /**
     * An array of armor equipment slots.
     */
    private static final EntityEquipmentSlot[] EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private EntityUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Calculates the distance between two entities.
     *
     * @param firstEntity The first entity to use.
     * @param secondEntity The second entity to use.
     * @return double The distance between the two entities passed.
     */
    public static double getDistanceFromEntity (Entity firstEntity, Entity secondEntity) {

        return MathsUtils.getDistanceBetweenPoints(firstEntity.getPositionVector(), secondEntity.getPositionVector());
    }

    /**
     * Calculates the distance between an entity and a BlockPos.
     *
     * @param entity The Entity to use for the first position.
     * @param pos The BlockPos to use for the second position.
     * @return double The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos (Entity entity, BlockPos pos) {

        return MathsUtils.getDistanceBetweenPoints(entity.getPositionVector(), new Vec3d(pos));
    }

    /**
     * Pushes an entity towards a specific direction.
     *
     * @param entityToMove The entity that you want to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, EnumFacing direction, double force) {

        pushTowards(entityToMove, entityToMove.getPosition().offset(direction.getOpposite(), 1), force);
    }

    /**
     * Pushes an Entity towards a BlockPos.
     *
     * @param entityToMove The entity that you want to push.
     * @param pos The BlockPos to push the entity towards.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, BlockPos pos, double force) {

        final BlockPos entityPos = entityToMove.getPosition();
        final double distanceX = pos.getX() - entityPos.getX();
        final double distanceY = pos.getY() - entityPos.getY();
        final double distanceZ = pos.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (distance > 0) {

            entityToMove.motionX = distanceX / distance * force;
            entityToMove.motionY = distanceY / distance * force;
            entityToMove.motionZ = distanceZ / distance * force;
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

        final double distanceX = destination.posX - entityToMove.posX;
        final double distanceY = destination.posY - entityToMove.posY;
        final double distanceZ = destination.posZ - entityToMove.posZ;
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (distance > 0) {

            entityToMove.motionX = distanceX / distance * force;
            entityToMove.motionY = distanceY / distance * force;
            entityToMove.motionZ = distanceZ / distance * force;
        }
    }

    /**
     * Creates a Vec3d that represents the additional motion that would be needed to push an
     * entity towards a destination.
     *
     * @param entityToMove The entity to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to use.
     * @return A Vec3d object that represents the motion of pushing the entity towards the
     *         destination.
     */
    public static Vec3d pushTowardsDirection (Entity entityToMove, EnumFacing direction, double force) {

        final BlockPos entityPos = entityToMove.getPosition();
        final BlockPos destination = entityToMove.getPosition().offset(direction.getOpposite(), 1);

        final double distanceX = destination.getX() - entityPos.getX();
        final double distanceY = destination.getY() - entityPos.getY();
        final double distanceZ = destination.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        return distance > 0 ? new Vec3d(entityToMove.motionX = distanceX / distance * force, entityToMove.motionY = distanceY / distance * force, entityToMove.motionZ = distanceZ / distance * force) : new Vec3d(0d, 0d, 0d);
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
     * @param entityClass The type of entity you are looking for.
     * @param world The world to search in.
     * @param pos The position to start the search around.
     * @param range The range of the search.
     * @return List<Entity> A List containing all entities of the specified type that are
     *         within the range.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getEntitiesInArea (Class<? extends Entity> entityClass, World world, BlockPos pos, int range) {

        return (List<T>) world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
    }

    /**
     * Gets the type of equipment for slot index.
     *
     * @param index The index of the slot.
     * @return EntityEquipmentSlot The slot for the index.
     */
    public static EntityEquipmentSlot getEquipmentSlot (int index) {

        if (index >= 0 && index < EQUIPMENT_SLOTS.length)
            return EQUIPMENT_SLOTS[index];

        return null;
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
    public static boolean isWearingFullSet (EntityLivingBase living, Class<Item> armorClass) {

        for (final EntityEquipmentSlot slot : EntityEquipmentSlot.values())
            if (slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR)) {

                final ItemStack armor = living.getItemStackFromSlot(slot);

                if (armor == null || !armor.getItem().getClass().equals(armorClass))
                    return false;
            }

        return true;
    }
}