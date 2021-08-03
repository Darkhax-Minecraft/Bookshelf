/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolType;

public final class StackUtils {

    public static boolean hasToolType (ToolType type, ItemStack stack) {

        return stack.getItem().getToolTypes(stack).contains(type);
    }
}