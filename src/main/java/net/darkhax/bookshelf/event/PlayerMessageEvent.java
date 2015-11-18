package net.darkhax.bookshelf.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.event.entity.player.PlayerEvent;

import cpw.mods.fml.common.eventhandler.Cancelable;

public class PlayerMessageEvent extends PlayerEvent {
    
    /**
     * The message to print into the chat.
     */
    public IChatComponent message;
    
    /**
     * A flag used to determine whether this is a join message, or a leave message.
     */
    public boolean isJoinedMessage;
    
    /**
     * An event that is fired whenever a join or leave message would be create. This event
     * allows for the contents of that message to be altered.
     * 
     * @param message: A ChatComponent which represents the original join/leave message.
     * @param isJoinedMessage: If true, this is a join message. If false, this is a leave
     *            message.
     */
    public PlayerMessageEvent(EntityPlayer player, IChatComponent message, boolean isJoinedMessage) {
        
        super(player);
        this.message = message;
        this.isJoinedMessage = isJoinedMessage;
    }
}