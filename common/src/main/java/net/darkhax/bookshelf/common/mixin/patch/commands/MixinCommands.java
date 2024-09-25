package net.darkhax.bookshelf.common.mixin.patch.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.darkhax.bookshelf.common.api.service.Services;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class MixinCommands {

    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>(Lnet/minecraft/commands/Commands$CommandSelection;Lnet/minecraft/commands/CommandBuildContext;)V", at = @At("RETURN"))
    private void registerCommands(Commands.CommandSelection selection, CommandBuildContext context, CallbackInfo cbi) {
        Services.CONTENT_PROVIDERS.get().forEach(provider -> provider.registerCommands(dispatcher, context, selection));
    }
}