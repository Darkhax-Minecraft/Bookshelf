package net.darkhax.bookshelf.common.mixin.patch.block;

import net.darkhax.bookshelf.common.api.registry.register.RegisterPotPatterns;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(DecoratedPotPatterns.class)
public class MixinDecoratedPotPatterns {

    @Unique
    private static final Map<Item, ResourceKey<DecoratedPotPattern>> NEW_PATTERNS = new HashMap<>();

    @Inject(method = "getPatternFromItem", at = @At("TAIL"), cancellable = true)
    private static void getResourceKey(Item item, CallbackInfoReturnable<ResourceKey<DecoratedPotPattern>> cbi) {
        if (NEW_PATTERNS.containsKey(item)) {
            cbi.setReturnValue(NEW_PATTERNS.get(item));
        }
    }

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(Registry<DecoratedPotPattern> registry, CallbackInfoReturnable<DecoratedPotPattern> cbi) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerPotPatterns(new RegisterPotPatterns(provider.contentNamespace(), NEW_PATTERNS, (id, pattern) -> {
            Registry.register(registry, pattern, new DecoratedPotPattern(id));
        })));
    }
}