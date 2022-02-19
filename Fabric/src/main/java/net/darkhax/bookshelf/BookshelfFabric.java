package net.darkhax.bookshelf;

import net.fabricmc.api.ModInitializer;

public class BookshelfFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        new BookshelfCommon();
    }
}
