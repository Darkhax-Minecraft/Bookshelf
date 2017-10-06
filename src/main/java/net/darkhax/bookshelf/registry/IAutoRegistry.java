/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.registry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface is used to define an automatic registry system for Bookshelf's
 * RegistryHelper.
 */
public interface IAutoRegistry {

    /**
     * Gets the registry helper that this auto registry is linked to.
     *
     * @return
     */
    RegistryHelper getHelper ();

    /**
     * Called when Bookshelf enters the init phase.
     */
    void init ();

    /**
     * Called when Bookshelf enters the postInit phase.
     */
    void postInit ();

    /**
     * Called when Bookshelf enters the client init phase.
     */
    @SideOnly(Side.CLIENT)
    void clientInit ();

    /**
     * Called when Bookshelf enters the client postInit phase.
     */
    @SideOnly(Side.CLIENT)
    void clientPostInit ();
}
