/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
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
     * Calculates the distance between two entities.
     *
     * @param firstEntity The first entity to use.
     * @param secondEntity The second entity to use.
     * @return double The distance between the two entities passed.
     */
    public static double getDistanceFromEntity (Entity firstEntity, Entity secondEntity) {
        
        return MathsUtils.getDistanceBetweenPoints(firstEntity.getPositionVec(), secondEntity.getPositionVec());
    }
    
    /**
     * Calculates the distance between an entity and a BlockPos.
     *
     * @param entity The Entity to use for the first position.
     * @param pos The BlockPos to use for the second position.
     * @return double The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos (Entity entity, BlockPos pos) {
        
        return MathsUtils.getDistanceBetweenPoints(entity.getPositionVec(), Vector3d.func_237491_b_(pos));
    }
    
    /**
     * Pushes an entity towards a specific direction.
     *
     * @param entityToMove The entity that you want to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, Direction direction, double force) {
        // TODO MCP-name: func_233580_cy_ -> getPosition
        pushTowards(entityToMove, entityToMove.func_233580_cy_().offset(direction.getOpposite(), 1), force);
    }
    
    /**
     * Pushes an Entity towards a BlockPos.
     *
     * @param entityToMove The entity that you want to push.
     * @param pos The BlockPos to push the entity towards.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, BlockPos pos, double force) {

        // TODO MCP-name: func_233580_cy_ -> getPosition
        final BlockPos entityPos = entityToMove.func_233580_cy_();
        final double distanceX = (double) pos.getX() - entityPos.getX();
        final double distanceY = (double) pos.getY() - entityPos.getY();
        final double distanceZ = (double) pos.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setMotion(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
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
        
        final double distanceX = destination.getPosX() - entityToMove.getPosX();
        final double distanceY = destination.getPosY() - entityToMove.getPosY();
        final double distanceZ = destination.getPosZ() - entityToMove.getPosZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setMotion(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
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
        // TODO MCP-name: func_233580_cy_ -> getPosition
        final BlockPos entityPos = entityToMove.func_233580_cy_();
        // TODO MCP-name: func_233580_cy_ -> getPosition
        final BlockPos destination = entityToMove.func_233580_cy_().offset(direction.getOpposite(), 1);
        
        final double distanceX = (double) destination.getX() - entityPos.getX();
        final double distanceY = (double) destination.getY() - entityPos.getY();
        final double distanceZ = (double) destination.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0) {
            entityToMove.setMotion(new Vector3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
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
        
        return world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
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
            if (slot.getSlotType().equals(EquipmentSlotType.Group.ARMOR)) {
                
                final ItemStack armor = living.getItemStackFromSlot(slot);
                
                if (armor.isEmpty() || !armor.getItem().getClass().equals(armorClass)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Gets the max health value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMaxHealth (MobEntity entity) {
        // TODO MCP-name: field_233818_a_ -> MAX_HEALTH
        return entity.getAttribute(Attributes.field_233818_a_).getValue();
    }
    
    /**
     * Gets the follow/tracking range value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getFollowRange (MobEntity entity) {
        // TODO MCP-name: field_233819_b_ -> FOLLOW_RANGE
        return entity.getAttribute(Attributes.field_233819_b_).getValue();
    }
    
    /**
     * Gets the knockback resistance value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getKnockbackResistance (MobEntity entity) {
        // TODO MCP-name: field_233820_c_ -> KNOCKBACK_RESISTANCE
        return entity.getAttribute(Attributes.field_233820_c_).getValue();
    }
    
    /**
     * Gets the movement speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMovementSpeed (MobEntity entity) {
        // TODO MCP-name: field_233821_d_ -> MOVEMENT_SPEED
        return entity.getAttribute(Attributes.field_233821_d_).getValue();
    }

    // TODO Attributes.field_233824_e_	/ Attributes.FLYING_SPEED

    /**
     * Gets the attack value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackDamage (MobEntity entity) {
        // TODO MCP-name: field_233823_f_ -> ATTACK_DAMAGE
        return entity.getAttribute(Attributes.field_233823_f_).getValue();
    }

    // TODO Attributes.field_233824_g_	/ Attributes.ATTACK_KNOCKBACK

    /**
     * Gets the attack speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackSpeed (MobEntity entity) {
        // TODO MCP-name: field_233825_h_ -> ATTACK_SPEED
        return entity.getAttribute(Attributes.field_233825_h_).getValue();
    }
    
    /**
     * Gets the armor value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmor (MobEntity entity) {
        // TODO MCP-name: field_233826_i_ -> ARMOR
        return entity.getAttribute(Attributes.field_233826_i_).getValue();
    }
    
    /**
     * Gets the armor toughness value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmorToughness (MobEntity entity) {
        // TODO MCP-name: field_233827_j_ -> ARMOR_TOUGHNESS
        return entity.getAttribute(Attributes.field_233827_j_).getValue();
    }
    
    /**
     * Gets the luck value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getLuck (MobEntity entity) {
        // TODO MCP-name: field_233828_k_ -> LUCK
        return entity.getAttribute(Attributes.field_233828_k_).getValue();
    }
    
    /**
     * Gets a value of an attribute for an entity.
     *
     * @param entity The entity to get the value of.
     * @param attribute The attribute to get the value of.
     * @return The value of the attribute.
     */
    public static double getValue (MobEntity entity, Attribute attribute) {
        
        return entity.getAttribute(attribute).getValue();
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
        
        final Vector3d startingPosition = new Vector3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());
        final Vector3d lookVector = entity.getLookVec();
        final Vector3d endingPosition = startingPosition.add(lookVector.x * length, lookVector.y * length, lookVector.z * length);
        return entity.world.rayTraceBlocks(new RayTraceContext(startingPosition, endingPosition, blockMode, fluidMode, entity));
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
        // TODO MCP-name: func_230279_az_ -> isImmuneToFire
        return !toCheck.func_230279_az_() && !toCheck.isPotionActive(Effects.FIRE_RESISTANCE);
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
        
        for (final EffectInstance effect : entity.getActivePotionEffects()) {
            
            final boolean isGood = effect.getPotion().isBeneficial();
            
            if (isGood && removePositive || !isGood && removeNegative) {
                
                toClear.add(effect.getPotion());
            }
        }
        
        for (final Effect effect : toClear) {
            
            entity.removePotionEffect(effect);
        }
    }
}