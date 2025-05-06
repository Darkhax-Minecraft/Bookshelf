package net.darkhax.bookshelf.common.mixin.patch.loot;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootItemFunctions.class)
public class MixinLootItemFunctions {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerLootFunctions(new Register<>(provider.contentNamespace(), (id, codec) -> Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, id, new LootItemFunctionType<>(codec)))));
    }
}