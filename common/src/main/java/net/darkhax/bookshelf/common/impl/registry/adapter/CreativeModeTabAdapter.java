package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.darkhax.bookshelf.common.api.registry.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeModeTabAdapter extends GameRegistryAdapter<CreativeModeTab> {

    public CreativeModeTabAdapter(RegistrationContext context, ResourceKey<Registry<CreativeModeTab>> regKey, BiConsumer<ResourceKey<CreativeModeTab>, Supplier<CreativeModeTab>> registryFunc) {
        super(context, regKey, registryFunc);
    }

    /**
     * Adds a new creative mode tab to the game. The title will be based on the registry ID of the tab.
     *
     * @param key     The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param icon    An item to display as the icon for the tab.
     * @param display Generates the items to display in the tab.
     */
    public void add(String key, Supplier<ItemStack> icon, OutputBuilder display) {
        this.add(key, builder -> {
            builder.title(Component.translatable("itemGroup." + this.context.namespace() + "." + key));
            builder.icon(icon);
            Services.GAMEPLAY.setTabOutputs(builder, display);
        });
    }

    /**
     * Adds a new creative mode tab to the game.
     *
     * @param key         The ID to register the value under. This ID only needs to be unique within your namespace.
     * @param builderFunc A creative mode tab builder.
     */
    public void add(String key, Consumer<CreativeModeTab.Builder> builderFunc) {
        final CreativeModeTab.Builder builder = Services.GAMEPLAY.tabBuilder();
        builderFunc.accept(builder);
        this.add(key, builder.build());
    }

    @FunctionalInterface
    public interface OutputBuilder {
        void build(CreativeModeTab.ItemDisplayParameters params, OutputWrapper output);
    }

    /**
     * Wraps an Output instances from a loader specific context.
     */
    public interface OutputWrapper {

        void accept(ItemStack stack);

        void accept(ItemLike item);

        void acceptAll(Collection<ItemStack> stacks);
    }
}