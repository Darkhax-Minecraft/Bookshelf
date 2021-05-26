package net.darkhax.bookshelf.lib;

import com.google.common.primitives.Ints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum MoonPhase {
    
    FULL(0, "full"),
    WAXING_GIBBOUS(1, "waxing.gibbous"),
    FIRST_QUARTER(2, "first.quarter"),
    WAXING_CRESCENT(3, "waxing.crescent"),
    NEW_MOON(4, "new"),
    WANING_CRESCENT(5, "waning.crescent"),
    LAST_QUARTER(6, "last.quarter"),
    WANING_GIBBOUS(7, "waning.gibbous");
    
    /**
     * The phase value for the moon.
     */
    private final int phase;
    
    /**
     * A translation key for the mood phase name.
     */
    private final String key;
    
    MoonPhase(int phase, String key) {
        
        this.phase = phase;
        this.key = key;
    }
    
    public int getPhase () {
        
        return this.phase;
    }
    
    public String getKey () {
        
        return this.key;
    }
    
    public static MoonPhase getPhase (int phase) {
        
        return MoonPhase.values()[Ints.constrainToRange(phase, 0, 7)];
    }
    
    @OnlyIn(Dist.CLIENT)
    public String getPhaseName () {
        
        return I18n.get("moon.phase." + this.key + ".name");
    }
    
    @OnlyIn(Dist.CLIENT)
    public static MoonPhase getCurrentPhase () {
        
        return getPhase(Minecraft.getInstance().level.getMoonPhase());
    }
}
