package net.darkhax.bookshelf.asm;

import java.util.Map;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.darkhax.bookshelf.asm")
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class BookshelfLoadingPlugin implements IFMLLoadingPlugin {
    
    @Override
    public String[] getASMTransformerClass () {
        
        ASMUtils.isASMEnabled = true;
        ASMConfig.loadConfigOptions();
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
        
        ASMUtils.isSrg = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }
    
    @Override
    public String getAccessTransformerClass () {
        
        return null;
    }
}