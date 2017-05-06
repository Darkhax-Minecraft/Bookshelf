/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
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
