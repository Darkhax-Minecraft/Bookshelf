package net.darkhax.bookshelf.common.mixin.patch.packs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SimpleJsonResourceReloadListener.class)
public class MixinSimpleJsonResourceReloadListener {

    /**
     * This patch introduces load conditions for all JSON based resource loaders. These conditions are independent of
     * the loader platform allowing them to be used in loader agnostic sourcesets.
     */
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;", at = @At("RETURN"))
    private void prepare(ResourceManager manager, ProfilerFiller profiler, CallbackInfoReturnable<Map<ResourceLocation, JsonElement>> cbi) {
        cbi.getReturnValue().entrySet().removeIf(entry -> entry.getValue() instanceof JsonObject obj && !LoadConditions.canLoad(obj));
    }
}