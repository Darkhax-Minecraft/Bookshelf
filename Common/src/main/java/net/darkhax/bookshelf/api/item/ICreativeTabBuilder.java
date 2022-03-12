package net.darkhax.bookshelf.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ICreativeTabBuilder {

    default ICreativeTabBuilder setIcon(Supplier<? extends ItemLike> iconSupplier) {

        return this.setIconStack(() -> new ItemStack(iconSupplier.get()));
    }

    ICreativeTabBuilder setIconStack(Supplier<ItemStack> iconSupplier);

    ICreativeTabBuilder setEnchantmentCategories(EnchantmentCategory... categories);

    default ICreativeTabBuilder setTabContents(List<ItemStack> items) {

        return setTabContents(tabContents -> tabContents.addAll(items));
    }

    ICreativeTabBuilder setTabContents(Consumer<List<ItemStack>> contentSupplier);

    CreativeModeTab build();

    ResourceLocation getTabId();
}