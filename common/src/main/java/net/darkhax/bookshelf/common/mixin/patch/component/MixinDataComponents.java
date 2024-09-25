package net.darkhax.bookshelf.common.mixin.patch.component;

import net.darkhax.bookshelf.common.api.registry.register.ItemComponentRegister;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.UnaryOperator;

@Mixin(DataComponents.class)
public class MixinDataComponents {

    @Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/core/component/DataComponentType;", at = @At("RETURN"))
    private static void bootstrap(Registry<DataComponentType<?>> registry, CallbackInfoReturnable<DataComponentType<?>> cir) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> {
            // This code is convoluted, but it allows the type parameters
            // of the private method to be preserved. Note that lambda
            // expressions can not implement functional interfaces with
            // type parameters.
            final ItemComponentRegister register = new ItemComponentRegister() {
                @Override
                public <T> void accept(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
                    register(provider.contentNamespace() + ":" + name, builder);
                }

                @Override
                public <T> void accept(ResourceLocation id, UnaryOperator<DataComponentType.Builder<T>> builder) {
                    register(id.toString(), builder);
                }
            };
            provider.registerItemComponents(register);
        });
    }

    @Shadow
    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        throw new IllegalStateException("Mixin failed to apply.");
    }
}