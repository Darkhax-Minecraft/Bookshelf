package net.darkhax.bookshelf.common.impl.data.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * A LootPool entry type that produces copies of predefined ItemStack.
 */
public class LootItemStack extends LootPoolSingletonContainer {

    public static final MapCodec<LootItemStack> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ItemStackTemplate.CODEC.fieldOf("item").forGetter(LootItemStack::getTemplate)).and(singletonFields(i)).apply(i, LootItemStack::new));

    private final ItemStackTemplate template;

    private LootItemStack(ItemStackTemplate template, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.template = template;
    }

    public ItemStackTemplate getTemplate() {
        return this.template;
    }

    @NotNull
    @Override
    public MapCodec<? extends LootPoolSingletonContainer> codec() {
        return MAP_CODEC;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, @NotNull LootContext context) {
        consumer.accept(this.template.create());
    }

    public static LootItemStack of(ItemStackTemplate template, int weight, int quality) {
        return new LootItemStack(template, weight, quality, List.of(), List.of());
    }

    public static LootItemStack of(ItemStackTemplate template, int weight) {
        return LootItemStack.of(template, weight, 0);
    }
}
