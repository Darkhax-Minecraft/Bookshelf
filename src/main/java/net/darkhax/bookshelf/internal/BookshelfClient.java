package net.darkhax.bookshelf.internal;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;

public class BookshelfClient extends BookshelfServer {
    
    public BookshelfClient() {
        
        super();
        MinecraftForge.EVENT_BUS.addListener(this::onRecipesUpdated);
    }
    
    @Override
    public void setClipboard (String text) {
        
        Minecraft.getInstance().keyboardListener.setClipboardString(text);
    }
    
    private void onRecipesUpdated (RecipesUpdatedEvent event) {
        
        this.recipeManager = event.getRecipeManager();
    }
}