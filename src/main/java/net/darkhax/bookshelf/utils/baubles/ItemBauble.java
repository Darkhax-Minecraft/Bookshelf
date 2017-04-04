package net.darkhax.bookshelf.utils.baubles;

import baubles.api.IBauble;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "Baubles", iface = "baubles.api.IBauble")
public abstract class ItemBauble extends Item implements IBauble {

}
