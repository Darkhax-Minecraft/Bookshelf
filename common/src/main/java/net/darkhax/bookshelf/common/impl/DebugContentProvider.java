package net.darkhax.bookshelf.common.impl;

import net.darkhax.bookshelf.common.api.entity.villager.MerchantTier;
import net.darkhax.bookshelf.common.api.entity.villager.trades.VillagerBuys;
import net.darkhax.bookshelf.common.api.registry.ContentProvider;
import net.darkhax.bookshelf.common.api.registry.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.bookshelf.common.impl.data.ingredient.AllOfIngredient;
import net.darkhax.bookshelf.common.impl.registry.adapter.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.CreativeModeTabAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.IngredientTypeAdapter;
import net.darkhax.bookshelf.common.impl.registry.adapter.VillagerTradeAdapter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class DebugContentProvider implements ContentProvider {

    @Override
    public void defineBlocks(BlockRegistryAdapter registry) {
        registry.add("test_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
        registry.addPlaceable("test_placeable", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
    }

    @Override
    public void defineItems(GameRegistryAdapter<Item> registry) {
        registry.add("test_item", () -> new Item(new Item.Properties()));
    }

    @Override
    public void defineCreativeTabs(CreativeModeTabAdapter registry) {
        registry.add("test_tab", Items.BOOKSHELF::getDefaultInstance, (params, output) -> {
            output.accept(Items.BOOKSHELF);
            BuiltInRegistries.ITEM.keySet().stream().filter(id -> id.getNamespace().equalsIgnoreCase(Constants.MOD_ID)).forEach(id -> output.accept(BuiltInRegistries.ITEM.get(id)));
        });
    }

    @Override
    public void defineTrades(VillagerTradeAdapter registry) {
        registry.addTrade(VillagerProfession.ARMORER, MerchantTier.NOVICE, new VillagerBuys(() -> new ItemCost(Items.BEDROCK, 1), 1, 1, 0, 0));
        registry.addCommonWanderingTrade(new VillagerBuys(() -> new ItemCost(Items.BARRIER, 1), 1, 1, 0, 0));
        registry.addRareWanderingTrade(new VillagerBuys(() -> new ItemCost(Items.STRUCTURE_VOID, 1), 1, 1, 0, 0));
    }

    @Override
    public void defineIngredientTypes(IngredientTypeAdapter registry) {
        registry.add("test_all", AllOfIngredient.CODEC, AllOfIngredient.STREAM);
    }

    @Override
    public String namespace() {
        return Constants.MOD_ID;
    }

    @Override
    public boolean canLoad() {
        final boolean canLoad = Services.PLATFORM.isDevelopmentEnvironment();
        if (canLoad) {
            Constants.LOG.warn("Developer mode is enabled! Bookshelf will load its debug content!");
            Constants.LOG.warn("Bookshelf's debug content will affect the gameplay experience!");
            Constants.LOG.warn("If you are not in a development environment you really should disable developer mode!");
            Constants.LOG.warn("If you are a developer you can ignore this message.");
        }
        return canLoad;
    }
}