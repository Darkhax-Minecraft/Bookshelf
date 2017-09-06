package net.darkhax.bookshelf.block;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorfulBlock {

    @SideOnly(Side.CLIENT)
    public IBlockColor getColorHandler ();
}
