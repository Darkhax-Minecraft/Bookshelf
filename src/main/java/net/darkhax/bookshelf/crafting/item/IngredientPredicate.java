package net.darkhax.bookshelf.crafting.item;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StackList;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * An ingredient implementation which checks all registered items against a code-defined
 * predicate to build the list of matching items.
 */
public class IngredientPredicate extends Ingredient {
    
    /**
     * This ingredient implementation is polymorphic so it needs to keep reference of the
     * serializer that created it for {@link #getSerializer()}.
     */
    private final Serializer serializer;
    
    private IngredientPredicate(Serializer serializer, Stream<? extends Ingredient.IItemList> itemLists) {
        
        super(itemLists);
        this.serializer = serializer;
    }
    
    @Override
    public boolean isSimple () {
        
        return false;
    }
    
    @Override
    public IIngredientSerializer<IngredientPredicate> getSerializer () {
        
        return this.serializer;
    }
    
    /**
     * Creates a new serializer that will create ingredients which use the provided predicate.
     * 
     * @param predicate The predicate used to check for items.
     * @return A serializer that creates ingredients for your predicate.
     */
    public static Serializer create (Predicate<Item> predicate) {
        
        return new Serializer(predicate);
    }
    
    static class Serializer implements IIngredientSerializer<IngredientPredicate> {
        
        /**
         * The predicate used to check for valid items.
         */
        private final Predicate<Item> itemPredicate;
        
        /**
         * Cache of the ingredient for performance reasons. Predicates must be deterministic so
         * this only needs to be calculated once per resource load.
         */
        private IngredientPredicate ingredient;
        
        /**
         * This constructor is intentionally restricted. Use
         * {@link IngredientPredicate#create(Predicate)}.
         */
        private Serializer(Predicate<Item> itemPredicate) {
            
            this.itemPredicate = itemPredicate;
        }
        
        @Override
        public IngredientPredicate parse (PacketBuffer buffer) {
            
            // This code is where the client handles reading the ingredient from the server.
            // The client should not be trusted to generate the ingredient cache so it is
            // instead based on a stream of items sent from the server.
            return new IngredientPredicate(this, Stream.generate( () -> new Ingredient.SingleItemList(buffer.readItemStack())).limit(buffer.readVarInt()));
        }
        
        @Override
        public IngredientPredicate parse (JsonObject json) {
            
            // This code is where the server creates the ingredient from the JSON data. We only
            // need one instance per data/resource load so we make and use a cache of the
            // ingredient.
            if (this.ingredient == null) {
                
                this.ingredient = new IngredientPredicate(this, Stream.of(new StackList(this.getMatchingItems())));
            }
            
            return this.ingredient;
        }
        
        @Override
        public void write (PacketBuffer buffer, IngredientPredicate ingredient) {
            
            final ItemStack[] items = ingredient.getMatchingStacks();
            buffer.writeVarInt(items.length);
            
            for (final ItemStack stack : items) {
                
                buffer.writeItemStack(stack);
            }
        }
        
        private List<ItemStack> getMatchingItems () {
            
            final List<ItemStack> matchingItems = NonNullList.create();
            
            for (final Item item : ForgeRegistries.ITEMS.getValues()) {
                
                if (this.itemPredicate.test(item)) {
                    
                    matchingItems.add(new ItemStack(item));
                }
            }
            
            return matchingItems;
        }
    }
}