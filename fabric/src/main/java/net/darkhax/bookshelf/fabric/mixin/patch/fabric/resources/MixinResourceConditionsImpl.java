package net.darkhax.bookshelf.fabric.mixin.patch.fabric.resources;

import com.google.gson.JsonObject;
import net.darkhax.bookshelf.common.api.data.conditions.LoadConditions;
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceConditionsImpl.class)
public class MixinResourceConditionsImpl {

    @Inject(method = "applyResourceConditions", at = @At("HEAD"), cancellable = true)
    private static void testConditions(JsonObject obj, String dataType, Identifier key, RegistryOps.RegistryInfoLookup registryInfo, CallbackInfoReturnable<Boolean> cir) {
        if (!LoadConditions.canLoad(obj)) {
            cir.setReturnValue(false);
        }
    }
}
