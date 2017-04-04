package net.darkhax.bookshelf.client.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DefaultItemOverrideList extends ItemOverrideList {

    public static final ItemOverrideList DEFAULT = new DefaultItemOverrideList();

    public DefaultItemOverrideList () {

        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState (IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

        return ((ModelRetexturable) originalModel).getDefaultModel();
    }
}
