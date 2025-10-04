package net.darkhax.bookshelf.common.impl.registry.adapter;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public record BlockRenderTypeAdapter(BiConsumer<Block, RenderType> bindFunc) {

    public void add(Block block, RenderType type) {
        this.bindFunc.accept(block, type);
    }
}