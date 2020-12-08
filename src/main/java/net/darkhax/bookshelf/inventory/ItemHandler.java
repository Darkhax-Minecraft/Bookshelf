package net.darkhax.bookshelf.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.ItemStackHandler;

/**
 * An implementation of ItemStackHandler that combines lambdas with a build-esq pattern.
 *
 * @param <T> The handler's own type. This is used to spare devs from having to cast their
 *        after using a builder method.
 */
public class ItemHandler<T extends ItemHandler<T>> extends ItemStackHandler {
    
    /**
     * Function used to derive the max stack size of a slot. This is a stand in for
     * {@link #getSlotLimit(int)}.
     */
    private IntFunction<Integer> maxSize = super::getSlotLimit;
    
    /**
     * Predicate used to check if an item can be inserted into a slot. This is a stand in for
     * {@link #isItemValid(int, ItemStack)}.
     */
    private BiPredicate<Integer, ItemStack> validItems = super::isItemValid;
    
    /**
     * Listeners which detect changes to the internal inventory.
     */
    private final List<Consumer<Integer>> changeListener = new ArrayList<>();
    
    /**
     * Creates a new item handler with only one slot.
     */
    public ItemHandler() {
        
        this(1);
    }
    
    /**
     * Creates a new item handler with any number of slots.
     * 
     * @param invSize The amount of slots to track.
     */
    public ItemHandler(int invSize) {
        
        super(invSize);
        this.withChangeListener(super::onContentsChanged);
    }
    
    /**
     * Sets the max stack size of all slots to a single value.
     * 
     * @param maxSize The max stack size for every slot.
     * @return The same item handler.
     */
    public T withMaxSize (int maxSize) {
        
        return this.withMaxSize(slot -> maxSize);
    }
    
    /**
     * Sets the max stack size for all slots using an int array. If a slot ID is not within the
     * bounds of the array a max size of 0 will be used. See {@link #withMaxSize(int[], int)}
     * for custom default values.
     * 
     * @param maxSizes An array of max stack sizes based on the slot index.
     * @return The same item handler.
     */
    public T withMaxSize (int[] maxSizes) {
        
        return this.withMaxSize(maxSizes, 0);
    }
    
    /**
     * Sets the max stack size for all slots using an int array. If a slot ID is not within the
     * bounds of the array the default size will be used.
     * 
     * @param maxSizes An array of max stack sizes based on the slot index.
     * @param defaultSize The default max stack size used when the slot index is out of bounds.
     * @return The same item handler.
     */
    public T withMaxSize (int[] maxSizes, int defaultSize) {
        
        return this.withMaxSize(slot -> slot >= 0 && slot < maxSizes.length ? maxSizes[slot] : defaultSize);
    }
    
    /**
     * Sets a function that is used to determine the maximum stack size of a given slot.
     * 
     * @param maxSizeFunc The function that determines stack size for a given slot index.
     * @return The same item handler.
     */
    public T withMaxSize (IntFunction<Integer> maxSizeFunc) {
        
        this.maxSize = maxSizeFunc;
        return this.self();
    }
    
    /**
     * Sets the inventory to blanket allow/deny any item for every slot in the inventory.
     * 
     * @param isValid Whether or not every item is valid/invalid for insertion.
     * @return The same item handler.
     */
    public T withItemValidator (boolean isValid) {
        
        return this.withItemValidator( (slot, stack) -> isValid);
    }
    
    /**
     * Sets the inventory to only accept item stacks from a given set of items.
     * 
     * @param items The items to accept.
     * @return The same item handler.
     */
    public T withItemValidator (IItemProvider... items) {
        
        return this.withItemValidator(Ingredient.fromItems(items));
    }
    
    /**
     * Sets the inventory to only accept item stacks from a given item tag.
     * 
     * @param tag The tag to check.
     * @return The same item handler.
     */
    public T withItemValidator (ITag<Item> tag) {
        
        return this.withItemValidator(Ingredient.fromTag(tag));
    }
    
    /**
     * Sets a function to test the insertion validity of each individual item stack.
     * 
     * @param validator The validation predicate.
     * @return The same item handler.
     */
    public T withItemValidator (Predicate<ItemStack> validator) {
        
        return this.withItemValidator( (slot, stack) -> validator.test(stack));
    }
    
    /**
     * Sets a function to test the insertion validity of an item stack into a given slot.
     * 
     * @param validator A predicate which accepts the slot index and the stack being inserted
     *        and returns the results of an insertion validity check.
     * @return The same item handler.
     */
    public T withItemValidator (BiPredicate<Integer, ItemStack> validator) {
        
        this.validItems = validator;
        return this.self();
    }
    
    /**
     * Adds a listener function that will be invoked every time the inventory is changed.
     * 
     * @param listener The listener function.
     * @return The same item handler.
     */
    public T withChangeListener (Runnable listener) {
        
        return this.withChangeListener(i -> listener.run());
    }
    
    /**
     * Adds a listener that will be invoked every time the inventory is changed.
     * 
     * @param listener A biconsumer that accepts the slot index and the stack that is now in
     *        the slot.
     * @return The same item handler.
     */
    public T withChangeListener (BiConsumer<Integer, ItemStack> listener) {
        
        return this.withChangeListener(slot -> listener.accept(slot, this.getStackInSlot(slot)));
    }
    
    /**
     * Adds a listener that will be invoked every time the inventory is changed.
     * 
     * @param listener A consumer that accepts the slot index that was modified.
     * @return The same item handler.
     */
    public T withChangeListener (Consumer<Integer> listener) {
        
        this.changeListener.add(listener);
        return this.self();
    }
    
    @Override
    public int getSlotLimit (int slot) {
        
        return this.maxSize.apply(slot);
    }
    
    @Override
    public boolean isItemValid (int slot, ItemStack stack) {
        
        return this.validItems.test(slot, stack);
    }
    
    @Override
    public void onContentsChanged (int slot) {
        
        this.changeListener.forEach(listener -> listener.accept(slot));
    }
    
    @SuppressWarnings("unchecked")
    public T self () {
        
        return (T) this;
    }
}