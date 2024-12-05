package net.darkhax.bookshelf.common.mixin.patch.loot;

import net.darkhax.bookshelf.common.api.registry.register.Register;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootPoolEntries.class)
public class MixinLootPoolEntries {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void classInit(CallbackInfo ci) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            final String owner = provider.contentNamespace();
            provider.registerLootEntryType(new Register<>(owner, (id, codec) -> Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, id, new LootPoolEntryType(codec))));
        });
    }
}