package net.darkhax.bookshelf.mixin.accessors.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public interface AccessorStructureTemplatePool {

    @Mutable
    @Accessor("rawTemplates")
    void bookshelf$setRawTemplates(List<Pair<StructurePoolElement, Integer>> rawTemplates);

    @Accessor("rawTemplates")
    List<Pair<StructurePoolElement, Integer>> bookshelf$getRawTemplates();

    @Mutable
    @Accessor("templates")
    void bookshelf$setTemplates(ObjectArrayList<StructurePoolElement> templates);

    @Accessor("templates")
    ObjectArrayList<StructurePoolElement> bookshelf$getTemplates();
}
