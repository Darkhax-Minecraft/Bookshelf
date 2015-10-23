package net.darkhax.bookshelf.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * {@link cpw.mods.fml.common.network.simpleimpl.IMessage} and {@link cpw.mods.fml.common.network.simpleimpl.IMessageHandler} implemented in the same class.
 *
 * @param <M>     The message to receive data from. This should be the same class. Make sure to use this, otherwise you can't access your variables in the message!
 */
public abstract class AbstractMessage<M extends AbstractMessage> implements IMessage, IMessageHandler<M, IMessage> {

    public IMessage onMessage(M message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            handleClientMessage(message, Bookshelf.proxy.getClientPlayer());
        } else {
            handleServerMessage(message, ctx.getServerHandler().playerEntity);
        }

        return null;
    }

    /**
     * Executes when message received on CLIENT side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message       The message instance with all variables.
     * @param player        The client player entity.
     */
    @SideOnly(Side.CLIENT)
    public abstract void handleClientMessage(M message, EntityPlayer player);

    /**
     * Executes when message received on SERVER side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message       The message instance with all variables.
     * @param player        The player who sent the message to the server.
     */
    @SideOnly(Side.SERVER)
    public abstract void handleServerMessage(M message, EntityPlayer player);
}
