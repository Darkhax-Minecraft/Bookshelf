package net.darkhax.bookshelf.potion;

import net.minecraft.nbt.NBTTagCompound;

public class BuffEffect {
    
    /**
     * The Buff being applied by this effect.
     */
    private Buff buff;
    
    /**
     * The duration of the effect in ticks. 20 ticks is 1 second.
     */
    public int duration;
    
    /**
     * The strength of this effect.
     */
    public int power;
    
    /**
     * Constructs a new BuffEffect.
     * 
     * @param buff: The buff to create the effect for.
     * @param duration: The duration of the effect.
     * @param power: The strength of the effect.
     */
    public BuffEffect(Buff buff, int duration, int power) {
        
        this.buff = buff;
        this.duration = duration;
        this.power = power;
    }
    
    /**
     * Writes this BuffEffect to an NBTTagCompound so that it can be saved.
     * 
     * @param nbt: The NBTTagCompound to write to.
     */
    public void writeToNBT (NBTTagCompound nbt) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("buff", this.buff.getPotionName());
        tag.setInteger("duration", this.duration);
        tag.setInteger("power", this.power);
        nbt.setTag("BookshelfBuff", tag);
    }
    
    /**
     * Reads a BuffEffect from an NBTTagCompound.
     * 
     * @param nbt: The NBTTagCompound to read from.
     * @return BuffEffect: The BuffEffect that has been recovered from the NBTTagCompound.
     */
    public static BuffEffect readFromNBT (NBTTagCompound nbt) {
        
        NBTTagCompound tag = nbt.getCompoundTag("BookshelfBuff");
        return new BuffEffect(BuffHelper.getBuffFromString(tag.getString("buff")), tag.getInteger("duration"), tag.getInteger("power"));
    }
    
    /**
     * Provides the buff used in this BuffEffect.
     * 
     * @return Buff: The Buff used in this BuffEffect.
     */
    public Buff getBuff () {
        
        return buff;
    }
    
    @Override
    public String toString () {
        
        return buff.getPotionName() + " duration: " + duration + " power: " + power;
    }
}