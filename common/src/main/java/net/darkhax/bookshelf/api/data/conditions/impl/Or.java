package net.darkhax.bookshelf.api.data.conditions.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;

import java.util.List;

public class Or implements ILoadCondition {

    public static final Codec<Or> CODEC = RecordCodecBuilder.create(instance -> instance.group(LoadConditions.CODEC_HELPER.getList("conditions", Or::getConditions)).apply(instance, Or::new));
    private final List<ILoadCondition> conditions;

    private Or(List<ILoadCondition> conditions) {

        this.conditions = conditions;
    }

    public List<ILoadCondition> getConditions() {

        return this.conditions;
    }

    @Override
    public boolean allowLoading() {

        for (ILoadCondition condition : this.conditions) {

            if (condition.allowLoading()) {

                return true;
            }
        }

        return false;
    }

    @Override
    public LoadConditions.ConditionType getType() {

        return LoadConditions.OR;
    }
}