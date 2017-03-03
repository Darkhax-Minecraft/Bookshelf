package net.darkhax.bookshelf.client.model;

import com.google.common.collect.ImmutableMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileEntityRender<T extends TileEntity> {

    @SideOnly(Side.CLIENT)
    ImmutableMap<String, String> getRenderStates (T tileEntity);
}
