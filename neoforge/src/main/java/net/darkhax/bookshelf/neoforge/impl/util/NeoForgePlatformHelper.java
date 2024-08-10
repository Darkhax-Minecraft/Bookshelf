package net.darkhax.bookshelf.neoforge.impl.util;

import net.darkhax.bookshelf.common.api.lib.PhysicalSide;
import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.gametest.GameTestHooks;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getModsPath() {
        return FMLPaths.MODSDIR.get();
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

    @Nullable
    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(mod -> mod.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public boolean isTestingEnvironment() {
        return GameTestHooks.isGametestEnabled();
    }

    @Override
    public String getName() {
        return "NeoForge";
    }
}