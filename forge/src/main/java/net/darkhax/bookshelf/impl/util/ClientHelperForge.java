package net.darkhax.bookshelf.impl.util;

import net.darkhax.bookshelf.api.util.IClientHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class ClientHelperForge implements IClientHelper {

    @Override
    public void setRenderType(Block block, RenderType type) {

        ItemBlockRenderTypes.setRenderLayer(block, type);
    }
}
