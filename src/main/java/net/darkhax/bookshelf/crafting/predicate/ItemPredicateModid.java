package net.darkhax.bookshelf.crafting.predicate;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.GsonHelper;

public class ItemPredicateModid extends ItemPredicate {

    private final String modid;

    public ItemPredicateModid (String modid) {

        this.modid = modid;
    }

    @Override
    public boolean matches (ItemStack stack) {

        return !stack.isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(this.modid);
    }

    public static ItemPredicate fromJson (JsonObject json) {

        final String modid = GsonHelper.getAsString(json, "modid");
        return new ItemPredicateModid(modid);
    }
}