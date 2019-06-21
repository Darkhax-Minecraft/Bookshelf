/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

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
    private EntityUtils() {

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
        final double distanceX = (double) pos.getX() - entityPos.getX();
        final double distanceY = (double) pos.getY() - entityPos.getY();
        final double distanceZ = (double) pos.getZ() - entityPos.getZ();
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

        final double distanceX = (double) destination.getX() - entityPos.getX();
        final double distanceY = (double) destination.getY() - entityPos.getY();
        final double distanceZ = (double) destination.getZ() - entityPos.getZ();
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

        if (index >= 0 && index < EQUIPMENT_SLOTS.length) {
            return EQUIPMENT_SLOTS[index];
        }

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

        for (final EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR)) {

                final ItemStack armor = living.getItemStackFromSlot(slot);

                if (armor.isEmpty() || !armor.getItem().getClass().equals(armorClass)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Teleports an entity in an ender way. This takes into account changing dimensions, and
     * firing the ender teleport event.
     *
     * @param entity The entity to teleport.
     * @param dimension The dimension to teleport the entity to.
     * @param x The x position.
     * @param y The y position.
     * @param z The z position.
     * @param damage The amount of damage to deal. 0 is a valid damage amount.
     */
    public static void enderTeleport (EntityLivingBase entity, int dimension, double x, double y, double z, float damage) {

        final EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, damage);

        if (!event.isCanceled()) {

            entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            entity.fallDistance = 0f;

            if (damage > 0f) {

                entity.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
            }

            if (entity.dimension != dimension) {

                if (PlayerUtils.isPlayerReal(entity)) {

                    final EntityPlayerMP player = (EntityPlayerMP) entity;
                    PlayerUtils.changeDimension(player, dimension);
                }

                else {

                    entity.changeDimension(dimension);
                }
            }
        }
    }

    /**
     * Changes the world that an entity is in. This allows for changing dimensions safer when
     * working with other mods.
     *
     * @param entity The entity to change the world of.
     * @param worldOld The old entity world.
     * @param worldNew The new entity world.
     */
    public static void changeWorld (Entity entity, WorldServer worldOld, WorldServer worldNew) {

        final WorldProvider providerOld = worldOld.provider;
        final WorldProvider providerNew = worldNew.provider;
        final double moveFactor = providerOld.getMovementFactor() / providerNew.getMovementFactor();
        final double x = MathHelper.clamp(entity.posX * moveFactor, -29999872, 29999872);
        final double z = MathHelper.clamp(entity.posZ * moveFactor, -29999872, 29999872);

        if (entity.isEntityAlive()) {

            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            worldNew.spawnEntity(entity);
            worldNew.updateEntityWithOptionalForce(entity, false);
        }

        entity.setWorld(worldNew);
    }

    /**
     * Gets the max health value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMaxHealth (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    /**
     * Gets the follow/tracking range value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getFollowRange (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
    }

    /**
     * Gets the knockback resistance value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getKnockbackResistance (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
    }

    /**
     * Gets the movement speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMovementSpeed (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
    }

    /**
     * Gets the attack value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackDamage (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    }

    /**
     * Gets the attack speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackSpeed (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue();
    }

    /**
     * Gets the armor value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmor (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
    }

    /**
     * Gets the armor toughness value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmorToughness (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();
    }

    /**
     * Gets the luck value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getLuck (EntityLivingBase entity) {

        return entity.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
    }

    /**
     * Gets a value of an attribute for an entity.
     *
     * @param entity The entity to get the value of.
     * @param attribute The attribute to get the value of.
     * @return The value of the attribute.
     */
    public static double getAttributeValue (EntityLivingBase entity, IAttribute attribute) {

        return entity.getEntityAttribute(attribute).getAttributeValue();
    }

    /**
     * Adds an item drop to a LivingDropsEvent. Allows for simple and quick item additions
     * where needed.
     *
     * @param stack The item you want to add.
     * @param event The event context info.
     */
    public static void addDrop (ItemStack stack, LivingDropsEvent event) {

        final EntityLivingBase living = event.getEntityLiving();
        final EntityItem itemEntity = new EntityItem(living.world, living.posX, living.posY, living.posZ, stack);
        itemEntity.setDefaultPickupDelay();
        event.getDrops().add(itemEntity);
    }
}