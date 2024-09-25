package net.darkhax.bookshelf.common.api.network;

/**
 * Determines where the packet will be resolved.
 */
public enum Destination {

    /**
     * Describes a situation where the server has a payload that will be sent to a client. The payload will be handled
     * on the client and can use code that is only available on a dedicated client.
     */
    SERVER_TO_CLIENT,

    /**
     * Describes a situation where the client has a payload that will be sent to a server. The payload will be handled
     * by the server and can access the game state. Please keep in mind that payloads originating on the client can be
     * forged and should not be trusted without an appropriate level of validation on the server.
     */
    CLIENT_TO_SERVER,

    /**
     * Describes a situation where the packet can originate from and be handled by a client or a server. These packets
     * have the limitations of both SERVER_TOL_CLIENT and CLIENT_TO_SERVER packets.
     */
    BIDIRECTIONAL;

    /**
     * Checks if the packet can be
     *
     * @return
     */


    /**
     * Checks if the payload can be handled on a server.
     *
     * @return If the payload can be handled by a server.
     */
    public boolean handledByServer() {
        return this == CLIENT_TO_SERVER || this == BIDIRECTIONAL;
    }

    /**
     * Checks if the payload can be handled on a client.
     *
     * @return If the payload can be handled by a client.
     */
    public boolean handledByClient() {
        return this == SERVER_TO_CLIENT || this == BIDIRECTIONAL;
    }
}
