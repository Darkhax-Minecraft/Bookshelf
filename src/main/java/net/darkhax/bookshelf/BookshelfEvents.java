/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf;

import net.darkhax.bookshelf.crafting.IAnvilRecipe;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BookshelfEvents {

    @SubscribeEvent
    public void onAnvilUpdate (AnvilUpdateEvent event) {

        for (final IAnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes()) {
            if (recipe.isValidRecipe(event.getLeft(), event.getRight(), event.getName())) {
                event.setCost(recipe.getExperienceCost(event.getLeft(), event.getRight(), event.getName()));
                event.setMaterialCost(recipe.getMaterialCost(event.getLeft(), event.getRight(), event.getName()));
                event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                return;
            }
        }
    }
}
