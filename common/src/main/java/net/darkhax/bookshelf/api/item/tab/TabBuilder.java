package net.darkhax.bookshelf.api.item.tab;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TabBuilder implements ITabBuilder {

    private final CreativeModeTab.Builder delegate;

    public TabBuilder(CreativeModeTab.Builder delegate) {

        this.delegate = delegate;
    }

    @Override
    public ITabBuilder title(Component tabTitle) {

        delegate.title(tabTitle);
        return this;
    }

    @Override
    public ITabBuilder icon(Supplier<ItemStack> supplier) {

        this.delegate.icon(supplier);
        return this;
    }

    @Override
    public ITabBuilder displayItems(IDisplayGenerator displayGen) {

        this.delegate.displayItems(Services.CONSTRUCTS.wrapDisplayGen(displayGen));
        return this;
    }

    @Override
    public ITabBuilder hideTitle() {

        this.delegate.hideTitle();
        return this;
    }

    @Override
    public ITabBuilder noScrollBar() {

        this.delegate.noScrollBar();
        return this;
    }

    @Override
    public ITabBuilder backgroundSuffix(String background) {

        this.delegate.backgroundSuffix(background);
        return this;
    }

    @Override
    public CreativeModeTab build() {

        return this.delegate.build();
    }
}