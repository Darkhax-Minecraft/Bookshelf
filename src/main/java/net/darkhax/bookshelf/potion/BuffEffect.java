package net.darkhax.bookshelf.potion;

import net.minecraft.nbt.NBTTagCompound;

public class BuffEffect {
    
    private Buff buff;
    private int duration;
    private int power;
    
    public BuffEffect(Buff buff, int duration, int power) {
        
        this.buff = buff;
        this.duration = duration;
        this.power = power;
    }
    
    public void writeToNBT (NBTTagCompound nbt) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("buff", buff.getPotionName());
        tag.setInteger("duration", getDuration());
        tag.setInteger("power", getPower());
        nbt.setTag("BookshelfBuff", tag);
    }
    
    public static BuffEffect readFromNBT (NBTTagCompound nbt) {
        
        NBTTagCompound tag = nbt.getCompoundTag("BookshelfBuff");
        return new BuffEffect(BuffHelper.getBuffFromString(tag.getString("buff")), tag.getInteger("duration"), tag.getInteger("power"));
    }
    
    public Buff getBuff () {
        
        return buff;
    }
    
    public int getDuration () {
        
        return duration;
    }
    
    public int getPower () {
        
        return power;
    }
    
    public void setDuration (int duration) {
        
        this.duration = duration;
    }
    
    public void setPower (int power) {
        
        this.power = power;
    }
    
    @Override
    public String toString () {
        
        return "BuffEffect [buff=" + getBuff() + ", duration=" + getDuration() + ", power=" + getPower() + "]";
    }
    
}
