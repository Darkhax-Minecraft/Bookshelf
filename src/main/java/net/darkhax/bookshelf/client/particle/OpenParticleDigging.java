/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.client.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

public class OpenParticleDigging extends DiggingParticle {
    
    /**
     * Constructs a new ParticleDigging. The constructor in ParticleDigging is not publicly
     * available, so a work around is required if you want to spawn one of those. This version
     * of the particle opens up that constructor.
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
    public OpenParticleDigging(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
        
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, state);
    }
}