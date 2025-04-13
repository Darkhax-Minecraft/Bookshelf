package net.darkhax.bookshelf.common.api.util;

import net.minecraft.world.level.Level;

/**
 * While the current tick rate is synced between the client and server, some things like tile entities continue to tick
 * at the base 20tps on the client. This tick accumulator allows ticks to be accumulates as normal the server, but will
 * scale client ticks to roughly the correct amount.
 */
public class TickAccumulator {

    private final float defaultValue;
    private float ticks;

    /**
     * Creates a new tick accumulator.
     *
     * @param defaultValue The amount to reset to when {@link TickAccumulator#reset()} is used.
     */
    public TickAccumulator(float defaultValue) {
        this.ticks = defaultValue;
        this.defaultValue = defaultValue;
    }

    /**
     * Ticks the accumulator up by one tick.
     *
     * @param level The current game level.
     */
    public void tickUp(Level level) {
        this.tick(level.isClientSide ? level.tickRateManager().tickrate() / 20f : 1f);
    }

    /**
     * Ticks the accumulator down by one tick.
     *
     * @param level The current game level.
     */
    public void tickDown(Level level) {
        this.tick(-(level.isClientSide ? level.tickRateManager().tickrate() / 20f : 1f));
    }

    /**
     * Adds an amount of ticks to the accumulator.
     *
     * @param amount The amount of ticks to add.
     */
    public void tick(float amount) {
        this.ticks += amount;
    }

    /**
     * Get the current amount of ticks.
     *
     * @return The current amount of ticks.
     */
    public float getTicks() {
        return this.ticks;
    }

    /**
     * Sets the current amount of ticks.
     *
     * @param ticks The new tick count.
     */
    public void setTicks(float ticks) {
        this.ticks = ticks;
    }

    /**
     * Resets the tick accumulator.
     */
    public void reset() {
        this.ticks = this.defaultValue;
    }
}