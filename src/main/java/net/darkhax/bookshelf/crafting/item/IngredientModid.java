package net.darkhax.bookshelf.crafting.item;

import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StackList;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientModid extends Ingredient {
    
    public static final Serializer SERIALIZER = new Serializer();
    
    private final String modid;
    
    private IngredientModid(String modid, Stream<? extends Ingredient.IItemList> itemLists) {
        
        super(itemLists);
        this.modid = modid;
    }
    
    @Override
    public boolean test (ItemStack stack) {
        
        return stack.getItem().getRegistryName().getNamespace().equals(this.modid);
    }
    
    @Override
    public boolean isSimple () {
        
        return false;
    }
    
    @Override
    public IIngredientSerializer<IngredientModid> getSerializer () {
        
        return SERIALIZER;
    }
    
    static class Serializer implements IIngredientSerializer<IngredientModid> {
        
        @Override
        public IngredientModid parse (PacketBuffer buffer) {
            
            final String modid = buffer.readString();
            return new IngredientModid(modid, Stream.generate( () -> new Ingredient.SingleItemList(buffer.readItemStack())).limit(buffer.readVarInt()));
        }
        
        @Override
        public IngredientModid parse (JsonObject json) {
            
            final String modid = JSONUtils.getString(json, "modid");
            return new IngredientModid(modid, Stream.of(new StackList(this.getMatchingItems(modid))));
        }
        
        @Override
        public void write (PacketBuffer buffer, IngredientModid ingredient) {
            
            buffer.writeString(ingredient.modid);
            
            final ItemStack[] items = ingredient.getMatchingStacks();
            buffer.writeVarInt(items.length);
            
            for (final ItemStack stack : items) {
                
                buffer.writeItemStack(stack);
            }
        }
        
        private List<ItemStack> getMatchingItems (String modid) {
            
            final List<ItemStack> matchingItems = NonNullList.create();
            
            for (final Item item : ForgeRegistries.ITEMS.getValues()) {
                
                if (item.getRegistryName().getNamespace().equals(modid)) {
                    
                    matchingItems.add(new ItemStack(item));
                }
            }
            
            return matchingItems;
        }
    }
}