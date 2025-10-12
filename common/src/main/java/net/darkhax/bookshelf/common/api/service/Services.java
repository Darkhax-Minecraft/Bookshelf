package net.darkhax.bookshelf.common.api.service;

import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.network.INetworkHandler;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.util.IGameplayHelper;
import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.darkhax.bookshelf.common.impl.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final CachedSupplier<List<ContentProvider>> CONTENT = CachedSupplier.cache(() -> loadMany(ContentProvider.class));
    public static final IGameplayHelper GAMEPLAY = load(IGameplayHelper.class);
    public static final INetworkHandler NETWORK = load(INetworkHandler.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}.", loadedService, clazz);
        return loadedService;
    }

    public static <T> List<T> loadMany(Class<T> clazz) {
        final List<T> entries = ServiceLoader.load(clazz).stream().map(ServiceLoader.Provider::get).toList();
        Constants.LOG.debug("Loaded {} entries for {}. {}", entries.size(), clazz, entries.stream().map(entry -> entry.getClass().getCanonicalName()).collect(Collectors.joining()));
        return entries;
    }

    /**
     * Finds implementations of a service without initializing or classloading them.
     *
     * @param name The fully qualified name of the service.
     * @return A list of all implementations that were found.
     * @throws IOException Sometimes stuff can't be read.
     */
    public static List<String> findServices(String name) throws IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final List<String> matches = new ArrayList<>();
        final Enumeration<URL> candidates = classLoader.getResources("META-INF/services/" + name);
        while (candidates.hasMoreElements()) {
            try (InputStream input = candidates.nextElement().openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.startsWith("#") && !line.isEmpty()) {
                        matches.add(line);
                    }
                }
            }
        }
        return matches;
    }
}