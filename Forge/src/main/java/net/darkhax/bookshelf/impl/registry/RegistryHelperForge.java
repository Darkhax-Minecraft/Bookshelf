package net.darkhax.bookshelf.impl.registry;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.darkhax.bookshelf.api.item.ICreativeTabBuilder;
import net.darkhax.bookshelf.api.registry.IRegistryEntries;
import net.darkhax.bookshelf.api.registry.RegistryHelper;
import net.darkhax.bookshelf.impl.item.CreativeTabBuilderForge;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RegistryHelperForge extends RegistryHelper {

    public RegistryHelperForge(String ownerId) {

        super(ownerId);
    }

    @Override
    public ICreativeTabBuilder<?> createTabBuilder(String tabId) {

        return new CreativeTabBuilderForge(new ResourceLocation(this.ownerId, tabId));
    }

    @Override
    public void init() {

        consumeRegistry(Block.class, this.blocks);
        consumeRegistry(Item.class, this.items);
        consumeRegistry(Enchantment.class, this.enchantments);
        consumeRegistry(Motive.class, this.paintings);
        consumeRegistry(MobEffect.class, this.mobEffects);
        consumeRegistry(Attribute.class, this.attributes);
        consumeRegistry(VillagerProfession.class, this.villagerProfessions);

        consumeWithModEvent(RegisterClientReloadListenersEvent.class, this.clientReloadListeners, e -> this.clientReloadListeners.getEntries().values().forEach(e::registerReloadListener));
        consumeWithEvent(AddReloadListenerEvent.class, this.serverReloadListeners, e -> this.serverReloadListeners.getEntries().values().forEach(e::addListener));

        MinecraftForge.EVENT_BUS.addListener(this::buildCommands);
        MinecraftForge.EVENT_BUS.addListener(this::registerVillagerTrades);
        MinecraftForge.EVENT_BUS.addListener(this::registerWanderingTrades);
    }

    private void registerVillagerTrades(VillagerTradesEvent event) {

        final Multimap<Integer, VillagerTrades.ItemListing> newTrades = this.trades.getVillagerTrades().get(event.getType());

        if (newTrades != null) {

            final Int2ObjectMap<List<VillagerTrades.ItemListing>> tradeData = event.getTrades();

            for (Map.Entry<Integer, VillagerTrades.ItemListing> entry : newTrades.entries()) {

                tradeData.computeIfAbsent(entry.getKey(), ArrayList::new).add(entry.getValue());
            }
        }
    }

    private void registerWanderingTrades(WandererTradesEvent event) {

        event.getGenericTrades().addAll(this.trades.getCommonWanderingTrades());
        event.getRareTrades().addAll(this.trades.getRareWanderingTrades());
    }

    private void buildCommands(RegisterCommandsEvent event) {

        final CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        final boolean isDedicated = event.getEnvironment() == Commands.CommandSelection.DEDICATED;
        this.commands.forEach(builder -> builder.build(dispatcher, isDedicated));
    }

    private <T extends IForgeRegistryEntry<T>> void consumeRegistry(Class<T> clazz, IRegistryEntries<T> registry) {

        if (!registry.isEmpty()) {

            final Consumer<RegistryEvent.Register<T>> listener = event -> {

                registry.getEntries().forEach((id, value) -> {

                    if (value.getRegistryName() == null) {

                        value.setRegistryName(id);
                    }

                    event.getRegistry().register(value);
                });
            };

            FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(clazz, listener);
        }
    }

    private <T, E extends Event> void consumeWithModEvent(Class<E> clazz, IRegistryEntries<T> registry, Consumer<E> listener) {

        if (!registry.isEmpty()) {

            FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, clazz, listener);
        }
    }

    private <T, E extends Event> void consumeWithEvent(Class<E> clazz, IRegistryEntries<T> registry, Consumer<E> listener) {

        if (!registry.isEmpty()) {

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, clazz, listener);
        }
    }
}