/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.entity;

import java.util.UUID;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

/**
 * This class provides the basic entity code for a player mob. A player mob is one that
 * resembles a player, like zombies. This also includes logic for having baby variations
 * similar to baby zombies. There is also a model and a renderer class available for this type
 * of mob.
 */
public class EntityPlayerMob extends EntityMob {
    
    /**
     * The ID of the modifier used for increasing the speed of a child mob.
     */
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    
    /**
     * The modifier for the child baby speed.
     */
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
    
    /**
     * The data manager for tracking whether or not a mob is a child.
     */
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean> createKey(EntityPlayerMob.class, DataSerializers.BOOLEAN);
    
    private float mobWidth = -1.0F;
    private float mobHeight;
    
    public EntityPlayerMob(World world) {
        
        super(world);
        this.setSize(0.6F, 1.95F);
    }
    
    @Override
    public void entityInit () {
        
        super.entityInit();
        this.getDataManager().register(IS_CHILD, Boolean.valueOf(false));
    }
    
    @Override
    public boolean isChild () {
        
        return this.getDataManager().get(IS_CHILD).booleanValue();
    }
    
    @Override
    public int getExperiencePoints (EntityPlayer player) {
        
        if (this.isChild()) {
            
            this.experienceValue = (int) (this.experienceValue * 2.5F);
        }
        
        return super.getExperiencePoints(player);
    }
    
    @Override
    public void notifyDataManagerChange (DataParameter<?> key) {
        
        if (IS_CHILD.equals(key)) {
            
            this.setChildSize(this.isChild());
        }
        
        super.notifyDataManagerChange(key);
    }
    
    @Override
    public void writeEntityToNBT (NBTTagCompound compound) {
        
        super.writeEntityToNBT(compound);
        
        compound.setBoolean("IsBaby", this.isChild());
    }
    
    @Override
    public void readEntityFromNBT (NBTTagCompound compound) {
        
        super.readEntityFromNBT(compound);
        
        this.setChild(compound.getBoolean("IsBaby"));
    }
    
    @Override
    public float getEyeHeight () {
        
        final float height = 1.74F;
        return this.isChild() ? height : height - 0.81f;
    }
    
    @Override
    public final void setSize (float width, float height) {
        
        final boolean flag = this.mobWidth > 0.0F && this.mobHeight > 0.0F;
        this.mobWidth = width;
        this.mobHeight = height;
        
        if (!flag) {
            
            this.multiplySize(1.0F);
        }
    }
    
    @Override
    public double getYOffset () {
        
        return this.isChild() ? 0.0D : -0.45D;
    }
    
    @Override
    public IEntityLivingData onInitialSpawn (DifficultyInstance difficulty, IEntityLivingData livingdata) {
        
        if (MathsUtils.tryPercentage(this.getChildSpawnChance(difficulty, livingdata))) {
            
            this.setChild(true);
        }
        
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    public double getChildSpawnChance (DifficultyInstance difficulty, IEntityLivingData livingdata) {
        
        return 0.05d;
    }
    
    public final void multiplySize (float size) {
        
        super.setSize(this.mobWidth * size, this.mobHeight * size);
    }
    
    public void setChildSize (boolean isChild) {
        
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }
    
    public void setChild (boolean isChild) {
        
        this.getDataManager().set(IS_CHILD, Boolean.valueOf(isChild));
        
        if (this.world != null && !this.world.isRemote) {
            
            final IAttributeInstance speed = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            speed.removeModifier(BABY_SPEED_BOOST);
            
            if (isChild) {
                
                speed.applyModifier(BABY_SPEED_BOOST);
            }
        }
        
        this.setChildSize(isChild);
    }
}