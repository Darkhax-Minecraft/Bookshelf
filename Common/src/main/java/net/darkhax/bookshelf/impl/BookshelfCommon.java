package net.darkhax.bookshelf.impl;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.impl.fixes.MC151457;

public class BookshelfCommon {

    public BookshelfCommon() {

        Services.REGISTRIES.loadContent(new BookshelfContentProvider());
        MC151457.applyFix();
    }
}