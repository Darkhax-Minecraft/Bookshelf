package net.darkhax.bookshelf.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * An implementation of Entity which doesn't actually do anything. Useful when you need to make
 * references to an entity class for tricking registers.
 */
public class FakeEntity extends Entity {
    
    public FakeEntity(World worldIn) {
        
        super(worldIn);
    }
    
    @Override
    protected void entityInit () {
        
    }
    
    @Override
    protected void readEntityFromNBT (NBTTagCompound compound) {
        
    }
    
    @Override
    protected void writeEntityToNBT (NBTTagCompound compound) {
        
    }
}