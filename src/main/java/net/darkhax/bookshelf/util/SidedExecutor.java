/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;

/**
 * This class contains utilities for running different code based on the current logical side.
 * Note that this is different from the DistExecutor which looks at the physical side the code
 * is running on.
 */
public final class SidedExecutor {

    /**
     * Invokes a Callable if this is the current logical side.
     *
     * @param <T> The desired return type.
     * @param side The side to run on.
     * @param toRun The code to run.
     * @return The return value. May be null if ran on the wrong side or if the value can not
     *         be retrieved.
     */
    @Nullable
    public static <T> T callOnSide (LogicalSide side, Supplier<Callable<T>> toRun) {

        return side.isClient() ? callForSide(toRun, null) : callForSide(null, toRun);
    }

    /**
     * Invokes a different Callable depending on the current logical side.
     *
     * @param <T> The desired return type.
     * @param client The client side code to run.
     * @param server The server side code to run.
     * @return The return value. May be null if no value can be returned.
     */
    @Nullable
    public static <T> T callForSide (@Nullable Supplier<Callable<T>> client, @Nullable Supplier<Callable<T>> server) {

        try {

            if (EffectiveSide.get().isClient() && client != null) {

                return client.get().call();
            }

            else if (EffectiveSide.get().isServer() && server != null) {

                return server.get().call();
            }
        }

        catch (final Exception e) {

            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Invokes a runnable on a given side.
     *
     * @param side The side to run on.
     * @param toRun The code to run.
     */
    public static void runOnSide (LogicalSide side, Supplier<Runnable> toRun) {

        if (side.isClient()) {

            runForSide(toRun, null);
        }

        else if (side.isServer()) {

            runForSide(null, toRun);
        }
    }

    /**
     * Invokes a different runnable based on the current logical side.
     *
     * @param client The client side code to run.
     * @param server The server side code to run.
     */
    public static void runForSide (@Nullable Supplier<Runnable> client, @Nullable Supplier<Runnable> server) {

        try {

            if (EffectiveSide.get().isClient() && client != null) {

                client.get().run();
            }

            else if (EffectiveSide.get().isServer() && server != null) {

                server.get().run();
            }
        }

        catch (final Exception e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Invokes a runnable only when executed on the client. Makes no attempt to safeguard class
     * loading.
     *
     * @param toRun The code to run.
     */
    public static void runOnClient (Runnable toRun) {

        runOnSide(LogicalSide.CLIENT, () -> toRun);
    }

    /**
     * Invokes a runnable only when executed on the server. Makes no attempt to safeguard class
     * loading.
     *
     * @param toRun The code to run.
     */
    public static void runOnServer (Runnable toRun) {

        runOnSide(LogicalSide.SERVER, () -> toRun);
    }
}