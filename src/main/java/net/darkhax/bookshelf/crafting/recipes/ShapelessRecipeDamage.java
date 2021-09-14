package net.darkhax.bookshelf.crafting.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.darkhax.bookshelf.util.InventoryUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShapelessRecipeDamage extends ShapelessRecipe {
    
    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();
    
    private final int damageAmount;
    private final boolean ignoreUnbreaking;
    
    public ShapelessRecipeDamage(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs, int damageAmount, boolean ignoreUnbreaking) {
        
        super(id, group, output, inputs);
        this.damageAmount = damageAmount;
        this.ignoreUnbreaking = ignoreUnbreaking;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems (CraftingInventory inv) {
        
        final NonNullList<ItemStack> keptItems = super.getRemainingItems(inv);
        return InventoryUtils.keepDamageableItems(inv, keptItems, this.ignoreUnbreaking, this.damageAmount);
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer () {
        
        return SERIALIZER;
    }
    
    static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessRecipeDamage> {
        
        private Serializer() {
            
        }
        
        @Override
        public ShapelessRecipeDamage fromJson (ResourceLocation id, JsonObject json) {
            
            final String group = JSONUtils.getAsString(json, "group", "");
            final NonNullList<Ingredient> inputs = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
            final ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            final int damageAmount = JSONUtils.getAsInt(json, "damageAmount", 1);
            final boolean ignoreUnbreaking = JSONUtils.getAsBoolean(json, "ignoreUnbreaking", false);
            
            if (inputs.isEmpty()) {
                
                throw new JsonSyntaxException("No ingredients were found for the recipe!");
            }
            
            else if (inputs.size() > 9) {
                
                throw new JsonSyntaxException("Too many ingredients. Maximum is 9 but " + inputs.size() + " were given.");
            }
            
            else if (output.isEmpty()) {
                
                throw new JsonSyntaxException("The output of the recipe must not be empty!");
            }
            
            return new ShapelessRecipeDamage(id, group, output, inputs, damageAmount, ignoreUnbreaking);
        }
        
        @Override
        public ShapelessRecipeDamage fromNetwork (ResourceLocation id, PacketBuffer buffer) {
            
            final String group = buffer.readUtf(32767);
            final int inputCount = buffer.readVarInt();
            final NonNullList<Ingredient> inputs = NonNullList.withSize(inputCount, Ingredient.EMPTY);
            
            for (int i = 0; i < inputCount; i++) {
                
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }
            
            final ItemStack output = buffer.readItem();
            final int damageAmount = buffer.readVarInt();
            final boolean ignoreUnbreaking = buffer.readBoolean();
            
            return new ShapelessRecipeDamage(id, group, output, inputs, damageAmount, ignoreUnbreaking);
        }
        
        @Override
        public void toNetwork (PacketBuffer buffer, ShapelessRecipeDamage recipe) {
            
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());
            
            for (final Ingredient ingredient : recipe.getIngredients()) {
                
                ingredient.toNetwork(buffer);
            }
            
            buffer.writeItem(recipe.getResultItem());
            buffer.writeVarInt(recipe.damageAmount);
            buffer.writeBoolean(recipe.ignoreUnbreaking);
        }
        
        private static NonNullList<Ingredient> readIngredients (JsonArray json) {
            
            final NonNullList<Ingredient> ingredients = NonNullList.create();
            
            for (final JsonElement element : json) {
                
                final Ingredient ingredient = Ingredient.fromJson(element);
                
                if (!ingredient.isEmpty()) {
                    
                    ingredients.add(ingredient);
                }
            }
            
            return ingredients;
        }
    }
}