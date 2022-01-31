package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.util.IClientHelper;
import net.darkhax.bookshelf.mixin.client.AccessorItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class ClientHelperFabric implements IClientHelper {

    @Override
    public void setRenderType(Block block, RenderType type) {

        AccessorItemBlockRenderTypes.getTypes().put(block, type);
    }
}