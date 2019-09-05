package net.darkhax.bookshelf.internal;

import net.minecraft.client.Minecraft;

public class BookshelfClient implements ISidedProxy {

    @Override
    public void setClipboard (String text) {
        
        Minecraft.getInstance().keyboardListener.setClipboardString(text);
    }  
}