package net.darkhax.bookshelf.api.item.tab;

import net.minecraft.world.flag.FeatureFlagSet;

@FunctionalInterface
public interface IDisplayGenerator {

    void display(FeatureFlagSet flags, IOutputWrapper output, boolean isViewerOp);
}