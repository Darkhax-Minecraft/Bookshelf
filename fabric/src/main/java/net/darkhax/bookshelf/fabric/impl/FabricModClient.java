package net.darkhax.bookshelf.fabric.impl;

import net.darkhax.bookshelf.common.api.registry.register.RegisterBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;

public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RegisterBlockEntityRenderer.bindBlockEntityRenderers();
    }
}