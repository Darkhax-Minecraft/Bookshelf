/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
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
