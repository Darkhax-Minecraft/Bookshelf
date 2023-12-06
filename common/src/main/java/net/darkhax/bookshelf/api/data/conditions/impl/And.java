package net.darkhax.bookshelf.api.data.conditions.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.data.conditions.ILoadCondition;
import net.darkhax.bookshelf.api.data.conditions.LoadConditions;

import java.util.List;

public class And implements ILoadCondition {

    public static final Codec<And> CODEC = RecordCodecBuilder.create(instance -> instance.group(LoadConditions.CODEC_HELPER.getList("conditions", And::getConditions)).apply(instance, And::new));
    private final List<ILoadCondition> conditions;

    private And(List<ILoadCondition> conditions) {

        this.conditions = conditions;
    }

    public List<ILoadCondition> getConditions() {

        return this.conditions;
    }

    @Override
    public boolean allowLoading() {

        for (ILoadCondition condition : this.conditions) {

            if (!condition.allowLoading()) {

                return false;
            }
        }

        return true;
    }

    @Override
    public LoadConditions.ConditionType getType() {

        return LoadConditions.NOT;
    }
}