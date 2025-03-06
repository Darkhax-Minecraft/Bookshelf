package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public record RegisterItem(String owner, BiConsumer<ResourceLocation, Item> registerFunc) {

    public void addBlock(Block block) {
        final ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (!id.getNamespace().equals(owner)) {
            throw new IllegalArgumentException("You can only register item blocks for blocks in your namespace!");
        }
        this.add(id.getPath(), new BlockItem(block, new Item.Properties()));
    }

    public void add(String path) {
        this.add(path, new Item.Properties());
    }

    public void add(String path, Item.Properties properties) {
        this.add(path, new Item(properties));
    }

    public void add(String path, Item item) {
        this.registerFunc.accept(ResourceLocation.fromNamespaceAndPath(this.owner, path), item);
    }

    public void add(ResourceLocation id) {
        this.add(id, new Item.Properties());
    }

    public void add(ResourceLocation id, Item.Properties properties) {
        this.add(id, new Item(properties));
    }

    public void add(ResourceLocation id, Item item) {
        this.registerFunc.accept(id, item);
    }
}