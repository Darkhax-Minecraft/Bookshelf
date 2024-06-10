package net.darkhax.bookshelf.impl.resources;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.function.CachedSupplier;
import net.minecraft.DetectedVersion;
import net.minecraft.client.Minecraft;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExtendedText {

    public static Supplier<ExtendedText> INSTANCE = CachedSupplier.cache(ExtendedText::new);
    private final Map<String, Supplier<String>> extendedEntries = new LinkedHashMap<>();

    private ExtendedText() {

        this.register("java_version", () -> getProperty("java.version"));
        this.register("mc_version", DetectedVersion.BUILT_IN::getName);
        this.register("loader_name", Services.PLATFORM::getName);
        this.register("player_name", () -> Minecraft.getInstance().getUser().getName());

        for (String modId : Services.PLATFORM.getLoadedMods()) {
            this.register(modId + "_name", () -> Services.PLATFORM.getModName(modId));
            this.register(modId + "_version", () -> {
                final String version = Services.PLATFORM.getModVersion(modId);
                return version != null ? version : modId + "_version";
            });
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