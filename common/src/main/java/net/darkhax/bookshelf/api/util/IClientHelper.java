package net.darkhax.bookshelf.api.util;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public interface IClientHelper {

    void setRenderType(Block block, RenderType type);
}