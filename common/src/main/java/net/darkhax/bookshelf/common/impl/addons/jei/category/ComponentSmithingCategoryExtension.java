package net.darkhax.bookshelf.common.impl.addons.jei.category;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.platform.IPlatformRecipeHelper;
import mezz.jei.library.plugins.vanilla.anvil.SmithingCategoryExtension;
import net.darkhax.bookshelf.common.impl.recipe.smithing.ComponentSmithingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComponentSmithingCategoryExtension extends SmithingCategoryExtension<ComponentSmithingRecipe> {

    public ComponentSmithingCategoryExtension(IPlatformRecipeHelper recipeHelper) {
        super(recipeHelper);
    }

    @Override
    public void onDisplayedIngredientsUpdate(ComponentSmithingRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        final List<IFocus<?>> outputFocuses = focuses.getFocuses(RecipeIngredientRole.OUTPUT).toList();
        if (outputFocuses.isEmpty()) {
            final ItemStack output = recipe.assemble(new SmithingRecipeInput(getStack(templateSlot), getStack(baseSlot), getStack(additionSlot)));
            outputSlot.createDisplayOverrides().add(output);
        }
        else {
            final ItemStack base = getStack(outputSlot).getItem().getDefaultInstance();
            baseSlot.createDisplayOverrides().add(base);
            outputSlot.createDisplayOverrides().add(recipe.assemble(new SmithingRecipeInput(getStack(templateSlot), base, getStack(additionSlot))));
        }
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(ComponentSmithingRecipe recipe, T ingredientAcceptor) {
        recipe.templateIngredient().ifPresent(ingredientAcceptor::add);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(ComponentSmithingRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.add(recipe.baseIngredient());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(ComponentSmithingRecipe recipe, T ingredientAcceptor) {
        recipe.additionIngredient().ifPresent(ingredientAcceptor::add);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(ComponentSmithingRecipe recipe, T ingredientAcceptor) {
        final ContextMap contextmap = SlotDisplayContext.fromLevel(Objects.requireNonNull(Minecraft.getInstance().level));
        final List<ItemStack> templateItems = resolve(recipe.templateIngredient(), contextmap);
        final List<ItemStack> baseItems = resolve(Optional.of(recipe.baseIngredient()), contextmap);
        final ItemStack addition = recipe.additionIngredient().map(i -> i.display().resolveForFirstStack(contextmap)).orElse(ItemStack.EMPTY);
        for (ItemStack template : templateItems) {
            for (ItemStack base : baseItems) {
                ingredientAcceptor.add(recipe.assemble(new SmithingRecipeInput(template, base, addition)));
            }
        }
    }

    private static ItemStack getStack(IRecipeSlotDrawable slot) {
        return slot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
    }

    private static List<ItemStack> resolve(Optional<Ingredient> ingredient, ContextMap contextmap) {
        final List<ItemStack> items = ingredient.map(i -> i.display().resolveForStacks(contextmap)).orElse(List.of(ItemStack.EMPTY));
        return items.isEmpty() ? List.of(ItemStack.EMPTY) : items;
    }
}