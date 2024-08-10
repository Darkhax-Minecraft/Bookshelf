package net.darkhax.bookshelf.forge.impl.util;

import net.darkhax.bookshelf.common.api.lib.PhysicalSide;
import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.gametest.ForgeGameTestHooks;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {

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
        return ForgeGameTestHooks.isGametestEnabled();
    }

    @Override
    public String getName() {
        return "Forge";
    }
}