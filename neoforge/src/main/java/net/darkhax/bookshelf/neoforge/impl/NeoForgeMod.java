package net.darkhax.bookshelf.neoforge.impl;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.register.RegisterIngredient;
import net.darkhax.bookshelf.common.api.registry.register.RegisterItemTab;
import net.darkhax.bookshelf.common.api.registry.register.RegisterVillagerTrades;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.darkhax.bookshelf.neoforge.impl.data.NeoForgeIngredient;
import net.darkhax.bookshelf.neoforge.impl.network.NeoForgeNetworkHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mod(Constants.MOD_ID)
public class NeoForgeMod {

    public NeoForgeMod(IEventBus eventBus) {
        BookshelfMod.getInstance().init();
        NeoForge.EVENT_BUS.addListener(this::registerVillagerTrades);
        NeoForge.EVENT_BUS.addListener(this::registerWandererTrades);
        eventBus.addListener(this::onRegister);
        if (Services.NETWORK instanceof NeoForgeNetworkHandler handler) {
            eventBus.addListener(handler::registerPayloadHandlers);
        }
        if (Services.PLATFORM.isPhysicalClient()) {
            new NeoForgeModClient(eventBus);
        }
    }

    private void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
            event.register(Registries.CREATIVE_MODE_TAB, registerFunc -> {
                final BiConsumer<ResourceLocation, CreativeModeTab> func = registerFunc::register;
                Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerItemTabs(new RegisterItemTab(provider.contentNamespace(), func)));
            });
        }
        else if (event.getRegistryKey() == NeoForgeRegistries.Keys.INGREDIENT_TYPES) {
            event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES, registerFunc -> Services.CONTENT_PROVIDERS.get().forEach(provider -> {
                final RegisterIngredient registry = new RegisterIngredient(provider.contentNamespace(), (id, codec, stream) -> registerFunc.register(id, NeoForgeIngredient.makeIngredientType(id, codec, stream)));
                provider.registerIngredientTypes(registry);
            }));
        }
    }

    private final CachedSupplier<RegisterVillagerTrades> villagerTrades = CachedSupplier.cache(() -> {
        final RegisterVillagerTrades register = new RegisterVillagerTrades();
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerTrades(register));
        return register;
    });

    private void registerVillagerTrades(VillagerTradesEvent event) {
        final Multimap<Integer, VillagerTrades.ItemListing> newTrades = villagerTrades.get().getVillagerTrades().get(event.getType());
        if (newTrades != null && !newTrades.isEmpty()) {
            final Int2ObjectMap<List<VillagerTrades.ItemListing>> tradeData = event.getTrades();
            for (Map.Entry<Integer, VillagerTrades.ItemListing> entry : newTrades.entries()) {
                tradeData.computeIfAbsent(entry.getKey(), ArrayList::new).add(entry.getValue());
            }
        }
    }

    private void registerWandererTrades(WandererTradesEvent event) {
        villagerTrades.get().getCommonWanderingTrades().forEach(event.getGenericTrades()::add);
        villagerTrades.get().getRareWanderingTrades().forEach(event.getRareTrades()::add);
    }
}