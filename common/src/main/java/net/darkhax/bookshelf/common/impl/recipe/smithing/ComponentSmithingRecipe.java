package net.darkhax.bookshelf.common.impl.recipe.smithing;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Optional;

public class ComponentSmithingRecipe extends SimpleSmithingRecipe {

    public static final MapCodec<ComponentSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CommonInfo.MAP_CODEC.forGetter(ComponentSmithingRecipe::commonInfo),
            Ingredient.CODEC.optionalFieldOf("template").forGetter(ComponentSmithingRecipe::templateIngredient),
            Ingredient.CODEC.fieldOf("base").forGetter(ComponentSmithingRecipe::baseIngredient),
            Ingredient.CODEC.optionalFieldOf("addition").forGetter(ComponentSmithingRecipe::additionIngredient),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(ComponentSmithingRecipe::components)
    ).apply(instance, ComponentSmithingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, ComponentSmithingRecipe::commonInfo,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, ComponentSmithingRecipe::templateIngredient,
            Ingredient.CONTENTS_STREAM_CODEC, ComponentSmithingRecipe::baseIngredient,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, ComponentSmithingRecipe::additionIngredient,
            DataComponentPatch.STREAM_CODEC, ComponentSmithingRecipe::components,
            ComponentSmithingRecipe::new);

    public static final RecipeSerializer<ComponentSmithingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Optional<Ingredient> addition;
    private final DataComponentPatch components;

    protected ComponentSmithingRecipe(CommonInfo commonInfo, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, DataComponentPatch components) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.components = components;
    }

    public CommonInfo commonInfo() {
        return this.commonInfo;
    }

    public DataComponentPatch components() {
        return this.components;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        final ItemStack output = input.base().copy();
        output.applyComponents(this.components);
        return output;
    }

    @Override
    public RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return this.template;
    }

    @Override
    public Ingredient baseIngredient() {
        return this.base;
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return this.addition;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(List.of(this.templateIngredient(), Optional.of(this.base), this.additionIngredient()));
    }
}
