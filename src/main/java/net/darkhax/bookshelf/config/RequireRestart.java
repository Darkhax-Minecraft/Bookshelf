package net.darkhax.bookshelf.config;

/**
 * This annotation is not yet implemented. I suggest using it regardless to future proof your
 * mods. It will allow you to specify if the config field requires a certain type of restart to
 * be changed.
 */
public @interface RequireRestart {

    /**
     * Checks if the entry requires a game restart to take effect.
     *
     * @return Whether or not the entry requires a game restart to take effect.
     */
    boolean game() default false;

    /**
     * Checks if the entry requires a world restart to take effect.
     *
     * @return Whether or not the entry requires a world restart to take effect.
     */
    boolean world() default false;
}
