/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.darkhax.bookshelf.entity.BookshelfFakePlayer;
import net.darkhax.bookshelf.lib.function.LazyWeakReference;
import net.darkhax.bookshelf.lib.function.NonNullSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class PlayerUtils {
    
    /**
     * A simple check to make sure that an PlayerEntity actually exists.
     *
     * @param player: The instance of PlayerEntity to check.
     * @return boolean: If the player exists true will be returned. If they don't false will be
     *         returned.
     */
    public static boolean isPlayerReal (Entity player) {
        
        return player != null && player.level != null && player.getClass() == ServerPlayerEntity.class;
    }
    
    /**
     * Attempts to fix a stripped UUID. Usually used to fix stripped uuid strings from Mojang.
     *
     * @param uuidString The UUID string to fix.
     * @return The fixed UUID, or null if the uuid string is invalid.
     */
    public static UUID fixStrippedUUID (String uuidString) {
        
        return uuidString.length() != 32 ? null : UUID.fromString(uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20, 32));
    }
    
    /**
     * Checks if a player has an item in their inventory or equipment slots.
     *
     * @param player The player to check the inventory of.
     * @param item The item to check for.
     * @return Whether or not the player has the item in their inventory.
     */
    public static boolean playerHasItem (PlayerEntity player, Item item) {
        
        for (final ItemStack stack : player.inventory.items) {
            if (stack != null && stack.getItem().equals(item)) {
                return true;
            }
        }
        
        for (final EquipmentSlotType slotType : EquipmentSlotType.values()) {
            
            if (player.getItemBySlot(slotType).getItem() == item) {
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets all stacks that match a certain item from a player's inventory and equipment slots.
     *
     * @param player The player to check.
     * @param item The item to look for.
     * @return A list of all matching item stacks.
     */
    public static List<ItemStack> getStacksFromPlayer (PlayerEntity player, Item item) {
        
        final List<ItemStack> items = new ArrayList<>();
        
        for (final ItemStack stack : player.inventory.items) {
            if (stack != null && stack.getItem() == item) {
                items.add(stack);
            }
        }
        
        for (final EquipmentSlotType slotType : EquipmentSlotType.values()) {
            
            final ItemStack stack = player.getItemBySlot(slotType);
            
            if (stack.getItem() == item) {
                
                items.add(stack);
            }
        }
        
        return items;
    }
    
    /**
     * Retrieves an instance of the player from the client side. This code only exists in
     * client side code and can not be used in server side code.
     *
     * @return The client side player.
     */
    @OnlyIn(Dist.CLIENT)
    public static ClientPlayerEntity getClientPlayer () {
        
        return Minecraft.getInstance().player;
    }
    
    /**
     * Checks if the player is currently in game. This is done by checking if the current world
     * and player exist, and if the player exists in a valid world.
     *
     * @return Whether or not the player is currently in game.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isPlayerInGame () {
        
        final Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.level != null && mc.player.level != null;
    }
    
    /**
     * Gets the UUID for the client side player.
     *
     * @return The UUID for the client side player. May be null.
     */
    @OnlyIn(Dist.CLIENT)
    public static UUID getClientUUID () {
        
        return fixStrippedUUID(Minecraft.getInstance().getUser().getUuid());
    }
    
    /**
     * Checks if a DamageSource was caused by a player.
     *
     * @param source The damage source to check.
     * @return Whether or not the source was caused by a player.
     */
    public static boolean isPlayerDamage (DamageSource source) {
        
        return source != null && source.getEntity() instanceof PlayerEntity;
    }
    
    /**
     * Gets a resource location that is bound to a player skin texture.
     *
     * @param profile The profile to lookup.
     * @return The texture to use for that profile.
     */
    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getPlayerTexture (GameProfile profile) {
        
        // Validate the profile first.
        if (profile != null) {
            
            final Minecraft minecraft = Minecraft.getInstance();
            
            // Load skin data about the profile.
            final Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(profile);
            
            // If the loaded data has a skin, return that.
            if (map.containsKey(Type.SKIN)) {
                return minecraft.getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN);
            }
            else {
                return DefaultPlayerSkin.getDefaultSkin(PlayerEntity.createPlayerUUID(profile));
            }
        }
        
        // Default to the legacy steve skin.
        return DefaultPlayerSkin.getDefaultSkin();
    }
    
    /**
     * Gets a fake player using their game profile. If there are no matching fake players a new
     * one will be created.
     * 
     * @param world A world instance used to load the fake player.
     * @param profile The profile of the fake player to find. If there are no matches a new
     *        fake player will be created with this profile.
     * @return The fake player that was found or created.
     */
    public static FakePlayer getFakePlayer (ServerWorld world, GameProfile profile) {
        
        return getFakePlayer(world, profile, BookshelfFakePlayer::new);
    }
    
    /**
     * Gets a fake player using their game profile. If there are no matching fake players a new
     * one will be created using the provided factory.
     * 
     * @param world A world instance used to load the fake player. This will be passed to the
     *        factory if no matching fake players were found.
     * @param profile The profile of the fake player to find. This will be passed to the
     *        factory if there are no matching fake players.
     * @param factory A factory used to create the fake player if a match could not be found.
     * @return The fake player that was found or created.
     */
    public static FakePlayer getFakePlayer (ServerWorld world, GameProfile profile, BiFunction<ServerWorld, GameProfile, FakePlayer> factory) {
        
        final Map<GameProfile, FakePlayer> fakePlayers = getCurrentFakePlayers();
        
        FakePlayer fakePlayer = fakePlayers.get(profile);
        
        if (fakePlayer == null) {
            
            fakePlayer = factory.apply(world, profile);
            fakePlayers.put(profile, fakePlayer);
        }
        
        return fakePlayer;
    }
    
    /**
     * Creates a supplier that stores a weak reference to the computed fake player. The fake
     * player will be recomputed as needed when the weak reference expires.
     * 
     * @param world A world instance used to load the fake player.
     * @param profile The profile of the fake player to find. If there are no matches a new
     *        fake player will be created with this profile.
     * @return The supplier for the fake player.
     */
    public static Supplier<FakePlayer> getFakePlayerReference (ServerWorld world, GameProfile profile) {
        
        return new LazyWeakReference<>(NonNullSupplier.from( () -> getFakePlayer(world, profile)));
    }
    
    /**
     * Creates a supplier that stores a weak reference to the computed fake player. The fake
     * player will be recomputed as needed when the weak reference expires.
     * 
     * @param world A world instance used to load the fake player. This will be passed to the
     *        factory if no matching fake players were found.
     * @param profile The profile of the fake player to find. This will be passed to the
     *        factory if there are no matching fake players.
     * @param factory A factory used to create the fake player if a match could not be found.
     * @return The supplier for the fake player.
     */
    public static Supplier<FakePlayer> getFakePlayerReference (ServerWorld world, GameProfile profile, BiFunction<ServerWorld, GameProfile, FakePlayer> factory) {
        
        return new LazyWeakReference<>(NonNullSupplier.from( () -> getFakePlayer(world, profile, factory)));
    }
    
    /**
     * Gets Forge's internal fake player map from the FakePlayerFactory. This map only contains
     * fake players that are currently loaded and can only return results when a world is
     * active.
     * 
     * Caution must be used when modifying this map as it is managed by Forge. Adding a new
     * fake player should be done using
     * {@link #getFakePlayer(ServerWorld, GameProfile, BiFunction)} or the FakePlayerHandler
     * methods.
     * 
     * @return The map containing all fake players that have been registered with Forge.
     */
    public static Map<GameProfile, FakePlayer> getCurrentFakePlayers () {
        
        return ObfuscationReflectionHelper.getPrivateValue(FakePlayerFactory.class, null, "fakePlayers");
    }
}