package net.darkhax.bookshelf.entity;

import java.util.List;

import net.darkhax.bookshelf.item.ItemModPotion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityModPotion extends EntityPotion {
    
    public ItemStack potionStack;
    
    public EntityModPotion(World world, EntityLivingBase living, ItemStack stack) {
        
        super(world, living, stack);
        this.potionStack = stack;
    }
    
    public EntityModPotion(World world, ItemStack stack) {
        
        super(world);
        this.potionStack = stack;
    }
    
    protected void onImpact (MovingObjectPosition pos) {
        
        if (!this.worldObj.isRemote) {
            
            List<PotionEffect> list = ItemModPotion.getEffects(potionStack);
            
            if (list != null && !list.isEmpty()) {
                
                AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
                List<EntityLivingBase> entities = this.worldObj.<EntityLivingBase> getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                
                if (!entities.isEmpty()) {
                    
                    for (EntityLivingBase entitylivingbase : entities) {
                        
                        double d0 = this.getDistanceSqToEntity(entitylivingbase);
                        
                        if (d0 < 16.0D) {
                            
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                            
                            if (entitylivingbase == pos.entityHit)
                                d1 = 1.0D;
                                
                            for (PotionEffect potioneffect : list) {
                                
                                int i = potioneffect.getPotionID();
                                
                                if (Potion.potionTypes[i].isInstant())
                                    Potion.potionTypes[i].affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                                    
                                else {
                                    
                                    int j = (int) (d1 * (double) potioneffect.getDuration() + 0.5D);
                                    
                                    if (j > 20)
                                        entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
                                }
                            }
                        }
                    }
                }
            }
            
            this.worldObj.playAuxSFX(2002, new BlockPos(this), this.getPotionDamage());
            this.setDead();
        }
    }
    
    @Override
    public void readEntityFromNBT (NBTTagCompound tag) {
        
        super.readEntityFromNBT(tag);
        this.potionStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("PotionStack"));
    }
    
    @Override
    public void writeEntityToNBT (NBTTagCompound tag) {
        
        super.writeEntityToNBT(tag);
        tag.setTag("PotionStack", this.potionStack.writeToNBT(new NBTTagCompound()));
    }
}
