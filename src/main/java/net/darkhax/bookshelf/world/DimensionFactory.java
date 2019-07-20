package net.darkhax.bookshelf.world;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ModDimension;

public abstract class DimensionFactory extends ModDimension {
    
    public abstract boolean hasSkylight ();
    
    public abstract PacketBuffer getDefaultData ();
}