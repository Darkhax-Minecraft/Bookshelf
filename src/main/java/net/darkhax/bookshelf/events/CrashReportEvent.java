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