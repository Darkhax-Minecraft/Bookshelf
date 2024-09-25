package net.darkhax.bookshelf.common.impl.resources;

import net.darkhax.bookshelf.common.api.ModEntry;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.DetectedVersion;
import net.minecraft.client.Minecraft;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExtendedText {

    public static Supplier<ExtendedText> INSTANCE = CachedSupplier.cache(ExtendedText::new);
    private final Map<String, Supplier<String>> extendedEntries = new LinkedHashMap<>();

    private ExtendedText() {

        this.register("java.version", () -> getProperty("java.version"));
        this.register("minecraft.version", DetectedVersion.BUILT_IN::getName);
        this.register("loader.name", Services.PLATFORM::getName);
        this.register("player.name", () -> Minecraft.getInstance().getUser().getName());

        for (ModEntry mod : Services.PLATFORM.getLoadedMods()) {
            this.register("mods." + mod.modId() + ".name", mod::name);
            this.register("mods." + mod.modId() + ".desc", mod::description);
            this.register("mods." + mod.modId() + ".id", mod::modId);
            this.register("mods." + mod.modId() + ".version", mod::version);
        }
    }

    public boolean has(String key) {
        return this.extendedEntries.containsKey(key);
    }

    public String get(String key) {
        return this.extendedEntries.get(key).get();
    }

    private void register(String key, Supplier<String> value) {
        extendedEntries.put("text.bookshelf.ext." + key, CachedSupplier.cache(value));
    }

    private static String getProperty(String propertyName) {
        try {
            return System.getProperty(propertyName);
        }
        catch (Exception e) {
            Constants.LOG.debug("Unable to read property {}", propertyName, e);
            return "unknown";
        }
    }
}