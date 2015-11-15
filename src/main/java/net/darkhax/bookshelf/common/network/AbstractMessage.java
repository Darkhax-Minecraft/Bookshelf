package net.darkhax.bookshelf.common.network;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.lib.util.PlayerUtils;

public abstract class AbstractMessage<M extends AbstractMessage> implements IMessage, IMessageHandler<M, IMessage> {
    
    @Override
    public IMessage onMessage (M message, MessageContext ctx) {
        
        if (ctx.side.isClient())
            handleClientMessage(message, PlayerUtils.getClientPlayer());
            
        else
            handleServerMessage(message, ctx.getServerHandler().playerEntity);
            
        return null;
    }
    
    /**
     * Called when the message is received on the client side. Only use fields from the
     * provided message instance. Fields from the current class may not be reliable.
     * 
     * @param message: An instance of the message that has been sent to the client.
     * @param player: An instance of the client side player, taken from the current Minecraft
     *            instance.
     */
    @SideOnly(Side.CLIENT)
    public abstract void handleClientMessage (M message, EntityPlayer player);
    
    /**
     * Called when the message is received on the server side. Only use fields from the
     * provided message instance. Fields from the current class may not be reliable.
     * 
     * @param message: An instance of the message that has been sent to the server. This can be
     *            a dedicated server, or an integrated server.
     * @param player: An instance of the player who sent the message to the server. Taken from
     *            ctx.getServerHandler().playerEntity.
     */
    public abstract void handleServerMessage (M message, EntityPlayer player);
}
