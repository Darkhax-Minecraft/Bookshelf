package net.darkhax.bookshelf.common.impl.data.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.impl.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A LootPool entry type that produces copies of predefined ItemStack.
 */
public class LootItemStack extends LootPoolSingletonContainer {

    private static final Supplier<LootPoolEntryType> TYPE = CachedSupplier.of(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, Constants.id("item_stack"));
    public static final MapCodec<LootItemStack> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(MapCodecs.ITEM_STACK.get("item", LootItemStack::getBaseStack))
            .and(singletonFields(instance))
            .apply(instance, LootItemStack::new)
    );

    private final ItemStack baseStack;

    private LootItemStack(ItemStack baseStack, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.baseStack = baseStack;
    }

    public ItemStack getBaseStack() {
        return this.baseStack.copy();
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, @NotNull LootContext context) {
        consumer.accept(this.baseStack.copy());
    }

    @NotNull
    @Override
    public LootPoolEntryType getType() {
        return TYPE.get();
    }

    public static LootItemStack of(ItemStack baseStack, int weight) {
        return new LootItemStack(baseStack, weight, 0, List.of(), List.of());
    }

    public static LootItemStack of(ItemStack baseStack, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        return new LootItemStack(baseStack, weight, quality, conditions, functions);
    }
}
