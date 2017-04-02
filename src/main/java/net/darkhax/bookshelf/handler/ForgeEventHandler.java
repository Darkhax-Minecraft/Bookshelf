package net.darkhax.bookshelf.handler;

import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {

    public void onPlayerJoinWorld (EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof AbstractClientPlayer) {

            final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();

            if (!PlayerUtils.PROFILE_CACHE.containsValue(player.getUniqueID())) {
                PlayerUtils.PROFILE_CACHE.put(player.getGameProfile().getName(), player.getUniqueID());
            }
        }
    }

    @SubscribeEvent
    public void onAnvilUsed (AnvilUpdateEvent event) {

        for (final BookshelfRegistry.AnvilRecipe recipe : BookshelfRegistry.getAnvilRecipes())
            if (recipe != null && !recipe.output.isEmpty() && ItemStackUtils.areStacksSimilarWithSize(event.getLeft(), recipe.inputLeft) && ItemStackUtils.areStacksSimilarWithSize(event.getRight(), recipe.inputRight)) {

                event.setCost(recipe.getExperienceCost(event.getLeft(), event.getRight(), event.getName()));
                event.setMaterialCost(recipe.getMaterialCost(event.getLeft(), event.getRight(), event.getName()));

                if (recipe.nameTaxt != null && !recipe.nameTaxt.isEmpty()) {

                    if (recipe.nameTaxt.equalsIgnoreCase(event.getName())) {
                        event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                    }

                    return;
                }

                event.setOutput(recipe.getOutput(event.getLeft(), event.getRight(), event.getName()));
                return;
            }
    }
}
