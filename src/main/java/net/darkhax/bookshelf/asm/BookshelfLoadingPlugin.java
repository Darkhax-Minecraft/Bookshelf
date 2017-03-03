package net.darkhax.bookshelf.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.darkhax.bookshelf.asm")
@IFMLLoadingPlugin.MCVersion("1.10.2")
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