package net.darkhax.bookshelf.crafting.item;

import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StackList;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientEnchantmentType extends Ingredient {
    
    public static Serializer create (EnchantmentType type) {
        
        return new Serializer(type);
    }
    
    private final Serializer serializer;
    private final EnchantmentType type;
    
    private IngredientEnchantmentType(EnchantmentType type, Serializer serializer, Stream<? extends Ingredient.IItemList> itemLists) {
        
        super(itemLists);
        this.serializer = serializer;
        this.type = type;
    }
    
    @Override
    public boolean test (ItemStack stack) {
        
        return this.type.canEnchantItem(stack.getItem());
    }
    
    @Override
    public boolean isSimple () {
        
        return false;
    }
    
    @Override
    public IIngredientSerializer<IngredientEnchantmentType> getSerializer () {
        
        return this.serializer;
    }
    
    static class Serializer implements IIngredientSerializer<IngredientEnchantmentType> {
        
        private final EnchantmentType type;
        private IngredientEnchantmentType ingredient;
        
        private Serializer(EnchantmentType type) {
            
            this.type = type;
        }
        
        @Override
        public IngredientEnchantmentType parse (PacketBuffer buffer) {
            
            return new IngredientEnchantmentType(this.type, this, Stream.generate( () -> new Ingredient.SingleItemList(buffer.readItemStack())).limit(buffer.readVarInt()));
        }
        
        @Override
        public IngredientEnchantmentType parse (JsonObject json) {
            
            if (this.ingredient == null) {
                
                this.ingredient = new IngredientEnchantmentType(this.type, this, Stream.of(new StackList(this.getMatchingItems())));
            }
            
            return this.ingredient;
        }
        
        @Override
        public void write (PacketBuffer buffer, IngredientEnchantmentType ingredient) {
            
            final ItemStack[] items = ingredient.getMatchingStacks();
            buffer.writeVarInt(items.length);
            
            for (final ItemStack stack : items) {
                
                buffer.writeItemStack(stack);
            }
        }
        
        private List<ItemStack> getMatchingItems () {
            
            final List<ItemStack> matchingItems = NonNullList.create();
            
            for (final Item item : ForgeRegistries.ITEMS.getValues()) {
                
                if (this.type.canEnchantItem(item)) {
                    
                    matchingItems.add(new ItemStack(item));
                }
            }
            
            return matchingItems;
        }
    }
}