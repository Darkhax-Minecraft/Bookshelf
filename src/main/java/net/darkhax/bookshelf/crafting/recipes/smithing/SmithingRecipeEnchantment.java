package net.darkhax.bookshelf.crafting.recipes.smithing;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmithingRecipeEnchantment extends SmithingRecipe {
    
    public static final IRecipeSerializer<?> SERIALIZER = new Serializer();
    
    private final List<EnchantmentData> enchantments;
    
    public SmithingRecipeEnchantment(ResourceLocation recipeId, Ingredient base, Ingredient addition, List<EnchantmentData> enchantments) {
        
        super(recipeId, base, addition, ItemStack.EMPTY);
        this.enchantments = enchantments;
    }
    
    @Override
    public ItemStack getCraftingResult (IInventory inv) {
        
        final ItemStack stack = inv.getStackInSlot(0).copy();
        
        for (final EnchantmentData data : this.enchantments) {
            
            if (EnchantmentHelper.getEnchantmentLevel(data.enchantment, stack) < data.enchantmentLevel) {
                
                stack.addEnchantment(data.enchantment, data.enchantmentLevel);
            }
        }
        
        return stack;
    }
    
    @Override
    public boolean matches (IInventory inv, World world) {
        
        final ItemStack input = inv.getStackInSlot(0);
        
        for (final EnchantmentData data : this.enchantments) {
            
            if (EnchantmentHelper.getEnchantmentLevel(data.enchantment, input) < data.enchantmentLevel) {
                
                return super.matches(inv, world);
            }
        }
        
        return false;
    }
    
    @Override
    public boolean isDynamic () {
        
        return true;
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer () {
        
        return SERIALIZER;
    }
    
    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipeEnchantment> {
        
        @Override
        public SmithingRecipeEnchantment read (ResourceLocation recipeId, JsonObject json) {
            
            final Ingredient base = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
            final Ingredient addition = Ingredient.deserialize(JSONUtils.getJsonObject(json, "addition"));
            final List<EnchantmentData> enchants = Serializers.ENCHANTMENT_DATA.readList(json, "enchantments");
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }
        
        @Override
        public SmithingRecipeEnchantment read (ResourceLocation recipeId, PacketBuffer buffer) {
            
            final Ingredient base = Ingredient.read(buffer);
            final Ingredient addition = Ingredient.read(buffer);
            final List<EnchantmentData> enchants = Serializers.ENCHANTMENT_DATA.readList(buffer);
            return new SmithingRecipeEnchantment(recipeId, base, addition, enchants);
        }
        
        @Override
        public void write (PacketBuffer buffer, SmithingRecipeEnchantment recipe) {
            
            recipe.base.write(buffer);
            recipe.addition.write(buffer);
            Serializers.ENCHANTMENT_DATA.writeList(buffer, recipe.enchantments);
        }
    }
}