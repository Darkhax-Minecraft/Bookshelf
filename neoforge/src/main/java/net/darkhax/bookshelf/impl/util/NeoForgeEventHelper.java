package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.function.QuadConsumer;
import net.darkhax.bookshelf.api.function.TriConsumer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.BiConsumer;

public class NeoForgeEventHelper {

    public static <T extends Event, CTX> void addContextListener(Class<T> eventType, CTX ctx, BiConsumer<T, CTX> listener) {

        addContextListener(eventType, EventPriority.NORMAL, false, ctx, listener);
    }

    public static <T extends Event, CTX> void addContextListener(Class<T> eventType, EventPriority priority, boolean receiveCancelled, CTX ctx, BiConsumer<T, CTX> listener) {

        NeoForge.EVENT_BUS.addListener(priority, receiveCancelled, eventType, (e) -> listener.accept(e, ctx));
    }

    public static <T extends Event, CTX, CTX2> void addContextListener(Class<T> eventType, CTX ctx, CTX2 ctx2, TriConsumer<T, CTX, CTX2> listener) {

        addContextListener(eventType, EventPriority.NORMAL, false, ctx, ctx2, listener);
    }

    public static <T extends Event, CTX, CTX2> void addContextListener(Class<T> eventType, EventPriority priority, boolean receiveCancelled, CTX ctx, CTX2 ctx2, TriConsumer<T, CTX, CTX2> listener) {

        NeoForge.EVENT_BUS.addListener(priority, receiveCancelled, eventType, (e) -> listener.accept(e, ctx, ctx2));
    }

    public static <T extends Event, CTX, CTX2, CTX3> void addContextListener(Class<T> eventType, CTX ctx, CTX2 ctx2, CTX3 ctx3, QuadConsumer<T, CTX, CTX2, CTX3> listener) {

        addContextListener(eventType, EventPriority.NORMAL, false, ctx, ctx2, ctx3, listener);
    }

    public static <T extends Event, CTX, CTX2, CTX3> void addContextListener(Class<T> eventType, EventPriority priority, boolean receiveCancelled, CTX ctx, CTX2 ctx2, CTX3 ctx3, QuadConsumer<T, CTX, CTX2, CTX3> listener) {

        NeoForge.EVENT_BUS.addListener(priority, receiveCancelled, eventType, (e) -> listener.accept(e, ctx, ctx2, ctx3));
    }
}
