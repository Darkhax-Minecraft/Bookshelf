package net.darkhax.bookshelf.crafting.recipes;

import java.util.Map;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.InventoryUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShapedRecipeDamaging extends ShapedRecipe {
    
    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();
    
    private final int damageAmount;
    private final boolean ignoreUnbreaking;
    
    public ShapedRecipeDamaging(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> input, ItemStack output, int damageAmount, boolean ignoreUnbreaking) {
        
        super(id, group, width, height, input, output);
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
    
    static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapedRecipeDamaging> {
        
        private Serializer() {
            
        }
        
        @Override
        public ShapedRecipeDamaging read (ResourceLocation recipeId, JsonObject json) {
            
            final String group = JSONUtils.getString(json, "group", "");
            final Map<String, Ingredient> ingredients = ShapedRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
            final String[] pattern = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
            final int width = pattern[0].length();
            final int height = pattern.length;
            final NonNullList<Ingredient> inputs = ShapedRecipe.deserializeIngredients(pattern, ingredients, width, height);
            final ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            final int damageAmount = JSONUtils.getInt(json, "damageAmount", 1);
            final boolean ignoreUnbreaking = JSONUtils.getBoolean(json, "ignoreUnbreaking", false);
            
            return new ShapedRecipeDamaging(recipeId, group, width, height, inputs, output, damageAmount, ignoreUnbreaking);
        }
        
        @Override
        public ShapedRecipeDamaging read (ResourceLocation recipeId, PacketBuffer buffer) {
            
            final int width = buffer.readInt();
            final int height = buffer.readInt();
            final String group = buffer.readString();
            
            final NonNullList<Ingredient> input = NonNullList.withSize(width * height, Ingredient.EMPTY);
            
            for (int i = 0; i < input.size(); i++) {
                
                input.set(i, Ingredient.read(buffer));
            }
            
            final ItemStack output = buffer.readItemStack();
            final int damageAmount = buffer.readInt();
            final boolean ignoreUnbreaking = buffer.readBoolean();
            
            return new ShapedRecipeDamaging(recipeId, group, width, height, input, output, damageAmount, ignoreUnbreaking);
        }
        
        @Override
        public void write (PacketBuffer buffer, ShapedRecipeDamaging recipe) {
            
            buffer.writeInt(recipe.getWidth());
            buffer.writeInt(recipe.getHeight());
            buffer.writeString(recipe.getGroup());
            
            for (final Ingredient ingredient : recipe.getIngredients()) {
                
                ingredient.write(buffer);
            }
            
            buffer.writeItemStack(recipe.getRecipeOutput());
            buffer.writeInt(recipe.damageAmount);
            buffer.writeBoolean(recipe.ignoreUnbreaking);
        }
    }
}