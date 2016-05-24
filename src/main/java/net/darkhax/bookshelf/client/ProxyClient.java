package net.darkhax.bookshelf.client;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.block.BlockWoodenShelf;
import net.darkhax.bookshelf.common.ProxyCommon;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProxyClient extends ProxyCommon {
    
    /**
     * A Thread pool to handle certain player specific texture requests.
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    
    /**
     * A custom cape texture for Darkhax. Do not ask for your own!
     */
    private static final ResourceLocation CAPE_DARKHAX = new ResourceLocation("bookshelf", "textures/entity/player/cape_darkhax.png");
    
    /**
     * A custom Elytra texture for Darkhax. Do not ask for your own!
     */
    private static final ResourceLocation ELYTRA_DARKHAX = new ResourceLocation("bookshelf", "textures/entity/player/elytra_darkhax.png");
    
    /**
     * A custom cape texture for SethG. Do not ask for your own!
     */
    private static final ResourceLocation CAPE_SETH = new ResourceLocation("bookshelf", "textures/entity/player/cape_seth.png");
    
    /**
     * A custom Elytra texture for SethG. Do not ask for your own!
     */
    private static final ResourceLocation ELYTRA_SETH = new ResourceLocation("bookshelf", "textures/entity/player/elytra_seth.png");
    
    /**
     * A custom cape texture for Sycophantasia. Do not ask for your own!
     */
    private static final ResourceLocation CAPE_SYCO = new ResourceLocation("bookshelf", "textures/entity/player/cape_syco.png");
    
    /**
     * A custom Elytra texture for Sycophantasia. Do not ask for your own!
     */
    private static final ResourceLocation ELYTRA_SYCO = new ResourceLocation("bookshelf", "textures/entity/player/elytra_syco.png");
    
    @Override
    public void preInit () {
        
        final Item item = Item.getItemFromBlock(Bookshelf.blockShelf);
        
        for (int meta = 0; meta < 5; meta++)
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("bookshelf:bookshelf_" + BlockWoodenShelf.types[meta], "inventory"));
            
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void entityJoinWorld (EntityJoinWorldEvent event) {
        
        if (event.getEntity() instanceof AbstractClientPlayer) {
            
            final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
            final UUID id = player.getUniqueID();
            
            // Darkhax
            if (id.toString().equals("d183e5a2-a087-462a-963e-c3d7295f9ec5"))
                makePlayerFancy(player, CAPE_DARKHAX, ELYTRA_DARKHAX);
                
            // Syco
            else if (id.toString().equals("eec70f59-1543-4580-b4d8-4954a108866b"))
                makePlayerFancy(player, CAPE_SYCO, ELYTRA_SYCO);
                
            // Seth
            else if (id.toString().equals("90b9f095-ba47-450c-a290-200a5cefe5be"))
                makePlayerFancy(player, CAPE_SETH, ELYTRA_SETH);
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