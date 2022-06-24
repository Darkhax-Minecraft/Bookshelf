package net.darkhax.bookshelf.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class RecipeTypeEntries extends RegistryEntries<RecipeType<?>> {

    public RecipeTypeEntries(Supplier<String> idProvider) {

        super(idProvider, Registry.RECIPE_TYPE_REGISTRY);
    }

    public <T extends Recipe<?>> IRegistryObject<RecipeType<T>> add(String typeId) {

        final ResourceLocation id = new ResourceLocation(this.getOwner(), typeId);

        return this.add(() -> new RecipeType<>() {

            @Override
            public String toString() {

                return id.toString();
            }

        }, id);
    }
}
