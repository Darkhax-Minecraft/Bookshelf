package net.darkhax.bookshelf.common.mixin.patch.client;

import net.darkhax.bookshelf.common.api.data.ISidedRecipeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1005)
public class MixinClientPacketListener {

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(Minecraft mc, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        if (this.recipeManager instanceof ISidedRecipeManager sided) {
            sided.bookshelf$setLogicalClient();
        }
    }
}