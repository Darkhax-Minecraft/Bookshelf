package net.darkhax.bookshelf.common.impl;

import net.darkhax.bookshelf.common.api.service.Services;

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