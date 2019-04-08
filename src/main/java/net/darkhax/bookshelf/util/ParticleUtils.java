/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;

public final class ParticleUtils {
    
    /**
     * Spawns particles in a ring, centered around a certain position.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *        will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnParticleRing (World world, IParticleData particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < 2 * Math.PI; degree += step) {
            world.addParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
        }
    }
    
    /**
     * Spawns particles in a ring, centered around a certain point. This method takes a percent
     * argument which is used to calculate the amount of the ring to spawn.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param percentage The percentage of the ring to render.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *        will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnPercentageParticleRing (World world, IParticleData particle, float percentage, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree < 2 * Math.PI * percentage; degree += step) {
            world.addParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
        }
    }
    
    /**
     * Spawns particles in a ring, centered around a certain point. This method takes a percent
     * argument which is used to calculate the amount of the ring to spawn. The height of the
     * particle will also go up and down like a wave.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param percentage The percentage of the ring to render.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *        will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnWavingParticleRing (World world, IParticleData particle, float percentage, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        
        for (double degree = 0.0d; degree <= 2 * Math.PI * percentage; degree += 0.15) {
            
            world.addParticle(particle, x + Math.cos(degree), y - Math.cos(Math.sin(degree)) + 0.5, z + Math.sin(degree), 0, 0, 0);
        }
    }
}