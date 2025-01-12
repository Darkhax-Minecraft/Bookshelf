package net.darkhax.bookshelf.common.mixin.patch.level;

import com.google.gson.JsonElement;
import net.darkhax.bookshelf.common.api.data.IReloadTracking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class MixinRecipeManager implements IReloadTracking {

    @Unique
    private int bookshelf$currentRevision = 0;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void onReload(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        this.bookshelf$bump();
    }

    @Inject(method = "replaceRecipes", at = @At("RETURN"))
    private void onRecipesUpdated(Iterable<RecipeHolder<?>> recipes, CallbackInfo ci) {
        this.bookshelf$bump();
    }

    @Override
    public int bookshelf$getRevision() {
        return this.bookshelf$currentRevision;
    }

    @Override
    public void bookshelf$setRevision(int revision) {
        this.bookshelf$currentRevision = revision;
    }
}