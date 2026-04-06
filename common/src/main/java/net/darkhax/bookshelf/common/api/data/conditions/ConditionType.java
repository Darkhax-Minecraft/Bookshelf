package net.darkhax.bookshelf.common.api.data.conditions;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * Represents a type of load condition that Bookshelf can process and test.
 *
 * @param id    The ID of the condition type.
 * @param codec The codec used to serialize the condition from data.
 */
public record ConditionType(Identifier id, MapCodec<? extends ILoadCondition> codec) {
}
