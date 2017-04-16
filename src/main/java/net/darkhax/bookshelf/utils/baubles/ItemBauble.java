/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.utils.baubles;

import baubles.api.IBauble;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public abstract class ItemBauble extends Item implements IBauble {
    
}
