/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHelper {
    
    /**
     * A reference to the packet channel used by the mod.
     */
    private final SimpleChannel channel;
    
    /**
     * The next ID that will be assigned to a packet. This is used and incremented in
     * {@link #registerMessage(Class, BiConsumer, Function, BiConsumer)}.
     */
    private int nextPacketId = 0;
    
    /**
     * Creates a new network helper. This will let you send and receive packets for your mod.
     *
     * @param channelName The name of your channel. This is turned into a ResourceLocation and
     *        should use your modid for the namespace.
     * @param protocolVersion The version of your network protocol. This must be the same on
     *        the client and server for players to be able to connect.
     */
    public NetworkHelper(String channelName, String protocolVersion) {
        
        this(new ResourceLocation(channelName), () -> protocolVersion, protocolVersion::equals, protocolVersion::equals);
    }
    
    /**
     * Creates a new network helper. This will let you send and receive packets for your mod.
     *
     * @param channelName An identifier for your packet channel.
     * @param protocolVersion The version of your network protocol. This must be the same on
     *        the client and server for players to be able to connect.
     */
    public NetworkHelper(ResourceLocation channelName, String protocolVersion) {
        
        this(channelName, () -> protocolVersion, protocolVersion::equals, protocolVersion::equals);
    }
    
    /**
     * Creates a new network helper. This will let you send and receive packets for your mod.
     *
     * @param channelName An identifier for your packet channel.
     * @param protocolVersion The protocol version string for your packet.
     * @param clientValidator A predicate for validating the client protocol.
     * @param serverValidator A predicate for validating the server protocol.
     */
    public NetworkHelper(ResourceLocation channelName, Supplier<String> protocolVersion, Predicate<String> clientValidator, Predicate<String> serverValidator) {
        
        this.channel = NetworkRegistry.newSimpleChannel(channelName, protocolVersion, clientValidator, serverValidator);
    }
    
    /**
     * Registers a new packet message type with the network channel. This type of packet
     * message will be automatically enqueued back onto the main thread of the game when it is
     * handled.
     *
     * @param <T> The Java type of the message being registered.
     * @param messageType The class of the message being registered.
     * @param encoder A consumer that will write your packet data to a packet buffer.
     * @param decoder A function that will read your packet data from a packet buffer.
     * @param messageConsumer A consumer that is applied when your packet is received and
     *        allows your code to respond.
     */
    public <T> void registerEnqueuedMessage (Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<Context>> messageConsumer) {
        
        this.registerMessage(messageType, encoder, decoder, (message, context) -> context.get().enqueueWork( () -> {
            messageConsumer.accept(message, context);
            context.get().setPacketHandled(true);
        }));
    }
    
    /**
     * Registers a new packet message type with the network channel.
     *
     * @param <T> The Java type of the message being registered.
     * @param messageType The class of the message being registered.
     * @param encoder A consumer that will write your packet data to a packet buffer.
     * @param decoder A function that will read your packet data from a packet buffer.
     * @param messageConsumer A consumer that is applied when your packet is received and
     *        allows your code to respond.
     */
    public <T> void registerMessage (Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<Context>> messageConsumer) {
        
        this.channel.registerMessage(this.nextPacketId, messageType, encoder, decoder, messageConsumer);
        this.nextPacketId++;
    }
    
    /**
     * Sends a packet message to the server. While this can technically be called on the
     * server, it is intended to only be invoked on the client.
     *
     * @param message The message object.
     */
    public void sendToServer (Object message) {
        
        this.channel.sendToServer(message);
    }
    
    /**
     * Sends a packet message over the channel.
     *
     * @param target The target of the message.
     * @param message The message to send.
     */
    public void send (PacketTarget target, Object message) {
        
        this.channel.send(target, message);
    }
    
    /**
     * Sends a packet message to a specific player.
     *
     * @param player The player to send the message to.
     * @param message The message to send.
     */
    public void sendToPlayer (ServerPlayerEntity player, Object message) {
        
        this.send(PacketDistributor.PLAYER.with( () -> player), message);
    }
    

    /**
     * Sends a packet message to all players near a certain position.
     *
     * @param point The point of origin.
     * @param message The message to send.
     */
    public void sendToNearbyPlayers (TargetPoint point, Object message) {
        
        this.send(PacketDistributor.NEAR.with( () -> point), message);
    }
    
    /**
     * Sends a packet message to all players on a server.
     *
     * @param message The message to send.
     */
    public void sendToAllPlayers (Object message) {
        
        this.send(PacketDistributor.ALL.noArg(), message);
    }
    
    /**
     * Sends a message to all players who are tracking a given chunk.
     *
     * @param chunk The targeted chunk.
     * @param message The message to send.
     */
    public void sendToChunk (Chunk chunk, Object message) {
        
        this.send(PacketDistributor.TRACKING_CHUNK.with( () -> chunk), message);
    }
}