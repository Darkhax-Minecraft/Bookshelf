package net.darkhax.bookshelf.impl.item;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabBuilderFabric implements ICreativeTabBuilder {

    private final ResourceLocation id;
    private final Set<EnchantmentCategory> enchantmentCategories = new HashSet<>();
    private final List<Consumer<List<ItemStack>>> tabContents = new ArrayList<>();

    private Supplier<ItemStack> iconSupplier;

    private final FabricItemGroupBuilder builder;

    public CreativeTabBuilderFabric(ResourceLocation id) {

        this.builder = FabricItemGroupBuilder.create(id);
        this.id = id;
    }

    @Override
    public CreativeTabBuilderFabric setIconStack(Supplier<ItemStack> iconSupplier) {

        this.builder.icon(iconSupplier);
        return this;
    }

    @Override
    public CreativeTabBuilderFabric setEnchantmentCategories(EnchantmentCategory... categories) {

        enchantmentCategories.addAll(Arrays.asList(categories));
        return this;
    }

    @Override
    public CreativeTabBuilderFabric setTabContents(Consumer<List<ItemStack>> contentSupplier) {

        this.tabContents.add(contentSupplier);
        return this;
    }

    @Override
    public CreativeModeTab build() {

        if (!this.tabContents.isEmpty()) {

            this.builder.appendItems(contentList -> tabContents.forEach(l -> l.accept(contentList)));
        }

        final CreativeModeTab tab = this.builder.build();

        tab.setEnchantmentCategories(enchantmentCategories.toArray(new EnchantmentCategory[0]));

        return tab;
    }

    @Override
    public ResourceLocation getTabId() {

        return this.id;
    }
}
