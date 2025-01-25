package net.darkhax.bookshelf.common.mixin.patch.level;

import com.google.gson.JsonElement;
import net.darkhax.bookshelf.common.api.data.ISidedRecipeManager;
import net.darkhax.bookshelf.common.impl.Constants;
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
public class MixinRecipeManager implements ISidedRecipeManager {

    @Unique
    private boolean bookshelf$isClient = false;

    @Unique
    private boolean bookshelf$isServer = false;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void onReload(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        if (this.bookshelf$isServer) {
            Constants.SERVER_REVISION++;
        }
    }

    @Inject(method = "replaceRecipes", at = @At("RETURN"))
    private void onRecipesUpdated(Iterable<RecipeHolder<?>> recipes, CallbackInfo ci) {
        if (this.bookshelf$isClient) {
            Constants.CLIENT_REVISION++;
        }
    }

    @Override
    public void bookshelf$setLogicalClient() {
        this.bookshelf$isClient = true;
        this.bookshelf$isServer = false;
    }

    @Override
    public void bookshelf$setLogicalServer() {
        this.bookshelf$isServer = true;
        this.bookshelf$isClient = false;
    }
}