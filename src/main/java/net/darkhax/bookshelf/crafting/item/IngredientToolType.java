package net.darkhax.bookshelf.crafting.item;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StackList;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientToolType extends Ingredient {

    public static Serializer create (Predicate<Item> condition, @Nullable ToolType type) {

        final Predicate<Item> itemPred = i -> condition.test(i);
        final Predicate<ItemStack> stackPred = s -> !s.isEmpty() && (itemPred.test(s.getItem()) || type != null && StackUtils.hasToolType(type, s));
        return new Serializer(itemPred, stackPred);
    }

    private final Serializer serializer;
    private final Predicate<ItemStack> stackPred;

    private IngredientToolType (Predicate<ItemStack> stackPred, Serializer serializer, Stream<? extends Ingredient.Value> itemLists) {

        super(itemLists);
        this.serializer = serializer;
        this.stackPred = stackPred;
    }

    @Override
    public boolean test (ItemStack stack) {

        return this.stackPred.test(stack);
    }

    @Override
    public boolean isSimple () {

        return false;
    }

    @Override
    public IIngredientSerializer<IngredientToolType> getSerializer () {

        return this.serializer;
    }

    static class Serializer implements IIngredientSerializer<IngredientToolType> {

        final Predicate<Item> itemPred;
        final Predicate<ItemStack> stackPred;

        private IngredientToolType ingredient;

        private Serializer (Predicate<Item> itemPred, Predicate<ItemStack> stackPred) {

            this.itemPred = itemPred;
            this.stackPred = stackPred;
        }

        @Override
        public IngredientToolType parse (FriendlyByteBuf buffer) {

            return new IngredientToolType(this.stackPred, this, Stream.generate( () -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
        }

        @Override
        public IngredientToolType parse (JsonObject json) {

            if (this.ingredient == null) {

                this.ingredient = new IngredientToolType(this.stackPred, this, Stream.of(new StackList(this.getMatchingItems())));
            }

            return this.ingredient;
        }

        @Override
        public void write (FriendlyByteBuf buffer, IngredientToolType ingredient) {

            final ItemStack[] items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (final ItemStack stack : items) {

                buffer.writeItem(stack);
            }
        }

        private List<ItemStack> getMatchingItems () {

            final List<ItemStack> matchingItems = NonNullList.create();

            for (final Item item : ForgeRegistries.ITEMS.getValues()) {

                if (this.itemPred.test(item)) {

                    matchingItems.add(new ItemStack(item));
                }
            }

            return matchingItems;
        }
    }
}