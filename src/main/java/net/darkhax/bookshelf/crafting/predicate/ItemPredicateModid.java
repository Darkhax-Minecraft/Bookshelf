package net.darkhax.bookshelf.crafting.predicate;

import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

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

        final String modid = JSONUtils.getAsString(json, "modid");
        return new ItemPredicateModid(modid);
    }
}