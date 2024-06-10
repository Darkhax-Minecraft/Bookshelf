package net.darkhax.bookshelf.api.data;

import net.darkhax.bookshelf.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BookshelfTags {

    public static class DamageTypes {

        public static TagKey<DamageType> FAKE_PLAYER = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, "fake_player"));
    }
}