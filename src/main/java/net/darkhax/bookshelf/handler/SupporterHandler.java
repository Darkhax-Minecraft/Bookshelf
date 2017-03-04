package net.darkhax.bookshelf.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.stream.JsonReader;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.darkhax.bookshelf.lib.util.TextUtils.ChatFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * This class is used to handle my supporters data. This class is not intended for other mod
 * authors to access in their code. If you want, you can copy and modify this code to add
 * support for your own supporters and rewards.
 */
public final class SupporterHandler {

    private static final ResourceLocation textureElytraMissing = new ResourceLocation("bookshelf", "textures/entity/player/missing_elytra.png");

    private static final List<SupporterData> supporterData = new ArrayList<>();

    private static final String supporterLocation = "https://raw.githubusercontent.com/Darkhax-Minecraft/Bookshelf/master/supporters.json";

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private SupporterHandler () {

        throw new IllegalAccessError("Utility class");
    }

    public static void readSupporterData () {

        try {

            // Reads the json file and makes it usable.
            final HttpURLConnection connection = (HttpURLConnection) new URL(supporterLocation).openConnection();
            final JsonReader reader = new JsonReader(new InputStreamReader((InputStream) connection.getContent()));

            reader.beginObject();

            // Starts a loop through all of the player entries.
            while (reader.hasNext()) {

                // Gets the name of the next player entry and skips it.
                reader.nextName();
                // final String entryName = reader.nextName();

                UUID playerID = null;
                String type = null;
                boolean confirmed = false;
                boolean wantsHead = true;
                boolean wantsWawla = false;
                String elytraTexture = null;

                // Opens up the player entry object
                reader.beginObject();

                // Loops through the values in the newly opened object
                while (reader.hasNext()) {

                    final String name = reader.nextName();

                    if ("playerID".equals(name)) {
                        playerID = UUID.fromString(reader.nextString());
                    }
                    else if ("type".equals(name)) {
                        type = reader.nextString();
                    }
                    else if ("confirmed".equals(name)) {
                        confirmed = reader.nextBoolean();
                    }
                    else if ("wantHead".equals(name)) {
                        wantsHead = reader.nextBoolean();
                    }
                    else if ("wantWawla".equals(name)) {
                        wantsWawla = reader.nextBoolean();
                    }
                    else if ("elytraTexture".equals(name)) {
                        elytraTexture = reader.nextString();
                    }
                    else {
                        reader.skipValue();
                    }
                }

                // Adds the data that was read to the list.
                supporterData.add(new SupporterData(playerID, type, confirmed, wantsHead, wantsWawla, elytraTexture));

                // Ends the current player object, allowing us to read the next player object.
                reader.endObject();
            }

            // Exits out of the json array.
            reader.endObject();

            // Closes the reader.
            reader.close();
        }

        catch (final MalformedURLException e) {

            Constants.LOG.error("Could not access supporter data.", e);
        }

        catch (final IOException e) {

            Constants.LOG.error("Could not access supporter data.", e);
        }
    }

    /**
     * Checks if a player is a valid supporter supporter.
     *
     * @param player The player to check for.
     * @return Whether or not the player is a supporter.
     */
    public static boolean isSupporter (EntityPlayer player) {

        for (final SupporterData supporter : supporterData)
            if (supporter.getPlayerID().equals(player.getUniqueID()))
                return true;

        return false;
    }

    /**
     * Attempts to get the supporterData for a player.
     *
     * @param player The player to get data for.
     * @return The supporter data, or null if the player is not a supporter.
     */
    public static SupporterData getSupporterData (EntityPlayer player) {

        for (final SupporterData supporter : supporterData)
            if (supporter.getPlayerID().equals(player.getUniqueID()))
                return supporter;

        return null;
    }

    /**
     * Gets an array containing all entries in the list of supporters.
     *
     * @return An array of all supporter entries.
     */
    public static SupporterData[] getSupporters () {

        return supporterData.toArray(new SupporterData[supporterData.size()]);
    }

    public static class SupporterData {

        /**
         * The unique identifier for the supporter player.
         */
        private final UUID playerId;

        /**
         * The type of supporter. Currently supports dev, patreon and contributor.
         */
        private final String type;

        /**
         * Whether or not this supporter has completed a payment.
         */
        private final boolean confirmed;

        /**
         * Whether or not this supporter wants their head to show up in the creative tab.
         */
        private final boolean wantsHead;

        /**
         * Whether or not this supporter wants additional tooltip info when using Wawla on
         * them.
         */
        private final boolean wantsWawla;

        /**
         * The URL for the custom Elytra texture.
         */
        private final String ELYTRA_TEXTURE;

        protected SupporterData (UUID playerID, String type, boolean confirmed, boolean wantsHead, boolean wantsWawla, String elytraTexture) {

            this.playerId = playerID;
            this.type = type;
            this.confirmed = confirmed;
            this.wantsHead = wantsHead;
            this.wantsWawla = wantsWawla;
            this.ELYTRA_TEXTURE = elytraTexture;
        }

        /**
         * Gets the supporter's unique identifier.
         *
         * @return The supporter's unique identifier.
         */
        public UUID getPlayerID () {

            return this.playerId;
        }

        /**
         * Checks if the supporter has completed a payment.
         *
         * @return Whether or not the supporter has completed a payment.
         */
        public boolean isConfirmed () {

            return this.confirmed;
        }

        /**
         * Checks if the supporter wants their head in the skull creative tab.
         *
         * @return Whether or not the supporter wants their head in the creative tab.
         */
        public boolean wantsHead () {

            return this.wantsHead;
        }

        /**
         * Checks if the supporter wants a supporter tooltip when using Wawla.
         *
         * @return Whether or not the supporter wants the tooltip.
         */
        public boolean wantsWawla () {

            return this.wantsWawla;
        }

        /**
         * Gets the URL for the custom Elytra texture for the supporter.
         *
         * @return A url that points to the custom Elytra texture that the supporter wants.
         */
        public String getElytraTextureURL () {

            return this.ELYTRA_TEXTURE;
        }

        /**
         * Gets the custom Elytra texture for the supporter.
         *
         * @return A ResourceLocation that points to the custom Elytra texture that the
         *         supporter wants.
         */
        public ResourceLocation getElytraTexture () {

            return this.ELYTRA_TEXTURE != null && !this.ELYTRA_TEXTURE.isEmpty() ? RenderUtils.downloadResourceLocation(this.ELYTRA_TEXTURE, new ResourceLocation("bookshelf", "elytra/" + this.getPlayerID().toString()), textureElytraMissing, null) : null;
        }

        /**
         * Gets the type of supporter. Currently dev, contributor, patreon and other.
         *
         * @return The type of supporter.
         */
        public String getType () {

            return this.type;
        }

        /**
         * Gets the localized name, including colors and other formatting.
         *
         * @return The localized display name for the supporter type.
         */
        public String getLocalizedType () {

            return this.type.toLowerCase();
        }

        /**
         * Gets the color type for the supporter tier.
         *
         * @return The chat formatting for the supporter tier.
         */
        public ChatFormat getFormat () {

            return "developer".equals(this.type) ? ChatFormat.GREEN : "patreon".equals(this.type) ? ChatFormat.GOLD : "contributor".equals(this.type) ? ChatFormat.AQUA : "translator".equals(this.type) ? ChatFormat.RED : ChatFormat.RESET;
        }
    }
}