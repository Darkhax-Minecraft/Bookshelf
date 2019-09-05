package net.darkhax.bookshelf.internal.network;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.function.Supplier;

import javax.rmi.CORBA.Util;

import com.electronwill.nightconfig.core.io.Utils;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.internal.BookshelfClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSetClipboard {
    
    private String clipboardText;
    
    public PacketSetClipboard(String text) {
        
        this.clipboardText = text;
    }
    
    public static void encode(PacketSetClipboard packet, PacketBuffer buffer) {
        
        buffer.writeString(packet.clipboardText);
    }
    
    public static PacketSetClipboard decode(PacketBuffer buffer) {
        
        return new PacketSetClipboard(buffer.readString());
    }
    
    public static void handle(PacketSetClipboard packet, Supplier<Context> context) {
        
        Bookshelf.SIDED.setClipboard(packet.clipboardText);
    }
}
