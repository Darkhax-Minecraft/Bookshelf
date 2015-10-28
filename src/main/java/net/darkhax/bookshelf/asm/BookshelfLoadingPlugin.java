package net.darkhax.bookshelf.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.darkhax.bookshelf.lib.Constants;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.darkhax.bookshelf.asm")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class BookshelfLoadingPlugin implements IFMLLoadingPlugin {
    
    @Override
    public String[] getASMTransformerClass () {
        
        ASMHelper.isASMEnabled = true;
        Constants.LOG.info("Starting to apply transformations");
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
        
        ASMHelper.isSrg = (Boolean) data.get("runtimeDeobfuscationEnabled");
        new Mappings();
    }
    
    @Override
    public String getAccessTransformerClass () {
        
        return null;
    }
}