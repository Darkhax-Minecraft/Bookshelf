package net.darkhax.bookshelf.loot.modifier;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * This loot modifier will clear all the generated loot.
 */
public class ModifierClear extends LootModifier {

    public static final GlobalLootModifierSerializer<ModifierClear> SERIALIZER = new GlobalLootModifierSerializer<ModifierClear>() {

        @Override
        public ModifierClear read (ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {

            return new ModifierClear(ailootcondition);
        }

        @Override
        public JsonObject write (ModifierClear instance) {

            return new JsonObject();
        }
    };

    private ModifierClear (LootItemCondition[] conditionsIn) {

        super(conditionsIn);
    }

    @Override
    protected List<ItemStack> doApply (List<ItemStack> generatedLoot, LootContext context) {

        return new ArrayList<>();
    }
}