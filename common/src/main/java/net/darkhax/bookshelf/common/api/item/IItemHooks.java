package net.darkhax.bookshelf.common.api.item;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public interface IItemHooks {

    @Nullable
    default ResourceKey<String> getSherdPattern() {
        return null;
    }
}
