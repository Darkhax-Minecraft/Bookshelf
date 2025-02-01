package net.darkhax.bookshelf.common.impl.data.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.darkhax.bookshelf.common.api.data.codecs.map.MapCodecs;
import net.darkhax.bookshelf.common.api.data.codecs.stream.StreamCodecs;
import net.darkhax.bookshelf.common.api.data.ingredient.IngredientLogic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ModIdIngredient implements IngredientLogic<ModIdIngredient> {

    public static final MapCodec<ModIdIngredient> CODEC = MapCodecs.flexibleList(Codec.STRING).xmap(ModIdIngredient::new, i -> i.modIds).fieldOf("mod");
    public static final StreamCodec<RegistryFriendlyByteBuf, ModIdIngredient> STREAM = StreamCodecs.list(StreamCodecs.STRING).map(ModIdIngredient::new, i -> i.modIds);

    private final List<String> modIds;

    public ModIdIngredient(List<String> modIds) {
        this.modIds = modIds;
    }

    @Override
    public boolean test(ItemStack stack) {
        final String owner = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
        for (String id : this.modIds) {
            if (owner.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
