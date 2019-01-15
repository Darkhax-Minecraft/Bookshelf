/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be added to any class with a configurable property. This is used as a
 * homing beacon to find annotations on the fields in the class you add this to.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {
    
    /**
     * The name/id for the config. This is used to create the file name for the config, and all
     * configurable fields in the class will be added to the file with this name. I highly
     * recommend using the modid for this, and only having one config file per mod.
     *
     * @return The name for the config file this is associated with.
     */
    String name();
}