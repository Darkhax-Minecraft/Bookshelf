package net.darkhax.bookshelf.common.mixin.patch.server;

import net.darkhax.bookshelf.common.api.data.ISidedRecipeManager;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadableServerResources.class)
public class MixinReloadableServerResources {

    @Shadow @Final private RecipeManager recipes;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(RegistryAccess.Frozen registry, FeatureFlagSet features, Commands.CommandSelection commands, int functionLevel, CallbackInfo ci) {
        if (this.recipes instanceof ISidedRecipeManager sided) {
            sided.bookshelf$setLogicalServer();
        }
    }
}