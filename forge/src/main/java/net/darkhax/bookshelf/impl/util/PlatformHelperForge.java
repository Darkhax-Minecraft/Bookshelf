package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.PhysicalSide;
import net.darkhax.bookshelf.api.util.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.gametest.ForgeGameTestHooks;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

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
    public Path getModsPath() {

        return FMLPaths.MODSDIR.get();
    }

    @Override
    public Set<String> getLoadedMods() {
        final Set<String> loadedMods = new HashSet<>();
        ModList.get().getMods().forEach(mod -> loadedMods.add(mod.getModId()));
        return loadedMods;
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

    @Nullable
    @Override
    public String getModVersion(String modId) {

        return ModList.get().getModContainerById(modId).map(mod -> mod.getModInfo().getVersion().toString()).orElse(null);
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