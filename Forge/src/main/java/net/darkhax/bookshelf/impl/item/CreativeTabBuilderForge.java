package net.darkhax.bookshelf.impl.item;

import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.minecraft.core.NonNullList;
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

public class CreativeTabBuilderForge implements ICreativeTabBuilder {

    private final ResourceLocation id;
    private final Set<EnchantmentCategory> enchantmentCategories = new HashSet<>();
    private final List<Consumer<List<ItemStack>>> tabContents = new ArrayList<>();

    private Supplier<ItemStack> iconSupplier;

    public CreativeTabBuilderForge(ResourceLocation id) {

        this.id = id;
    }

    @Override
    public CreativeTabBuilderForge setIconStack(Supplier<ItemStack> iconSupplier) {

        this.iconSupplier = iconSupplier;
        return this;
    }

    @Override
    public CreativeTabBuilderForge setEnchantmentCategories(EnchantmentCategory... categories) {

        enchantmentCategories.addAll(Arrays.asList(categories));
        return this;
    }

    @Override
    public CreativeTabBuilderForge setTabContents(Consumer<List<ItemStack>> contentSupplier) {

        this.tabContents.add(contentSupplier);
        return this;
    }

    @Override
    public CreativeModeTab build() {

        CreativeModeTab tab = new CreativeModeTab(this.id.getNamespace() + "." + this.id.getPath()) {

            @Override
            public ItemStack makeIcon() {

                return iconSupplier.get();
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> stackContents) {

                for (Consumer<List<ItemStack>> contentSupplier : tabContents) {

                    contentSupplier.accept(stackContents);
                }

                super.fillItemList(stackContents);
            }
        };

        tab.setEnchantmentCategories(enchantmentCategories.toArray(new EnchantmentCategory[0]));

        return tab;
    }

    @Override
    public ResourceLocation getTabId() {

        return this.id;
    }
}
