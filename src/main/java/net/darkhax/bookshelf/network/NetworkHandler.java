/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    
    /**
     * The network wrapper instance, created when a new handler is constructed.
     */
    private final SimpleNetworkWrapper network;
    
    /**
     * The current discriminator value. This is ticked up automatically as messages are
     * registered.
     */
    private int curDiscriminator = 0;
    
    /**
     * Constructs a new NetworkHandler, which is basically a wrapper for SimpleNetworkWrapper.
     *
     * @param netId The id for the new network channel. This should probably be your mod id.
     */
    public NetworkHandler (String netId) {
        
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel(netId);
    }
    
    /**
     * Registers a new packet to the network handler.
     *
     * @param clazz The class of the packet. This class must implement IMessage and
     *        IMessageHandler!
     * @param side The side that receives this packet.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void register (Class clazz, Side side) {
        
        this.network.registerMessage(clazz, clazz, this.curDiscriminator++, side);
    }
    
    /**
     * Sends the message to all players.
     *
     * @param message The message to send.
     */
    public void sendToAll (IMessage message) {
        
        this.network.sendToAll(message);
    }
    
    /**
     * Sends the message to a specific player.
     *
     * @param message The message to send.
     * @param player The player to receive the message.
     */
    public void sendTo (IMessage message, EntityPlayerMP player) {
        
        this.network.sendTo(message, player);
    }
    
    /**
     * Sends the message to everyone near a point.
     *
     * @param message The message to send.
     * @param world The world to send the message to.
     * @param pos The position to send the message to.
     * @param range The range of the message.
     */
    public void sendToAllAround (IMessage message, World world, BlockPos pos, double range) {
        
        this.sendToAllAround(message, new TargetPoint(world.provider.getDimension(), pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, range));
    }
    
    /**
     * Sends the message to everyone near a point.
     *
     * @param message The message to send.
     * @param point The point to send the message to.
     */
    public void sendToAllAround (IMessage message, TargetPoint point) {
        
        this.network.sendToAllAround(message, point);
    }
    
    /**
     * Sends the message to everyone in a dimension.
     *
     * @param message The message to send.
     * @param dimensionId The id of the dimension to send the message to.
     */
    public void sendToDimension (IMessage message, int dimensionId) {
        
        this.network.sendToDimension(message, dimensionId);
    }
    
    /**
     * Sends a message to the server from a client.
     *
     * @param message The message to send.
     */
    public void sendToServer (IMessage message) {
        
        this.network.sendToServer(message);
    }
}
