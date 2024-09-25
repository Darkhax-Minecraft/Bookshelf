package net.darkhax.bookshelf.fabric.impl;

import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.fabricmc.api.ModInitializer;
import net.minecraft.DetectedVersion;

import java.net.HttpURLConnection;
import java.net.URL;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        BookshelfMod.getInstance().init();
        checkForUpdates();
    }

    private static void checkForUpdates() {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL("https://updates.blamejared.com/get?n=" + Constants.MOD_ID + "&gv=" + DetectedVersion.BUILT_IN.getName() + "&ml=fabric").openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Constants.LOG.warn("Version checker is not available.");
            }
        }
        catch (Exception e) {
            // TODO
        }
    }
}