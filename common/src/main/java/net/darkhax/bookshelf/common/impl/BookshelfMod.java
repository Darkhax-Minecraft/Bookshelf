package net.darkhax.bookshelf.common.impl;

import net.darkhax.bookshelf.common.api.service.Services;

import java.io.IOException;
import java.util.List;

public class BookshelfMod {

    private static BookshelfMod instance;
    private boolean hasInitialized = false;

    public void init() {
        if (hasInitialized) {
            throw new IllegalStateException("The " + Constants.MOD_NAME + " has already been initialized.");
        }

        this.runStartupChecks();

        hasInitialized = true;
    }

    private void runStartupChecks() {
        if (Services.PLATFORM == null) {
            throw new IllegalStateException("Bookshelf services are not available.");
        }
        this.detectInvalidContentProviders();
    }

    @Deprecated
    private void detectInvalidContentProviders() {
        try {
            final List<String> oldProviders = Services.findServices("net.darkhax.bookshelf.common.api.registry.IContentProvider");
            if (!oldProviders.isEmpty()) {
                final String errorMsg = "An outdated implementation of IContentProvider has been found. The game is being stopped for your protection. Please check if an update is available! More information at https://gist.github.com/Darkhax/63356eed0a27848efe8574ce4c677bae";
                Constants.LOG.error(errorMsg);
                for (String provider : oldProviders) {
                    Constants.LOG.error("- {}", provider);
                }
                throw new IllegalStateException(errorMsg + " " + String.join(", ", oldProviders));
            }
        }
        catch (IOException e) {
            Constants.LOG.error("Failed to read services.", e);
        }
    }

    /**
     * Gets the Bookshelf mod instance. If an instance does not already exist one will be created.
     *
     * @return The Bookshelf mod instance.
     */
    public static BookshelfMod getInstance() {
        if (instance == null) {
            instance = new BookshelfMod();
        }
        return instance;
    }
}