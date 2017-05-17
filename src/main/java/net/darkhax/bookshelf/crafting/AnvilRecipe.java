/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.crafting;

import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.item.ItemStack;

public class AnvilRecipe implements IAnvilRecipe {

    /**
     * The ItemStack required in the left slot of the Anvil GUI.
     */
    private final ItemStack requiredLeft;

    /**
     * The ItemStack required in the right side of the Anvil GUI.
     */
    private final ItemStack requiredRight;

    /**
     * A name requirement for the recipe.
     */
    private final String requiredName;

    /**
     * The amount of experience levels to charge for this recipe.
     */
    private final int experienceCost;

    /**
     * The amount of items to consume, from the right slot of the Anvil GUI. If 0, the whole
     * stack will be consumed.
     */
    private final int materialCost;

    /**
     * The ItemStack output for this recipe.
     */
    private final ItemStack output;

    /**
     * Constructs a new AnvilRecipe, using all of the required parameters, except for the name
     * requirement and material cost.
     *
     * @param firstInput The ItemStack required in the left slot of the Anvil GUI.
     * @param secondInput The ItemStack required in the right slot of the Anvil GUI.
     * @param experience The amount of experience that this recipe should cost.
     * @param outputStack The ItemStack that will be created by this recipe.
     */
    public AnvilRecipe (ItemStack firstInput, ItemStack secondInput, int experience, ItemStack outputStack) {

        this(firstInput, secondInput, null, experience, 0, outputStack);
    }

    /**
     * Constructs a new AnvilRecipe, using all of the required parameters.
     *
     * @param firstInput The ItemStack required in the left slot of the Anvil GUI.
     * @param secondInput The ItemStack required in the right slot of the Anvil GUI.
     * @param requiredName A name requirement for this recipe.
     * @param experience The amount of experience that this recipe should cost.
     * @param materialCost The amount of items to consume from the right slot of the Anvil GUI.
     *        If 0, the whole stack will be consumed.
     * @param outputStack The ItemStack that will be created by this recipe.
     */
    public AnvilRecipe (ItemStack firstInput, ItemStack secondInput, String requiredName, int experience, int materialCost, ItemStack outputStack) {

        this.requiredLeft = firstInput;
        this.requiredRight = secondInput;
        this.requiredName = requiredName;
        this.experienceCost = experience;
        this.materialCost = materialCost;
        this.output = outputStack;
    }

    @Override
    public boolean isValidRecipe (ItemStack leftSlot, ItemStack rightSlot, String name) {

        return StackUtils.areStacksSimilarWithSize(leftSlot, this.requiredLeft) && StackUtils.areStacksSimilarWithSize(rightSlot, this.requiredRight) && this.requiredName != null && !this.requiredName.isEmpty() ? this.requiredName.equals(name) : true;
    }

    @Override
    public int getExperienceCost (ItemStack leftSlot, ItemStack rightSlot, String name) {

        return this.experienceCost;
    }

    @Override
    public int getMaterialCost (ItemStack leftSlot, ItemStack rightSlot, String name) {

        return this.materialCost;
    }

    @Override
    public ItemStack getOutput (ItemStack leftSlot, ItemStack rightSlot, String name) {

        return this.output.copy();
    }
}