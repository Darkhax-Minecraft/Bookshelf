package net.darkhax.bookshelf.recipe;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import net.darkhax.bookshelf.lib.util.PlayerUtils;

public class PlayerRecipe implements IRecipe {
    
    /**
     * An array of usernames which represent the players that can use this recipe.
     */
    private String[] playerNames;
    
    /**
     * An array of UUID which represent the players that can use this recipe.
     */
    private UUID[] playerIDs;
    
    /**
     * The IRecipe to pull crafting details from.
     */
    private IRecipe recipe;
    
    /**
     * Constructs a new PlayerRecipe. This is a Recipe that can only be used by specific
     * players.
     * 
     * @param username: The username of the player that is allowed to craft this recipe.
     * @param recipe: The Recipe that is crafted by this recipe.
     */
    public PlayerRecipe(String username, IRecipe recipe) {
        
        this(new String[] { username }, recipe);
    }
    
    /**
     * Constructs a new PlayerRecipe. This is a Recipe that can only be used by specific
     * players.
     * 
     * @param usernames: An array of usernames which represent the players allowed to craft
     *            this recipe.
     * @param recipe: The Recipe that is crafted by this recipe.
     */
    public PlayerRecipe(String[] usernames, IRecipe recipe) {
        
        this.playerNames = usernames;
        this.recipe = recipe;
    }
    
    /**
     * Constructs a new PlayerRecipe. This is a Recipe that can only be used by specific
     * players.
     * 
     * @param playerID: The UUID of the player allowed to craft this recipe.
     * @param recipe: The Recipe that is crafted by this recipe.
     */
    public PlayerRecipe(UUID playerID, IRecipe recipe) {
        
        this(new UUID[] { playerID }, recipe);
    }
    
    /**
     * Constructs a new PlayerRecipe. This is a Recipe that can only be used by specific
     * players.
     * 
     * @param playerIDs: An array of UUID which represent the players allowed to craft this
     *            recipe.
     * @param recipe: The Recipe that is crafted by this recipe.
     */
    public PlayerRecipe(UUID[] playerIDs, IRecipe recipe) {
        
        this.playerIDs = playerIDs;
        this.recipe = recipe;
    }
    
    @Override
    public boolean matches (InventoryCrafting crafting, World world) {
        
        EntityPlayer player = PlayerUtils.getPlayerFromCrafting(crafting);
        boolean isValidPlayer = false;
        
        if (player != null) {
            
            if (playerNames != null) {
                
                for (String playerName : playerNames) {
                    
                    if (player.getCommandSenderName().equalsIgnoreCase(playerName)) {
                        
                        isValidPlayer = true;
                        break;
                    }
                }
            }
            
            if (playerIDs != null && !isValidPlayer) {
                
                for (UUID playerID : playerIDs) {
                    
                    if (player.getUniqueID().equals(playerID)) {
                        
                        isValidPlayer = true;
                        break;
                    }
                }
            }
        }
        
        return isValidPlayer && recipe.matches(crafting, world);
    }
    
    @Override
    public ItemStack getCraftingResult (InventoryCrafting crafting) {
        
        return recipe.getCraftingResult(crafting);
    }
    
    @Override
    public int getRecipeSize () {
        
        return recipe.getRecipeSize();
    }
    
    @Override
    public ItemStack getRecipeOutput () {
        
        return recipe.getRecipeOutput();
    }
}
