package net.darkhax.bookshelf.api.data;

import net.darkhax.bookshelf.Constants;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BookshelfTags {

    public static class DamageTypes {

        public static TagKey<DamageType> CAUSE_PLAYER_ONLY_DROPS = Services.TAGS.damageTag(new ResourceLocation(Constants.MOD_ID, "cause_player_only_drops"));
        public static TagKey<DamageType> CAUSE_EXP_DROPS = Services.TAGS.damageTag(new ResourceLocation(Constants.MOD_ID, "cause_exp_drops"));
    }
}
