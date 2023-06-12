package net.darkhax.bookshelf.api;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.data.ITagHelper;
import net.darkhax.bookshelf.api.event.IEventHelper;
import net.darkhax.bookshelf.api.registry.IContentLoader;
import net.darkhax.bookshelf.api.util.IConstructHelper;
import net.darkhax.bookshelf.api.util.IInventoryHelper;
import net.darkhax.bookshelf.api.util.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IEventHelper EVENTS = load(IEventHelper.class);
    public static final IContentLoader REGISTRIES = load(IContentLoader.class);
    public static final ITagHelper TAGS = load(ITagHelper.class);
    public static final IInventoryHelper INVENTORY_HELPER = load(IInventoryHelper.class);
    public static final IConstructHelper CONSTRUCTS = load(IConstructHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}.", loadedService, clazz);
        return loadedService;
    }
}