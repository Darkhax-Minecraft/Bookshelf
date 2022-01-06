package net.darkhax.bookshelf.impl.registry;

import net.darkhax.bookshelf.api.registry.IRegistryHelperFactory;
import net.darkhax.bookshelf.api.registry.RegistryHelper;

public class RegistryHelperFactoryForge implements IRegistryHelperFactory {

    @Override
    public RegistryHelper create(String modid) {

        return new RegistryHelperForge(modid);
    }
}