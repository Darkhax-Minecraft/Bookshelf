package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class ModifierAddItem extends LootModifier {

    public static final Serializer SERIALIZER = new Serializer();

    private final ItemStack toAdd;

    public ModifierAddItem (LootItemCondition[] conditions, ItemStack toAdd) {

        super(conditions);
        this.toAdd = toAdd;
    }

    @Override
    protected List<ItemStack> doApply (List<ItemStack> loot, LootContext ctx) {

        loot.add(this.toAdd.copy());
        return loot;
    }

    static class Serializer extends GlobalLootModifierSerializer<ModifierAddItem> {

        @Override
        public ModifierAddItem read (ResourceLocation location, JsonObject data, LootItemCondition[] conditions) {

            final ItemStack item = ShapedRecipe.itemStackFromJson(data.getAsJsonObject("item"));
            return new ModifierAddItem(conditions, item);
        }

        @Override
        public JsonObject write (ModifierAddItem instance) {

            final JsonObject json = new JsonObject();
            json.add("item", Serializers.ITEMSTACK.write(instance.toAdd));
            return json;
        }
    }
}