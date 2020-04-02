package net.darkhax.bookshelf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = ExampleSaveData.MODID)
public class ExampleSaveData {
    
    public static final String MODID = "examplemod";
    
    private static final Map<UUID, Integer> PLAYER_STAR_DATA = new HashMap<>();
    
    public static int getStars(PlayerEntity player) {
        
        return PLAYER_STAR_DATA.getOrDefault(player.getUniqueID(), 0);
    }
    
    public static void setStars(PlayerEntity player, int stars) {
        
        PLAYER_STAR_DATA.put(player.getUniqueID(), stars);
    }
    
    /**
     * A helper method that will get an appropriate save file location for a given player.
     * 
     * @param playerDir The root player directory.
     * @param uuid The UUID of the player.
     * @return An appropriate save file location for the custom player data.
     */
    private static File getPlayerFile (File playerDir, String uuid) {
        
        // Creates a new folder within the save directory that is named after your mod id.
        final File saveDir = new File(playerDir, MODID);
        
        // If your mod's save folder doesn't exist yet make it.
        if (!saveDir.exists()) {
            
            saveDir.mkdirs();
        }
        
        // Specifies a new player file within the mod's custom save director.
        return new File(saveDir, uuid + ".dat");
    }
    
    @SubscribeEvent
    public static void onPlayerLoad (PlayerEvent.LoadFromFile event) {
        
        // Get the custom player file for the player being loaded.
        final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
        
        // Only read a player's data if their custom file exists.
        if (playerFile.exists()) {
            
            try {
                
                // Reads the file as a compound tag.
                final CompoundNBT tag = CompressedStreamTools.read(playerFile);
                
                // Maps the player's data in a global map.
                PLAYER_STAR_DATA.put(event.getPlayer().getUniqueID(), tag.getInt("StarsCollected"));
            }
            
            catch (final IOException e) {
                
                System.out.println(e);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerSave (PlayerEvent.SaveToFile event) {
        
        final UUID playerUUID = event.getPlayer().getUniqueID();
        
        // Only try to write player data if we have data to write.
        if (PLAYER_STAR_DATA.containsKey(playerUUID)) {
            
            // Get the custom save file for the player.
            final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
            
            // Create the tag and write our custom data to it.
            final CompoundNBT tag = new CompoundNBT();
            tag.putInt("StarsCollected", PLAYER_STAR_DATA.get(playerUUID));
            
            try {
                
                // Save the file.
                CompressedStreamTools.write(tag, playerFile);
            }
            
            catch (final IOException e) {
                
                System.out.println(e);
            }
        }
    }
}