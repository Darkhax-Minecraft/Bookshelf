package net.darkhax.bookshelf.loot.modifier;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifier will try to craft the item into something else using a defined recipe
 * category.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ModifierRecipe extends LootModifier {

    public static final GlobalLootModifierSerializer CRAFTING = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.CRAFTING));
    public static final GlobalLootModifierSerializer SMELTING = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.SMELTING));
    public static final GlobalLootModifierSerializer BLASTING = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.BLASTING));
    public static final GlobalLootModifierSerializer SMOKING = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.SMOKING));
    public static final GlobalLootModifierSerializer CAMPFIRE = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.CAMPFIRE_COOKING));
    public static final GlobalLootModifierSerializer STONECUT = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.STONECUTTING));
    public static final GlobalLootModifierSerializer SMITHING = createModifier(conditions -> new ModifierRecipe(conditions, RecipeType.SMITHING));

    private final RecipeType recipeType;

    public ModifierRecipe (LootItemCondition[] conditions, RecipeType type) {

        super(conditions);
        this.recipeType = type;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply (List<ItemStack> loot, LootContext ctx) {

        return loot.stream().map(stack -> this.craft(stack, ctx)).collect(Collectors.toList());
    }

    private ItemStack craft (ItemStack stack, LootContext ctx) {

        try {

            final List<Recipe> matchingRecipes = ctx.getLevel().getRecipeManager().getRecipesFor(this.recipeType, new SimpleContainer(stack), ctx.getLevel());

            if (!matchingRecipes.isEmpty()) {

                final Recipe recipe = matchingRecipes.get(ctx.getLevel().random.nextInt(matchingRecipes.size()));

                if (recipe != null) {

                    final ItemStack output = recipe.getResultItem();

                    if (output != null && !output.isEmpty()) {

                        return output;
                    }
                }
            }
        }

        catch (final Exception e) {

            Bookshelf.LOG.error("The following error is with another mod or your configs. Do not report it to Bookshelf!");
            Bookshelf.LOG.catching(e);
        }

        return stack;
    }

    public static GlobalLootModifierSerializer<ModifierRecipe> createModifier (Function<LootItemCondition[], ModifierRecipe> function) {

        return new GlobalLootModifierSerializer<ModifierRecipe>() {

            @Override
            public ModifierRecipe read (ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {

                return function.apply(ailootcondition);
            }

            @Override
            public JsonObject write (ModifierRecipe instance) {

                return new JsonObject();
            }
        };
    }
}