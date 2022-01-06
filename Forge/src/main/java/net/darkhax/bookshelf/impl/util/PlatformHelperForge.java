package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.PhysicalSide;
import net.darkhax.bookshelf.api.util.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PlatformHelperForge implements IPlatformHelper {

    @Override
    public Path getGamePath() {

        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigPath() {

        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public PhysicalSide getPhysicalSide() {

        return FMLEnvironment.dist.isClient() ? PhysicalSide.CLIENT : PhysicalSide.SERVER;
    }
}