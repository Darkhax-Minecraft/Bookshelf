package net.darkhax.bookshelf.internal;

public class BookshelfServer implements ISidedProxy {

    @Override
    public void setClipboard (String text) {

        throw new IllegalStateException("Can not set the clipboard contents on a server!");
    }
}