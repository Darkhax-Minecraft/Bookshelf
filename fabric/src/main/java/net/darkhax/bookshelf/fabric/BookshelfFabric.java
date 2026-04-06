package net.darkhax.bookshelf.fabric;

import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.fabric.impl.util.FabricRegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.DetectedVersion;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class BookshelfFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Services.CONTENT.get().forEach(FabricRegistryHelper::new);
        CompletableFuture.runAsync(BookshelfFabric::checkForUpdates);
    }

    private static void checkForUpdates() {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URI("https://updates.blamejared.com/get?n=" + BookshelfMod.MOD_ID + "&gv=" + DetectedVersion.tryDetectVersion().name() + "&ml=fabric").toURL().openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                BookshelfMod.LOG.warn("Version checker is not available.");
            }
        }
        catch (Exception e) {
            // TODO
        }
    }
}