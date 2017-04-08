package net.darkhax.bookshelf.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.darkhax.bookshelf.config.ConfigurationHandler;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.AnnotationUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public class FeatureManager {

    private static final List<Feature> features = new ArrayList<>();

    private static boolean loaded = false;

    private static ConfigurationHandler config;

    public static void init (ASMDataTable asmDataTable) {

        loaded = true;

        config = new ConfigurationHandler(Constants.MOD_ID);
        config.init(asmDataTable);

        for (final Entry<Feature, BookshelfFeature> feature : AnnotationUtils.getAnnotations(asmDataTable, BookshelfFeature.class, Feature.class).entrySet()) {

            final BookshelfFeature annotation = feature.getValue();

            if (annotation == null) {

                Constants.LOG.warn("Annotation for " + feature.getKey().getClass().getCanonicalName() + " was null!");
                continue;
            }

            registerFeature(feature.getKey(), annotation.name(), annotation.description());
        }

        for (final Feature feature : getFeatures()) {

            feature.setupConfiguration(config.getConfig());
        }

        config.sync();
    }

    /**
     * Registers a new feature with the feature manager. This will automatically create an
     * entry in the configuration file to enable/disable this feature. If the feature has been
     * disabled, it will not be registered. This will also handle event bus subscriptions.
     *
     * @param feature The feature being registered.
     * @param name The name of the feature.
     * @param description A short description of the feature.
     */
    public static void registerFeature (Feature feature, String name, String description) {

        feature.enabled = isFeatureEnabled(feature, name, description);

        if (feature.enabled) {

            feature.configName = name.toLowerCase().replace(' ', '_');
            features.add(feature);

            if (feature.usesEvents()) {
                MinecraftForge.EVENT_BUS.register(feature);
            }
        }
    }

    private static boolean isFeatureEnabled (Feature feature, String name, String description) {

        return config.getConfig().getBoolean(name, "_features", feature.enabledByDefault(), description);
    }

    public static boolean isLoaded () {

        return loaded;
    }

    public static List<Feature> getFeatures () {

        return features;
    }
}