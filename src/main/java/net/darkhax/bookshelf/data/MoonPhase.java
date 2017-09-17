package net.darkhax.bookshelf.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum MoonPhase {

    FULL(0, "full"),
    WAXING_GIBBOUS(1, "waxing.gibbous"),
    FIRST_QUARTER(2, "first.quarter"),
    WAXING_CRESCENT(3, "waxing.crescent"),
    NEW_MOON(4, "new.moon"),
    WANING_CRESCENT(5, "waning.crescent"),
    LAST_QUARTER(6, "last.quarter"),
    WANING_GIBBOUS(7, "waning.gibbous");

    private final int phase;

    private final String key;

    MoonPhase (int phase, String key) {

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

        final int safePhase = phase < 0 ? 0 : phase > 7 ? 7 : phase;
        return MoonPhase.values()[safePhase];
    }

    @SideOnly(Side.CLIENT)
    public String getPhaseName () {

        return I18n.format("moon.phase." + this.key + ".name");
    }

    @SideOnly(Side.CLIENT)
    public static MoonPhase getCurrentPhase () {

        return getPhase(Minecraft.getMinecraft().world.getMoonPhase());
    }
}
