package net.darkhax.bookshelf.crafting.recipes.smithing;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.TextUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeFont extends SmithingRecipe {
    
    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();
    
    private final ResourceLocation fontId;
    
    public SmithingRecipeFont(ResourceLocation recipeId, Ingredient base, Ingredient addition, ResourceLocation fontId) {
        
        super(recipeId, base, addition, ItemStack.EMPTY);
        this.fontId = fontId;
    }
    
    @Override
    public ItemStack getCraftingResult (IInventory inv) {
        
        final ItemStack stack = inv.getStackInSlot(0).copy();
        stack.setDisplayName(TextUtils.applyFont(stack.getDisplayName(), this.fontId));
        return stack;
    }
    
    @Override
    public boolean isDynamic () {
        
        return true;
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer () {
        
        return SERIALIZER;
    }
    
    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipeFont> {
        
        @Override
        public SmithingRecipeFont read (ResourceLocation recipeId, JsonObject json) {
            
            final Ingredient base = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
            final Ingredient addition = Ingredient.deserialize(JSONUtils.getJsonObject(json, "addition"));
            final ResourceLocation font = ResourceLocation.tryCreate(JSONUtils.getString(json, "font"));
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }
        
        @Override
        public SmithingRecipeFont read (ResourceLocation recipeId, PacketBuffer buffer) {
            
            final Ingredient base = Ingredient.read(buffer);
            final Ingredient addition = Ingredient.read(buffer);
            final ResourceLocation font = ResourceLocation.tryCreate(buffer.readString());
            return new SmithingRecipeFont(recipeId, base, addition, font);
        }
        
        @Override
        public void write (PacketBuffer buffer, SmithingRecipeFont recipe) {
            
            recipe.base.write(buffer);
            recipe.addition.write(buffer);
            buffer.writeString(recipe.fontId.toString());
        }
    }
}