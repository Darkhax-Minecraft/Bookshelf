package net.darkhax.bookshelf.api.data;

import net.darkhax.bookshelf.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BookshelfTags {

    public static class DamageTypes {

        public static TagKey<DamageType> FAKE_PLAYER = TagKey.create(Registries.DAMAGE_TYPE, Constants.id("fake_player"));
    }
}