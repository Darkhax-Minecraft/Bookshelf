package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.PhysicalSide;
import net.darkhax.bookshelf.api.util.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.gametest.FabricGameTestHelper;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class PlatformHelperFabric implements IPlatformHelper {

    @Override
    public Path getGamePath() {

        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigPath() {

        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Path getModsPath() {

        return this.getGamePath().resolve("mods");
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public PhysicalSide getPhysicalSide() {

        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? PhysicalSide.CLIENT : PhysicalSide.SERVER;
    }

    @Nullable
    @Override
    public String getModName(String modId) {

        return FabricLoader.getInstance().getModContainer(modId).map(mod -> mod.getMetadata().getName()).orElse(modId);
    }

    @Override
    public boolean isTestingEnvironment() {

        return FabricGameTestHelper.ENABLED;
    }

    @Override
    public String getName() {

        return "Fabric";
    }
}