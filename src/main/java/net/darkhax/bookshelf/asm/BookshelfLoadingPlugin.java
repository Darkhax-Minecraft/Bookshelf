/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.darkhax.bookshelf.asm")
@IFMLLoadingPlugin.MCVersion("1.11.2")
public class BookshelfLoadingPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass () {

        return new String[] { BookshelfTransformerManager.class.getName() };
    }

    @Override
    public String getModContainerClass () {

        return null;
    }

    @Override
    public String getSetupClass () {

        return null;
    }

    @Override
    public void injectData (Map<String, Object> data) {

        // Don't care about this data.
    }

    @Override
    public String getAccessTransformerClass () {

        return null;
    }
}