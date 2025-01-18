package net.darkhax.bookshelf.common.api.registry.register;

import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record RegisterItemTab(String owner, BiConsumer<ResourceLocation, CreativeModeTab> registerFunc) {

    public void add(String path, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator display) {
        this.add(path, builder -> {
            builder.title(this.title(path));
            builder.icon(icon);
            builder.displayItems(display);
        });
    }

    public void add(String path, Supplier<ItemStack> icon) {
        this.add(path, builder -> {
            builder.title(title(path));
            builder.icon(icon);
        });
    }

    public void add(String path, Consumer<CreativeModeTab.Builder> builderFunc) {
        final CreativeModeTab.Builder builder = Services.GAMEPLAY.tabBuilder();
        builderFunc.accept(builder);
        this.add(path, builder.build());
    }

    public void add(String path, CreativeModeTab tab) {
        registerFunc.accept(ResourceLocation.fromNamespaceAndPath(this.owner, path), tab);
    }

    public Component title(String path) {
        return Component.translatable("itemGroup." + owner + "." + path);
    }
}