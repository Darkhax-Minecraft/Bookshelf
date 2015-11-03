package net.darkhax.bookshelf.lib;

import net.minecraft.world.World;

public enum VanillaParticle {
    
    EXPLOSION_NORMAL("explode"),
    EXPLOSION_LARGE("largeexplode"),
    EXPLOSION_HUGE("hugeexplosion"),
    FIREWORKS_SPARK("fireworksSpark"),
    WATER_BUBBLE("bubble"),
    WATER_SPLASH("splash"),
    WATER_WAKE("wake"),
    SUSPENDED("suspended"),
    SUSPENDED_DEPTH("depthsuspend"),
    CRIT("crit"),
    CRIT_MAGIC("magicCrit"),
    SMOKE_NORMAL("smoke"),
    SMOKE_LARGE("largesmoke"),
    SPELL("spell"),
    SPELL_INSTANT("instantSpell"),
    SPELL_MOB("mobSpell"),
    SPELL_MOB_AMBIENT("mobSpellAmbient"),
    SPELL_WITCH("witchMagic"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    VILLAGER_ANGRY("angryVillager"),
    VILLAGER_HAPPY("happyVillager"),
    TOWN_AURA("townaura"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    CLOUD("cloud"),
    REDSTONE("reddust"),
    SNOWBALL("snowballpoof"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart");
    
    /**
     * The name of the particle. Used in RenderGlobal to render the particle.
     */
    private final String particleName;
    
    /**
     * A basic enumeration to contain all of the particle names from vanilla minecraft.
     * 
     * @param name: The name of the vanilla particle.
     */
    VanillaParticle(String name) {
        
        this.particleName = name;
    }
    
    /**
     * Provides the name of the vanilla particle.
     * 
     * @return String: The name of the vanilla particle.
     */
    public String getParticleName () {
        
        return this.particleName;
    }
    
    /**
     * 
     * @param world
     * @param particle
     * @param x
     * @param y
     * @param z
     * @param velocityX
     * @param velocityY
     * @param velocityZ
     */
    public static void spawnParticle(World world, VanillaParticle particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        
        world.spawnParticle(particle.getParticleName(), x, y, z, velocityX, velocityY, velocityZ);
    }
}