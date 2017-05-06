/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CrashReportEvent extends Event {

    /**
     * The message to send to the
     */
    private String message;

    public CrashReportEvent () {

        this.setMessage("");
    }

    public String getMessage () {

        return this.message;
    }

    public void setMessage (String message) {

        this.message = message;
    }
}