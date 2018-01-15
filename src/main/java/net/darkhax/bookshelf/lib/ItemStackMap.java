package net.darkhax.bookshelf.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.item.ItemStack;

/**
 * This implementation of HashMap allows for an ItemStack to be used as a key. This version of
 * the map uses IStackComparator which allows for custom equivalence checks to be made. This
 * allows for item stacks which are similar, but not actually the same object to be considered
 * equal.
 */
public class ItemStackMap<T> extends HashMap<ItemStack, T> {

    /**
     * This comparator will mark stacks as equal if they have the same item and metadata.
     */
    public static final IStackComparator SIMILAR = (stack, obj) -> {

        return obj instanceof ItemStack ? StackUtils.areStacksSimilar(stack, (ItemStack) obj) : false;
    };

    /**
     * This comparator will mark stacks as equal if they have the same item, metadata, and
     * stack size.
     */
    public static final IStackComparator SIMILAR_WITH_SIZE = (stack, obj) -> {

        return obj instanceof ItemStack ? StackUtils.areStacksSimilarWithSize(stack, (ItemStack) obj) : false;
    };

    /**
     * This comparator will mark stacks as equal if they have the same item, metadata, and the
     * first stack has all of the nbt of the second stack.
     */
    public static final IStackComparator SIMILAR_WITH_NBT = (stack, obj) -> {

        return obj instanceof ItemStack ? StackUtils.areStacksSimilarWithPartialNBT(stack, (ItemStack) obj) : false;
    };

    /**
     * The stack comparator instance.
     */
    private final IStackComparator comparator;

    /**
     * Constructor for an item stack map. This map allows itemstacks to be used as a key.
     *
     * @param comparator This is required to determine how map similarities are determined.
     *        There are some default instances in the class that can be used like
     *        {@link #SIMILAR}.
     */
    public ItemStackMap (IStackComparator comparator) {

        this.comparator = comparator;
    }

    @Override
    public boolean containsKey (Object key) {

        for (final Map.Entry<ItemStack, T> entry : this.entrySet()) {

            if (this.comparator.isValid(entry.getKey(), key)) {

                return true;
            }
        }

        return super.containsKey(key);
    }

    @Override
    public T remove (Object key) {

        final Iterator<Map.Entry<ItemStack, T>> iterator = this.entrySet().iterator();

        while (iterator.hasNext()) {

            final Map.Entry<ItemStack, T> entry = iterator.next();

            if (this.comparator.isValid(entry.getKey(), key)) {

                iterator.remove();
            }
        }

        return super.remove(key);
    }

    @Override
    public T get (Object key) {

        for (final Map.Entry<ItemStack, T> entry : this.entrySet()) {

            if (this.comparator.isValid(entry.getKey(), key)) {

                return entry.getValue();
            }
        }

        return super.get(key);
    }

    /**
     * Functional interface for comparing item stacks for the purpose of map sorting.
     */
    public interface IStackComparator {

        /**
         * Checks if something is valid for the map.
         *
         * @param first The first itemstack to compare.
         * @param second The second object to compare.
         * @return Whether or not the objects are equal enough to be considered the same.
         */
        boolean isValid (ItemStack first, Object second);
    }
}