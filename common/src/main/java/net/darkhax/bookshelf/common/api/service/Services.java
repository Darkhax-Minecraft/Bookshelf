package net.darkhax.bookshelf.common.api.service;

import net.darkhax.bookshelf.common.api.util.IPlatformHelper;
import net.darkhax.bookshelf.common.impl.Constants;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}.", loadedService, clazz);
        return loadedService;
    }
}