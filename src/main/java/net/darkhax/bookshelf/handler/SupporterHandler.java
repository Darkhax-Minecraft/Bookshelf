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
public class SupporterHandler {
    
    private static final ResourceLocation MISSING_ELYTRA = new ResourceLocation("bookshelf", "textures/entity/player/missing_elytra.png");
    
    private static final List<SupporterData> DATA = new ArrayList<SupporterData>();
    private static final String SUPPORTER_URL = "https://raw.githubusercontent.com/Darkhax-Minecraft/Bookshelf/master/supporters.json";
    
    public static void readSupporterData () {
        
        try {
            
            // Reads the json file and makes it usable.
            final HttpURLConnection connection = (HttpURLConnection) new URL(SUPPORTER_URL).openConnection();
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
                    
                    if (name.equals("playerID"))
                        playerID = UUID.fromString(reader.nextString());
                    
                    else if (name.equals("type"))
                        type = reader.nextString();
                    
                    else if (name.equals("confirmed"))
                        confirmed = reader.nextBoolean();
                    
                    else if (name.equals("wantHead"))
                        wantsHead = reader.nextBoolean();
                    
                    else if (name.equals("wantWawla"))
                        wantsWawla = reader.nextBoolean();
                    
                    else if (name.equals("elytraTexture"))
                        elytraTexture = reader.nextString();
                    
                    // Skips values we don't care about so they don't break us.
                    else
                        reader.skipValue();
                }
                
                // Adds the data that was read to the list.
                DATA.add(new SupporterData(playerID, type, confirmed, wantsHead, wantsWawla, elytraTexture));
                
                // Ends the current player object, allowing us to read the next player object.
                reader.endObject();
            }
            
            // Exits out of the json array.
            reader.endObject();
            
            // Closes the reader.
            reader.close();
        }
        
        catch (final MalformedURLException e) {
            
            Constants.LOG.error("Could not access supporter data. " + e.getMessage());
        }
        
        catch (final IOException e) {
            
            Constants.LOG.error("Could not access supporter data. " + e.getMessage());
        }
    }
    
    /**
     * Checks if a player is a valid supporter supporter.
     * 
     * @param player The player to check for.
     * @return Whether or not the player is a supporter.
     */
    public static boolean isSupporter (EntityPlayer player) {
        
        for (final SupporterData supporter : DATA)
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
        
        for (final SupporterData supporter : DATA)
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
        
        return DATA.toArray(new SupporterData[DATA.size()]);
    }
    
    public static class SupporterData {
        
        /**
         * The unique identifier for the supporter player.
         */
        private final UUID PLAYER_ID;
        
        /**
         * The type of supporter. Currently supports dev, patreon and contributor.
         */
        private final String TYPE;
        
        /**
         * Whether or not this supporter has completed a payment.
         */
        private final boolean CONFIRMED;
        
        /**
         * Whether or not this supporter wants their head to show up in the creative tab.
         */
        private final boolean WANTS_HEAD;
        
        /**
         * Whether or not this supporter wants additional tooltip info when using Wawla on
         * them.
         */
        private final boolean WANTS_WAWLA;
        
        /**
         * The URL for the custom Elytra texture.
         */
        private final String ELYTRA_TEXTURE;
        
        protected SupporterData(UUID playerID, String type, boolean confirmed, boolean wantsHead, boolean wantsWawla, String elytraTexture) {
            
            this.PLAYER_ID = playerID;
            this.TYPE = type;
            this.CONFIRMED = confirmed;
            this.WANTS_HEAD = wantsHead;
            this.WANTS_WAWLA = wantsWawla;
            this.ELYTRA_TEXTURE = elytraTexture;
        }
        
        /**
         * Gets the supporter's unique identifier.
         * 
         * @return The supporter's unique identifier.
         */
        public UUID getPlayerID () {
            
            return this.PLAYER_ID;
        }
        
        /**
         * Checks if the supporter has completed a payment.
         * 
         * @return Whether or not the supporter has completed a payment.
         */
        public boolean isConfirmed () {
            
            return this.CONFIRMED;
        }
        
        /**
         * Checks if the supporter wants their head in the skull creative tab.
         * 
         * @return Whether or not the supporter wants their head in the creative tab.
         */
        public boolean wantsHead () {
            
            return this.WANTS_HEAD;
        }
        
        /**
         * Checks if the supporter wants a supporter tooltip when using Wawla.
         * 
         * @return Whether or not the supporter wants the tooltip.
         */
        public boolean wantsWawla () {
            
            return this.WANTS_WAWLA;
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
            
            return this.ELYTRA_TEXTURE != null && !this.ELYTRA_TEXTURE.isEmpty() ? RenderUtils.downloadResourceLocation(this.ELYTRA_TEXTURE, new ResourceLocation("bookshelf", "elytra/" + this.getPlayerID().toString()), MISSING_ELYTRA, null) : null;
        }
        
        /**
         * Gets the type of supporter. Currently dev, contributor, patreon and other.
         * 
         * @return The type of supporter.
         */
        public String getType () {
            
            return this.TYPE;
        }
        
        /**
         * Gets the localized name, including colors and other formatting.
         * 
         * @return The localized display name for the supporter type.
         */
        public String getLocalizedType () {
            
            return this.TYPE.toLowerCase();
        }
        
        /**
         * Gets the color type for the supporter tier.
         * 
         * @return The chat formatting for the supporter tier.
         */
        public ChatFormat getFormat () {
            
            return this.TYPE.equals("developer") ? ChatFormat.GREEN : this.TYPE.equals("patreon") ? ChatFormat.GOLD : this.TYPE.equals("contributor") ? ChatFormat.AQUA : this.TYPE.equalsIgnoreCase("translator") ? ChatFormat.RED : ChatFormat.RESET;
        }
    }
}