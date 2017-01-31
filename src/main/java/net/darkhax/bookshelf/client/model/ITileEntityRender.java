package net.darkhax.bookshelf.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileEntityRender {

    @SideOnly(Side.CLIENT)
    ImmutableMap<String, String> getRenderStates ();
}
