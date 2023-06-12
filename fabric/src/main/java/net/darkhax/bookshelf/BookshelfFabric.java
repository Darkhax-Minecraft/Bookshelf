package net.darkhax.bookshelf;

import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.fabricmc.api.ModInitializer;

public class BookshelfFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        new BookshelfCommon();
    }
}
