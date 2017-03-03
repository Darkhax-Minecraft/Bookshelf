package net.darkhax.bookshelf.lib;

public class BookshelfException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookshelfException (String message, Throwable cause) {

        super(message, cause);
    }

    public BookshelfException (String message) {

        super(message);
    }
}
