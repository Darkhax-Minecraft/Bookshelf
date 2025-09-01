package net.darkhax.bookshelf.common.impl;

import net.darkhax.bookshelf.common.api.registry2.ContentProvider;
import net.darkhax.bookshelf.common.api.registry2.adapters.BlockRegistryAdapter;
import net.darkhax.bookshelf.common.api.registry2.adapters.GameRegistryAdapter;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.world.item.Item;
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
    public String namespace() {
        return Constants.MOD_ID;
    }

    @Override
    public boolean canLoad() {
        return Services.PLATFORM.isDevelopmentEnvironment();
    }
}