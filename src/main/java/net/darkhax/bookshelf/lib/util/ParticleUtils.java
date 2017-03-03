package net.darkhax.bookshelf.lib.util;

import net.darkhax.bookshelf.client.particle.OpenParticleDigging;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleUtils {

    /**
     * Spawns the digging particles for a block, similarly to the normal block hit effect
     * particle. The intended use of this method is to override the block hit effects.
     *
     * @param manager The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effects in.
     * @param pos The position to spawn the particles at.
     * @param side The side offset for the effect.
     * @return boolean Whether or not the effect actually spawned.
     */
    @SideOnly(Side.CLIENT)
    public static boolean spawnDigParticles (ParticleManager manager, IBlockState state, World world, BlockPos pos, EnumFacing side) {

        if (state != null && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {

            final int x = pos.getX();
            final int y = pos.getY();
            final int z = pos.getZ();
            final float offset = 0.1F;
            final AxisAlignedBB bounds = state.getBoundingBox(world, pos);
            double xOffset = x + Constants.RANDOM.nextDouble() * (bounds.maxX - bounds.minX - offset * 2.0F) + offset + bounds.minX;
            double yOffset = y + Constants.RANDOM.nextDouble() * (bounds.maxY - bounds.minY - offset * 2.0F) + offset + bounds.minY;
            double zOffset = z + Constants.RANDOM.nextDouble() * (bounds.maxZ - bounds.minZ - offset * 2.0F) + offset + bounds.minZ;

            if (side == EnumFacing.DOWN) {
                yOffset = y + bounds.minY - offset;
            }
            else if (side == EnumFacing.UP) {
                yOffset = y + bounds.maxY + offset;
            }
            else if (side == EnumFacing.NORTH) {
                zOffset = z + bounds.minZ - offset;
            }
            else if (side == EnumFacing.SOUTH) {
                zOffset = z + bounds.maxZ + offset;
            }
            else if (side == EnumFacing.WEST) {
                xOffset = x + bounds.minX - offset;
            }
            else if (side == EnumFacing.EAST) {
                xOffset = x + bounds.maxX + offset;
            }

            manager.addEffect(new OpenParticleDigging(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, state).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }

        return false;
    }

    /**
     * Spawns the break particles for a block, similarly to the normal block break effect
     * particle. The intended use of this method is to override the block break effects.
     *
     * @param manager The EffectRenderer, used to render the particle effect.
     * @param state The BlockState for the block to render the breaking effect of.
     * @param world The World to spawn the particle effect in.
     * @param pos The position to spawn the particles at.
     * @return boolean Whether or not the effect actually spawned.
     */
    @SideOnly(Side.CLIENT)
    public static boolean spawnBreakParticles (ParticleManager manager, IBlockState state, World world, BlockPos pos) {

        if (state.getBlock() != null) {

            final int multiplier = 4;

            for (int xOffset = 0; xOffset < multiplier; xOffset++) {
                for (int yOffset = 0; yOffset < multiplier; yOffset++) {
                    for (int zOffset = 0; zOffset < multiplier; zOffset++) {

                        final double xPos = pos.getX() + (xOffset + 0.5D) / multiplier;
                        final double yPos = pos.getY() + (yOffset + 0.5D) / multiplier;
                        final double zPos = pos.getZ() + (zOffset + 0.5D) / multiplier;
                        manager.addEffect(new OpenParticleDigging(world, xPos, yPos, zPos, xPos - pos.getX() - 0.5D, yPos - pos.getY() - 0.5D, zPos - pos.getZ() - 0.5D, state).setBlockPos(pos));
                    }
                }
            }
            
            return true;
        }

        return false;
    }

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
    @SideOnly(Side.CLIENT)
    public static void spawnParticleRing (World world, EnumParticleTypes particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {

        for (double degree = 0.0d; degree < 2 * Math.PI; degree += step) {
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
        }
    }

    /**
     * Spawns particles in a ring, centered around a certain point. This method takes a percent
     * argument which is used to calculate the amount of the ring to spawn.
     *
     * @param world The world to spawn the particles in.
     * @param particle The type of particle to spawn.
     * @param percent The percentage of the ring to render.
     * @param x The x position to spawn the particle around.
     * @param y The y position to spawn the particle around.
     * @param z The z position to spawn the particle around.
     * @param velocityX The velocity of the particle, in the x direction.
     * @param velocityY The velocity of the particle, in the y direction.
     * @param velocityZ The velocity of the particle, in the z direction.
     * @param step The distance in degrees, between each particle. The maximum is 2 * PI, which
     *        will create 1 particle per ring. 0.15 is a nice value.
     */
    public static void spawnPercentageParticleRing (World world, EnumParticleTypes particle, float percentage, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {

        for (double degree = 0.0d; degree < 2 * Math.PI * percentage; degree += step) {
            world.spawnParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
        }
    }
}
