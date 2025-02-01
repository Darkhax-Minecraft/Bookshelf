package net.darkhax.bookshelf.forge.impl;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.common.api.function.CachedSupplier;
import net.darkhax.bookshelf.common.api.registry.register.RegisterIngredient;
import net.darkhax.bookshelf.common.api.registry.register.RegisterItemTab;
import net.darkhax.bookshelf.common.api.registry.register.RegisterVillagerTrades;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.BookshelfMod;
import net.darkhax.bookshelf.common.impl.Constants;
import net.darkhax.bookshelf.forge.impl.data.ForgeIngredient;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mod(Constants.MOD_ID)
public class ForgeMod {

    public ForgeMod(FMLJavaModLoadingContext loadingContext) {
        BookshelfMod.getInstance().init();
        loadingContext.getModEventBus().addListener(this::onRegister);
        MinecraftForge.EVENT_BUS.addListener(this::registerVillagerTrades);
        MinecraftForge.EVENT_BUS.addListener(this::registerWandererTrades);
        if (Services.PLATFORM.isPhysicalClient()) {
            new ForgeModClient(loadingContext);
        }
    }

    private void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB && event.getVanillaRegistry() != null) {
            final BiConsumer<ResourceLocation, CreativeModeTab> registerFunc = (rl, tab) -> Registry.register(event.getVanillaRegistry(), rl, tab);
            Services.CONTENT_PROVIDERS.get().forEach(provider -> {
                provider.registerItemTabs(new RegisterItemTab(provider.contentNamespace(), registerFunc));
            });
        }
        else if (event.getRegistryKey() == ForgeRegistries.Keys.INGREDIENT_SERIALIZERS) {
            event.register(ForgeRegistries.Keys.INGREDIENT_SERIALIZERS, registerFunc -> Services.CONTENT_PROVIDERS.get().forEach(provider -> {
                final RegisterIngredient registry = new RegisterIngredient(provider.contentNamespace(), (id, codec, stream) -> registerFunc.register(id, ForgeIngredient.makeIngredientType(id, codec, stream)));
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