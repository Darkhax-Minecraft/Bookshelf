package net.darkhax.bookshelf.forge.impl.util;

import net.darkhax.bookshelf.common.api.ModEntry;
import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.gametest.ForgeGameTestHooks;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ForgePlatformHelper implements IPlatformHelper {

    private static final Supplier<Set<ModEntry>> LOADED_MODS = CachedSupplier.cache(() -> ModList.get().getMods().stream().map(mod -> new ModEntry(mod.getModId(), mod.getDisplayName(), mod.getDescription(), mod.getVersion().toString())).collect(Collectors.toSet()));

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

    @Override
    public Set<ModEntry> getLoadedMods() {
        return LOADED_MODS.get();
    }

    @Override
    public boolean isTestingEnvironment() {
        return ForgeGameTestHooks.isGametestEnabled();
    }

    @Override
    public String getName() {
        return "Forge";
    }

    @Override
    public <T extends AbstractContainerMenu> void unsafeRegisterMenu(ResourceLocation id, MenuRegister.ClientMenuFactory<T> clientFactory) {
        Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(clientFactory::create, FeatureFlags.VANILLA_SET));
    }
}