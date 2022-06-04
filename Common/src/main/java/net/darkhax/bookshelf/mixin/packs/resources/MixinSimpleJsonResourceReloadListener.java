package net.darkhax.bookshelf.mixin.packs.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.ILoadConditionSerializer;
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

    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;", at = @At("RETURN"))
    private void prepare(ResourceManager manager, ProfilerFiller profiler, CallbackInfoReturnable<Map<ResourceLocation, JsonElement>> cbi) {

        cbi.getReturnValue().entrySet().removeIf(entry -> entry.getValue() instanceof JsonObject obj && !ILoadConditionSerializer.canLoad(obj));
    }
}