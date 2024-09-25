package net.darkhax.bookshelf.common.mixin.patch.packs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {

    @Shadow private boolean hasErrors;
    @Shadow @Final private HolderLookup.Provider registries;
    @Shadow @Final private static Logger LOGGER;
    @Shadow private Multimap<RecipeType<?>, RecipeHolder<?>> byType;
    @Shadow private Map<ResourceLocation, RecipeHolder<?>> byName;

    @Overwrite
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.hasErrors = false;
        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder = ImmutableMultimap.builder();
        ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder1 = ImmutableMap.builder();
        RegistryOps<JsonElement> registryops = this.registries.createSerializationContext(JsonOps.INSTANCE);
        Iterator var7 = object.entrySet().iterator();

        while (var7.hasNext()) {
            Map.Entry<ResourceLocation, JsonElement> entry = (Map.Entry) var7.next();
            ResourceLocation resourcelocation = (ResourceLocation) entry.getKey();

            try {
                Recipe<?> recipe = (Recipe) Recipe.CODEC.parse(registryops, (JsonElement) entry.getValue()).getOrThrow(JsonParseException::new);
                RecipeHolder<?> recipeholder = new RecipeHolder(resourcelocation, recipe);
                builder.put(recipe.getType(), recipeholder);
                builder1.put(resourcelocation, recipeholder);
            }
            catch (JsonParseException | IllegalArgumentException var12) {
                LOGGER.error("Parsing error loading recipe {}", resourcelocation, var12);
            }
        }

        this.byType = builder.build();
        this.byName = builder1.build();
        LOGGER.info("Loaded {} recipes", this.byType.size());
    }
}
