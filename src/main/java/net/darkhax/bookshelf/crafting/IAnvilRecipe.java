package net.darkhax.bookshelf.crafting;

import net.minecraft.item.ItemStack;

/**
 * Defines the base requirements for any anvil recipe entry.
 *
 * @author Tyler Hancock (Darkhax)
 */
public interface IAnvilRecipe {

    /**
     * Checks if the conditions for the recipe are correct.
     *
     * @param leftSlot The left anvil slot.
     * @param rightSlot The right anvil slot.
     * @param name The string in the name section of the anvil.
     * @return Whether or not the conditions for the recipe are met.
     */
    public boolean isValidRecipe (ItemStack leftSlot, ItemStack rightSlot, String name);

    /**
     * Gets the amount of experience levels to charge for the recipe.
     *
     * @param leftSlot The left anvil slot.
     * @param rightSlot The right anvil slot.
     * @param name The string in the name section of the anvil.
     * @return The amount of experience levels to charge.
     */
    public int getExperienceCost (ItemStack leftSlot, ItemStack rightSlot, String name);

    /**
     * Gets the amount of items to consume from the material (right) slot. If 0, the entire
     * stack will be consumed.
     *
     * @param leftSlot The left anvil slot.
     * @param rightSlot The right anvil slot.
     * @param name The string in the name section of the anvil.
     * @return The amount of items to consume from the material (right) slot.
     */
    public int getMaterialCost (ItemStack leftSlot, ItemStack rightSlot, String name);

    /**
     * Gets the output for the recipe. This is the resulting item stack and will be previewable
     * before crafting it.
     *
     * @param leftSlot The left anvil slot.
     * @param rightSlot The right anvil slot.
     * @param name The string in the name section of the anvil.
     * @return The ItemStack to create with this recipe.
     */
    public ItemStack getOutput (ItemStack leftSlot, ItemStack rightSlot, String name);
}