package net.darkhax.bookshelf.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorfulItem {

    @SideOnly(Side.CLIENT)
    public IItemColor getColorHandler ();
}
