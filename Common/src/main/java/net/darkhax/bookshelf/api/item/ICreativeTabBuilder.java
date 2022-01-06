package net.darkhax.bookshelf.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ICreativeTabBuilder <BT extends ICreativeTabBuilder<BT>>{

    default BT setIcon(ItemLike icon) {

        return this.setIcon(new ItemStack(icon));
    }

    default BT setIcon(ItemStack icon) {

        return setIcon(() -> icon);
    }

    BT setIcon(Supplier<ItemStack> iconSupplier);

    BT setEnchantmentCategories(EnchantmentCategory... categories);

    default BT setTabContents(List<ItemStack> items) {

        return setTabContents(tabContents -> tabContents.addAll(items));
    }

    BT setTabContents(Consumer<List<ItemStack>> contentSupplier);

    CreativeModeTab build();

    ResourceLocation getTabId();
}