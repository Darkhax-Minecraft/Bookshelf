package net.darkhax.bookshelftest.recipeowner;

import java.util.UUID;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.darkhax.bookshelf.util.CraftingUtils;
import net.darkhax.bookshelf.util.ItemStackUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "recipeownertest", name = "Recipe Owner Test", version = "1.0.0.0")
public class RecipeOwnerTest {

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        GameRegistry.addShapelessRecipe(new ItemStack(Items.IRON_INGOT, 21, 0), Items.IRON_HORSE_ARMOR);
    }

    @SubscribeEvent
    public void onTooltip (ItemTooltipEvent event) {

        final IRecipe recipe = CraftingUtils.getCurrentCraftingRecipe(event.getEntityPlayer());

        if (recipe != null) {

            final ModContainer container = CraftingUtils.getOwner(recipe);

            if (container != null) {

                final ItemStack output = CraftingUtils.getCurrentCraftingOutput(event.getEntityPlayer());
                ItemStackUtils.prepareDataTag(output).setString("BookshelfTempUUID", UUID.randomUUID().toString());

                if (ItemStackUtils.areStacksEqual(event.getItemStack(), output, true)) {
                    event.getToolTip().add(ChatFormatting.BLUE + container.getName());
                }
            }
        }
    }
}