package net.darkhax.bookshelf.client;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
    
    @SubscribeEvent
    public void onEntityRender (RenderLivingEvent.Pre event) {
    
    }
    
    @SubscribeEvent
    public void onPlayerRender (RenderPlayerEvent.Pre event) {
    
    }
}
