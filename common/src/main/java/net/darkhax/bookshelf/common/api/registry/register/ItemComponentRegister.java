package net.darkhax.bookshelf.common.api.registry.register;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public interface ItemComponentRegister {
    <T> void accept(String name, UnaryOperator<DataComponentType.Builder<T>> builder);
    <T> void accept(ResourceLocation id, UnaryOperator<DataComponentType.Builder<T>> builder);
}
