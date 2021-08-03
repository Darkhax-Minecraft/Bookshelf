package net.darkhax.bookshelf.loot.modifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class ModifierConvert extends LootModifier {

    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation tableName;

    private final float chance;

    public ModifierConvert (LootItemCondition[] conditions, ResourceLocation table, float chance) {

        super(conditions);

        this.tableName = table;
        this.chance = chance;
    }

    @Override
    protected List<ItemStack> doApply (List<ItemStack> loot, LootContext ctx) {

        final List<ItemStack> outputs = NonNullList.create();

        final LootTable table = ctx.getLevel().getServer().getLootTables().get(this.tableName);

        for (final ItemStack item : loot) {

            if (MathsUtils.tryPercentage(this.chance)) {

                outputs.addAll(table.getRandomItems(ctx));
            }

            else {

                outputs.add(item);
            }
        }

        return outputs;
    }

    static class Serializer extends GlobalLootModifierSerializer<ModifierConvert> {

        @Override
        public ModifierConvert read (ResourceLocation location, JsonObject data, LootItemCondition[] conditions) {

            final ResourceLocation tableName = ResourceLocation.tryParse(GsonHelper.getAsString(data, "table"));
            final float chance = GsonHelper.getAsFloat(data, "chance", 1f);
            return new ModifierConvert(conditions, tableName, chance);
        }

        @Override
        public JsonObject write (ModifierConvert instance) {

            final JsonObject json = new JsonObject();
            json.addProperty("table", instance.tableName.toString());

            if (instance.chance > 0f) {

                json.addProperty("chance", instance.chance);
            }

            return json;
        }
    }
}