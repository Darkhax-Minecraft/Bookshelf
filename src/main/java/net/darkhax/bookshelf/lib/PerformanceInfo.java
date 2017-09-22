/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class allows for snapshots of the game's performance to be created.
 */
@SideOnly(Side.CLIENT)
public class PerformanceInfo {

    /**
     * The FPS of the client.
     */
    private final int fps;

    /**
     * The amount of chunk render updates.
     */
    private final int chunkUpdates;

    /**
     * The maximum memory the JVM will attempt to use.
     */
    private final long maxMemory;

    /**
     * The total memory in the JVM.
     */
    private final long totalMemory;

    /**
     * The amount of memory currently free.
     */
    private final long freeMemory;

    /**
     * The amount of memory being used.
     */
    private final long usedMemory;

    /**
     * Constructs a PerformanceInfo snapshot. Once constructed it can be used to get get info about
     * the game at time of construction.
     */
    public PerformanceInfo () {

        this.fps = Minecraft.getDebugFPS();
        this.chunkUpdates = RenderChunk.renderChunksUpdated;

        final Runtime runtime = Runtime.getRuntime();
        this.maxMemory = runtime.maxMemory();
        this.totalMemory = runtime.totalMemory();
        this.freeMemory = runtime.freeMemory();
        this.usedMemory = this.totalMemory - this.freeMemory;
    }

    /**
     * Gets the FPS at time of construction.
     *
     * @return The FPS.
     */
    public int getFps () {

        return this.fps;
    }

    /**
     * Gets the amount of chunk render updates at time of constructuon.
     *
     * @return The amount of chunk render updates.
     */
    public int getChunkUpdates () {

        return this.chunkUpdates;
    }

    /**
     * The maximum amount of memory that the JVM will try to use.
     *
     * @return The maxium memory.
     */
    public long getMaxMemory () {

        return this.maxMemory;
    }

    /**
     * Gets the maximum aount of memory that the JVM will try to use, in megabytes.
     *
     * @return The maximum memory in megabytes.
     */
    public long getMaxMemoryMb () {

        return bytesToMb(this.maxMemory);
    }

    /**
     * Gets the total amount of memory available to the JVM.
     *
     * @return The total amount of memory available.
     */
    public long getTotalMemory () {

        return this.totalMemory;
    }

    /**
     * Gets the total amount of memory available to the JVM in megabytes.
     *
     * @return The total memory in megabytes.
     */
    public long getTotalMemoryMb () {

        return bytesToMb(this.totalMemory);
    }

    /**
     * Gets the total amount of unused memory.
     *
     * @return The amount of unused memory.
     */
    public long getFreeMemory () {

        return this.freeMemory;
    }

    /**
     * Gets the total amount of unused memory in megabytes.
     *
     * @return The amount of unushed memory in megabytes.
     */
    public long getFreeMemoryMb () {

        return bytesToMb(this.totalMemory);
    }

    /**
     * Gets the amount of memory being used.
     *
     * @return The amount of memory used.
     */
    public long getUsedMemory () {

        return this.usedMemory;
    }

    /**
     * Gets the amount of memory being used in megabytes.
     *
     * @return The amount of memory used in megabytes.
     */
    public long getUsedMemoryMb () {

        return bytesToMb(this.usedMemory);
    }

    /**
     * Gets a string which shows the memory being used.
     *
     * @return A string which shows the memory being used.
     */
    public String getMemoryString () {

        return String.format("Mem:% 2d%% %03d/%03dMB", this.getUsedMemory() * 100L / this.getMaxMemory(), this.getUsedMemoryMb(), this.getMaxMemoryMb());
    }

    /**
     * Gets a string which shows the allocated memory.
     *
     * @return A string which shows the allocated memory.
     */
    public String getAllocatedString () {

        return String.format("Allocated:% 2d%% %03dMB", this.getUsedMemory() * 100L / this.getMaxMemory(), this.getUsedMemoryMb());
    }

    /**
     * Simple util to convert bytes to megabytes.
     *
     * @param bytes The amount of bytes.
     * @return The amount of megabytes.
     */
    public static long bytesToMb (long bytes) {

        return bytes / 1024L / 1024L;
    }
}