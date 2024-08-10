package net.darkhax.bookshelf.common.api.lib;

/**
 * Represents a physical location in the client/server network diagram.
 */
public enum PhysicalSide {

    /**
     * A physical client. This includes single player, and LAN worlds.
     */
    CLIENT,

    /**
     * A physical server. This includes dedicated servers where client code and logic is not accessible.
     */
    SERVER;

    /**
     * Checks if this is a physical client.
     *
     * @return Returns true when on a physical client.
     */
    public boolean isClient() {

        return this == CLIENT;
    }

    /**
     * Checks if this is a physical server.
     *
     * @return Returns true when on a physical server.
     */
    public boolean isServer() {

        return this == SERVER;
    }
}