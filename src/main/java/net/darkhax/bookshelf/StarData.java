package net.darkhax.bookshelf;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class StarData {
    
    private final PlayerEntity player;
    private int starsCollected = 0;
    
    public StarData(PlayerEntity player) {
        
        this.player = player;
        this.read();
    }
    
    public int getStarsCollected() {
        
        return this.starsCollected;
    }
    
    public void setStarsCollected(int starCount) {
        
        this.starsCollected = starCount;
    }
    
    public void read () {
        
        final CompoundNBT playerData = player.getPersistentData().getCompound("YourData");
        this.starsCollected = playerData.getInt("StarsCollected");
    }
    
    public void write() {
        
        final CompoundNBT playerData = new CompoundNBT();
        playerData.putInt("StarsCollected", this.starsCollected);
        player.getPersistentData().put("YourData", playerData);
    }
}