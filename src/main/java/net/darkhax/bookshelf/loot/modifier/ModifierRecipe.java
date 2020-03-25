package net.darkhax.bookshelf.loot.modifier;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifier will try to craft the item into something else using a defined recipe
 * category.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ModifierRecipe extends LootModifier {
    
    public static final Function<ILootCondition[], ModifierRecipe> CRAFTING = conditions -> new ModifierRecipe(conditions, IRecipeType.CRAFTING);
    public static final Function<ILootCondition[], ModifierRecipe> SMELTING = conditions -> new ModifierRecipe(conditions, IRecipeType.SMELTING);
    public static final Function<ILootCondition[], ModifierRecipe> BLASTING = conditions -> new ModifierRecipe(conditions, IRecipeType.BLASTING);
    public static final Function<ILootCondition[], ModifierRecipe> SMOKING = conditions -> new ModifierRecipe(conditions, IRecipeType.SMOKING);
    public static final Function<ILootCondition[], ModifierRecipe> CAMPFIRE = conditions -> new ModifierRecipe(conditions, IRecipeType.CAMPFIRE_COOKING);
    public static final Function<ILootCondition[], ModifierRecipe> STONECUT = conditions -> new ModifierRecipe(conditions, IRecipeType.STONECUTTING);
    
    private final IRecipeType recipeType;
    
    public ModifierRecipe(ILootCondition[] conditions, IRecipeType type) {
        
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
            
            final List<IRecipe> matchingRecipes = ctx.getWorld().getRecipeManager().getRecipes(this.recipeType, new Inventory(stack), ctx.getWorld());
            
            if (!matchingRecipes.isEmpty()) {
                
                final IRecipe recipe = matchingRecipes.get(ctx.getWorld().rand.nextInt(matchingRecipes.size()));
                
                if (recipe != null) {
                    
                    final ItemStack output = recipe.getRecipeOutput();
                    
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
}