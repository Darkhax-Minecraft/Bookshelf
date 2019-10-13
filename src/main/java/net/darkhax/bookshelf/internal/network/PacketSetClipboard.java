package net.darkhax.bookshelf.internal.network;

import java.util.function.Supplier;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSetClipboard {
    
    private final String clipboardText;
    
    public PacketSetClipboard(String text) {
        
        this.clipboardText = text;
    }
    
    public static void encode (PacketSetClipboard packet, PacketBuffer buffer) {
        
        buffer.writeString(packet.clipboardText);
    }
    
    public static PacketSetClipboard decode (PacketBuffer buffer) {
        
        return new PacketSetClipboard(buffer.readString());
    }
    
    public static void handle (PacketSetClipboard packet, Supplier<Context> context) {
        
        Bookshelf.SIDED.setClipboard(packet.clipboardText);
    }
}
