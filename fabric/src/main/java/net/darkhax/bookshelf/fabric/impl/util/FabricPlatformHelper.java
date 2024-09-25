package net.darkhax.bookshelf.fabric.impl.util;

import net.darkhax.bookshelf.common.api.ModEntry;
import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.register.MenuRegister;
import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.gametest.FabricGameTestHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FabricPlatformHelper implements IPlatformHelper {

    private static final Supplier<Set<ModEntry>> LOADED_MODS = CachedSupplier.cache(() -> FabricLoader.getInstance().getAllMods().stream().map(mod -> {
        final ModMetadata meta = mod.getMetadata();
        return new ModEntry(meta.getId(), meta.getName(), meta.getDescription(), meta.getVersion().getFriendlyString());
    }).collect(Collectors.toSet()));

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

    @Override
    public Set<ModEntry> getLoadedMods() {
        return LOADED_MODS.get();
    }

    @Override
    public boolean isTestingEnvironment() {
        return FabricGameTestHelper.ENABLED;
    }

    @Override
    public String getName() {
        return "Fabric";
    }

    @Override
    public <T extends AbstractContainerMenu> void unsafeRegisterMenu(ResourceLocation id, MenuRegister.ClientMenuFactory<T> clientFactory) {
        Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(clientFactory::create, FeatureFlags.VANILLA_SET));
    }
}