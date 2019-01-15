package net.darkhax.bookshelf.world;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Notice: This class derives a lot of code from the original explosion class. It should not be
 * considered part of the LGPL 2.1 license. It is planned to eventually rewrite most of this
 * logic as it is not very good.
 */
public class ExplosionBase extends Explosion {
    
    public ExplosionBase(World world, Entity entity, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        
        super(world, entity, x, y, z, size, flaming, damagesTerrain);
    }
    
    @Override
    public void doExplosionA () {
        
        final Set<BlockPos> set = Sets.<BlockPos> newHashSet();
        
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = j / 15.0F * 2.0F - 1.0F;
                        double d1 = k / 15.0F * 2.0F - 1.0F;
                        double d2 = l / 15.0F * 2.0F - 1.0F;
                        final double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;
                        
                        // this is weird mojang code
                        for (final float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            final BlockPos blockpos = new BlockPos(d4, d6, d8);
                            final IBlockState affectedState = this.world.getBlockState(blockpos);
                            
                            if (affectedState.getMaterial() != Material.AIR) {
                                final float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, affectedState) : affectedState.getBlock().getExplosionResistance(this.world, blockpos, (Entity) null, this);
                                f -= (f2 + 0.3F) * 0.3F;
                            }
                            
                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, affectedState, f))) {
                                set.add(blockpos);
                            }
                            
                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }
        
        this.affectedBlockPositions.addAll(set);
        
        if (this.affectsEntities()) {
            
            final float totalSize = this.size * 2.0F;
            
            final int xMin = MathHelper.floor(this.x - totalSize - 1.0D);
            final int xMax = MathHelper.floor(this.x + totalSize + 1.0D);
            final int yMin = MathHelper.floor(this.y - totalSize - 1.0D);
            final int yMax = MathHelper.floor(this.y + totalSize + 1.0D);
            final int zMin = MathHelper.floor(this.z - totalSize - 1.0D);
            final int zMax = MathHelper.floor(this.z + totalSize + 1.0D);
            
            final List<Entity> entitiesInBlastzone = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax));
            ForgeEventFactory.onExplosionDetonate(this.world, this, entitiesInBlastzone, totalSize);
            final Vec3d explosionPosition = new Vec3d(this.x, this.y, this.z);
            
            for (Entity entity : entitiesInBlastzone) {
                
                if (this.canAffectEntity(entity)) {
                    
                    final double distance = entity.getDistance(this.x, this.y, this.z) / totalSize;
                    this.affectEntity(entity, explosionPosition, distance, totalSize);
                }
            }
        }
    }
    
    @Override
    public void doExplosionB (boolean spawnParticles) {
        
        this.playExplosionSound();
        this.spawnExplosionParticle();
        
        for (final BlockPos blockpos : this.getAffectedBlockPositions()) {
            
            final IBlockState state = this.world.getBlockState(blockpos);
            final Block block = state.getBlock();
            
            if (spawnParticles) {
                
                this.spawnBlockExplosionParticles(blockpos);
            }
            
            if (state.getMaterial() != Material.AIR) {
                
                this.destroyBlock(block, state, blockpos);
            }
            
            else if (this.doesCauseFire() && this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBlockState(blockpos.down()).isFullBlock() && this.random.nextInt(3) == 0) {
                
                this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }
    
    public void destroyBlock (Block block, IBlockState state, BlockPos pos) {
        
        if (this.doesDestroyBlocks()) {
            
            if (block.canDropFromExplosion(this)) {
                
                block.dropBlockAsItemWithChance(this.world, pos, this.world.getBlockState(pos), 1.0F / this.size, 0);
            }
            
            block.onBlockExploded(this.world, pos, this);
        }
    }
    
    public void playExplosionSound () {
        
        this.world.playSound((EntityPlayer) null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
    }
    
    public void spawnExplosionParticle () {
        
        final EnumParticleTypes particle = this.size >= 2.0F && this.damagesTerrain ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE;
        this.world.spawnParticle(particle, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
    }
    
    public void spawnBlockExplosionParticles (BlockPos pos) {
        
        final double particleX = pos.getX() + this.world.rand.nextFloat();
        final double particleY = pos.getY() + this.world.rand.nextFloat();
        final double particleZ = pos.getZ() + this.world.rand.nextFloat();
        
        double velocityX = -this.x;
        double velocityY = -this.y;
        double velocityZ = -this.z;
        
        final double d6 = MathHelper.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        
        velocityX /= d6;
        velocityY /= d6;
        velocityZ /= d6;
        
        final double d7 = 0.5D / (d6 / this.size + 0.1D) * (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
        
        velocityX *= d7;
        velocityY *= d7;
        velocityZ *= d7;
        
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (particleX + this.x) / 2.0D, (particleY + this.y) / 2.0D, (particleZ + this.z) / 2.0D, velocityX, velocityY, velocityZ);
        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, velocityX, velocityY, velocityZ);
    }
    
    public boolean doesCauseFire () {
        
        return this.causesFire;
    }
    
    public boolean doesDestroyBlocks () {
        
        return this.damagesTerrain;
    }
    
    public Random getRandom () {
        
        return this.random;
    }
    
    public World getWorld () {
        
        return this.world;
    }
    
    public float getSize () {
        
        return this.size;
    }
    
    public double getX () {
        
        return this.x;
    }
    
    public double getY () {
        
        return this.y;
    }
    
    public double getZ () {
        
        return this.z;
    }
    
    public boolean affectsEntities () {
        
        return true;
    }
    
    public boolean canAffectEntity (Entity entity) {
        
        return !entity.isImmuneToExplosions();
    }
    
    public void affectEntity (Entity entity, Vec3d position, double distanceFrom, float size) {
        
        if (distanceFrom <= 1.0D) {
            double d5 = entity.posX - this.x;
            double d7 = entity.posY + entity.getEyeHeight() - this.y;
            double d9 = entity.posZ - this.z;
            final double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
            
            if (d13 != 0.0D) {
                d5 = d5 / d13;
                d7 = d7 / d13;
                d9 = d9 / d13;
                final double d14 = this.world.getBlockDensity(position, entity.getEntityBoundingBox());
                final double d10 = (1.0D - distanceFrom) * d14;
                entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (int) ((d10 * d10 + d10) / 2.0D * 7.0D * size + 1.0D));
                double d11 = d10;
                
                if (entity instanceof EntityLivingBase) {
                    d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                }
                
                entity.motionX += d5 * d11;
                entity.motionY += d7 * d11;
                entity.motionZ += d9 * d11;
                
                if (entity instanceof EntityPlayer) {
                    final EntityPlayer entityplayer = (EntityPlayer) entity;
                    
                    if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                        this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                    }
                }
            }
        }
    }
}