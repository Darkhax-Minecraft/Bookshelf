/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.world.loot.functions;

import java.util.Random;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.lib.MCColor;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class SetColor extends LootFunction {

    /**
     * The color to apply. If null, the color will be random.
     */
    private final MCColor color;

    /**
     * Constructs a new SetColor function, with no conditions.
     */
    public SetColor () {

        this(null, new LootCondition[0]);
    }

    /**
     * Constructs a new SetColor function which uses a random color when
     * applied.
     *
     * @param conditions The traditional loot conditions.
     */
    public SetColor (LootCondition[] conditions) {

        this(null, conditions);
    }

    /**
     * Constructs a new SetColor function which uses the specified color when
     * applied.
     *
     * @param color The color to use.
     * @param conditions The traditional loot conditions.
     */
    protected SetColor (MCColor color, LootCondition[] conditions) {

        super(conditions);
        this.color = color;
    }

    @Override
    public ItemStack apply (ItemStack stack, Random rand, LootContext context) {

        final MCColor colorToApply = this.color == null ? MCColor.getRandomColor(rand) : this.color;
        colorToApply.writeToStack(StackUtils.prepareStack(stack));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetColor> {

        public Serializer () {

            super(new ResourceLocation(Constants.MOD_ID, "set_color"), SetColor.class);
        }

        @Override
        public void serialize (JsonObject object, SetColor function, JsonSerializationContext serializationContext) {

            object.add("color", serializationContext.serialize(function.color.getComponents()));
        }

        @Override
        public SetColor deserialize (JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {

            return new SetColor(new MCColor((int[]) deserializationContext.deserialize(object.get("color"), int[].class)), conditionsIn);
        }
    }
}