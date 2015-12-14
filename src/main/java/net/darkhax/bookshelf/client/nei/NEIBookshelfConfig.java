package net.darkhax.bookshelf.client.nei;

import net.darkhax.bookshelf.lib.Constants;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIBookshelfConfig implements IConfigureNEI {
    
    @Override
    public String getName () {
        
        return Constants.MOD_NAME;
    }
    
    @Override
    public String getVersion () {
        
        return Constants.VERSION;
    }
    
    @Override
    public void loadConfig () {
        
        DescriptionHandler handler = new DescriptionHandler(null);
        API.registerUsageHandler(handler);
        API.registerRecipeHandler(handler);
    }
}
