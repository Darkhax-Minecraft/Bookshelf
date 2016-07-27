package net.darkhax.bookshelf.features.supporters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.features.Feature;
import net.darkhax.bookshelf.handler.SupporterHandler;
import net.darkhax.bookshelf.handler.SupporterHandler.SupporterData;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FeatureSupporters extends Feature {
    
    /**
     * A Thread pool to handle certain player specific texture requests.
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    
    public FeatureSupporters() {
        
        SupporterHandler.readSupporterData();
    }
    
    @Override
    public void setupRendering () {
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void entityJoinWorld (EntityJoinWorldEvent event) {
        
        if (event.getEntity() instanceof AbstractClientPlayer) {
            
            final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
            final SupporterData data = SupporterHandler.getSupporterData(player);
            
            if (data != null)
                makePlayerFancy(player, data.getCapeTexture(), data.getElytraTexture());
        }
    }
    
    /**
     * Attempts to make the player super fancy. Will try to apply the cape texture and the
     * elytra texture that is passed. This method is used internally and should not be
     * considered part of the public API.
     * 
     * @param player The player to make fancy.
     * @param cape The cape texture to set.
     * @param elytra The elytra texture to set.
     */
    @SideOnly(Side.CLIENT)
    private static void makePlayerFancy (final AbstractClientPlayer player, final ResourceLocation cape, final ResourceLocation elytra) {
        
        THREAD_POOL.submit(new Runnable() {
            
            @Override
            public void run () {
                
                try {
                    
                    Thread.sleep(100);
                }
                catch (final InterruptedException e) {
                    
                    return;
                }
                
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    
                    @Override
                    public void run () {
                        
                        RenderUtils.setPlayerTexture(Type.CAPE, player, cape);
                        RenderUtils.setPlayerTexture(Type.ELYTRA, player, elytra);
                    }
                });
            }
        });
    }
}
