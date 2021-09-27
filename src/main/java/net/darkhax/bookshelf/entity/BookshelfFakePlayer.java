package net.darkhax.bookshelf.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.potion.EffectInstance;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

public class BookshelfFakePlayer extends FakePlayer {
    
    public BookshelfFakePlayer(ServerWorld world, GameProfile name) {
        
        super(world, name);
        this.abilities.invulnerable = true;
    }
    
    @Override
    public boolean canBeAffected (EffectInstance effectToTest) {
        
        return false;
    }
}