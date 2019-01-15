package net.darkhax.bookshelf.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionBase extends Explosion {
    
    public ExplosionBase(World world, Entity entity, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        
        super(world, entity, x, y, z, size, flaming, damagesTerrain);
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
}
