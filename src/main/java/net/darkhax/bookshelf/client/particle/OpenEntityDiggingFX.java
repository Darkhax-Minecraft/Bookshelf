package net.darkhax.bookshelf.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.world.World;

public class OpenEntityDiggingFX extends EntityDiggingFX {
    
    /**
     * Constructs a new EntityDiggingFX. The constructor in EntityDiggingFX is not publicly
     * available, so a work around is required if you want to make one of those.
     * 
     * @param world The World instance to spawn the particle in.
     * @param x The X position for the particle.
     * @param y The Y position for the particle.
     * @param z The Z position for the particle.
     * @param xSpeed The velocity of the particle on the X axis.
     * @param ySpeed The velocity of the particle on the Y axis.
     * @param zSpeed The velocity of the particle on the Z axis.
     * @param state The IBlockState of the block to create the particle for.
     */
    public OpenEntityDiggingFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IBlockState state) {
        
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, state);
    }
}