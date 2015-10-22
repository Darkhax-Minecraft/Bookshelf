package net.darkhax.bookshelf.common;

public class ProxyCommon {
    
    /**
     * A sided version of the preInit event. This method allows for code to be executed
     * exclusively on the client or server side during the pre-initialization phase.
     */
    public void preInit () {
        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://github.com/Darkhax-Minecraft/Bookshelf/raw/version/versions.json");
    }
    
    /**
     * A sided version of the init event. This method allows for code to be executed
     * exclusively on the client or server side during the initialization phase.
     */
    public void init () {
    
    }
    
    /**
     * A sided version of the postInit event. This method allows for code to be executed
     * exclusively on the client or server side during the post-initialization phase.
     */
    public void postInit () {
    
    }
}
