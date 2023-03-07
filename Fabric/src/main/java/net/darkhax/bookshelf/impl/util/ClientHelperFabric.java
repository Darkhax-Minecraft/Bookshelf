package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.util.IClientHelper;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class ClientHelperFabric implements IClientHelper {

    @Override
    public void setRenderType(Block block, RenderType type) {

        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }
}