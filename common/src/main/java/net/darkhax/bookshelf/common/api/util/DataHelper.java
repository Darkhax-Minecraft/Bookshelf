package net.darkhax.bookshelf.common.api.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DataHelper {

    public static <T> HolderSet<T> getTagOrEmpty(@Nullable HolderLookup.Provider provider, ResourceKey<Registry<T>> registryKey, TagKey<T> tag) {
        if (provider != null) {
            final Optional<HolderSet.Named<T>> optional = provider.lookupOrThrow(registryKey).get(tag);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return HolderSet.direct();
    }
}
