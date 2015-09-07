package net.darkhax.bookshelf.asm;

import java.util.Map;

import net.darkhax.bookshelf.util.Constants;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.darkhax.bookshelf.asm")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class BookshelfLoadingPlugin implements IFMLLoadingPlugin {
    
    @Override
    public String[] getASMTransformerClass () {
    
        ASMHelper.isASMEnabled = true;
        Constants.LOG.info("Starting to apply transformations");
        return new String[] { EntityHorseTransformer.class.getName() };
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
    
        ASMHelper.isMCP = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }
    
    @Override
    public String getAccessTransformerClass () {
    
        return null;
    }
}