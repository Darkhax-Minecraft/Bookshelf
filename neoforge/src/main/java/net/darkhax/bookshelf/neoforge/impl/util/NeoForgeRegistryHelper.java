package net.darkhax.bookshelf.neoforge.impl.util;

import net.darkhax.bookshelf.common.api.registry2.ContentProvider;
import net.darkhax.bookshelf.common.api.registry2.RegistrationContext;
import net.darkhax.bookshelf.common.api.registry2.adapters.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry2.adapters.GameRegistryAdapter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class NeoForgeRegistryHelper {

    private final ContentProvider content;
    private final RegistrationContext context;

    public NeoForgeRegistryHelper(ContentProvider content) {
        this.content = content;
        this.context = new RegistrationContext(content.namespace());
        getModBus(content.namespace()).addListener(this::registerContent);
    }

    private void registerContent(RegisterEvent event) {
        event.register(Registries.BLOCK, helper -> this.content.defineBlocks(new BlockRegistryAdapter(this.context, adapt(helper))));
        event.register(Registries.ITEM, helper -> {
            this.context.getPlaceableBlocks().forEach((blockRef, builder) -> helper.register(blockRef.key().location(), builder.apply(blockRef.value().get())));
            this.content.defineItems(new GameRegistryAdapter<>(this.context, Registries.ITEM, adapt(helper)));
        });
    }

    private static <T> BiConsumer<ResourceKey<T>, Supplier<T>> adapt(RegisterEvent.RegisterHelper<T> helper) {
        return (key, value) -> helper.register(key, value.get());
    }

    private static IEventBus getModBus(String modid) {
        final ModContainer container = ModList.get().getModContainerById(modid).orElseThrow(() -> new IllegalArgumentException("Could not find mod '" + modid + "'."));
        if (container instanceof FMLModContainer fmlContainer) {
            final IEventBus modEventBus = fmlContainer.getEventBus();
            if (modEventBus != null) {
                return modEventBus;
            }
            throw new IllegalStateException("Mod '" + modid + "' does not have an event bus!");
        }
        throw new IllegalStateException("Mod '" + modid + "' is not an FML mod!");
    }
}
