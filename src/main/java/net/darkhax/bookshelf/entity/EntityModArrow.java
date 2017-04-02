/*******************************************************************************************************************
 * Copyright: Jasmine Iwanek
 *
 * License: Lesser General Public License 2.1
 *          https://github.com/MinecraftModDevelopment/BaseMetals/blob/1041ccba63dcebe009676c9b18e25143f5afda3e/LICENSE
 *
 * Original: https://github.com/MinecraftModDevelopment/BaseMetals/blob/62ee3b6cb4d2b1591e614c7266b245546b3f80dd/src/main/java/cyano/basemetals/entity/EntityCustomArrow.java
 *
 * Changes: - Reformatted to Bookshelf's code style and formatting.
 *          - Wrote complete Javadocs.
 *          - Renamed class to EntityModArrow.
 *          - Several parameters and fields have been renamed.
 *          - Modified getArrowStack to be cleaner, and generic.
 *          - NBT now syncs under a new key which is more fitting.
 *          - Compacted bulk of writeToNBT into a single line.
 *          - Opened up secondary constructor to accept an EntityLivingBase as the shoother.
 *******************************************************************************************************************/
package net.darkhax.bookshelf.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityModArrow extends EntityTippedArrow {

    /**
     * The ItemStack held by the arrow entity.
     */
    private ItemStack heldStack;

    /**
     * Constructor for a variant on EntityArrow which holds a specific ItemStack rather than a
     * vanilla one.
     *
     * @param world The world to place the new entity in.
     */
    public EntityModArrow (World world) {

        super(world);
    }

    /**
     * Constructor for a variant on EntityArrow which holds a specific ItemStack rather than a
     * vanilla one.
     *
     * @param world The world to place the new entity in.
     * @param stack The ItemStack that is held by the arrow entity.
     * @param shooter The entity which fired the arrow.
     */
    public EntityModArrow (World world, ItemStack stack, EntityLivingBase shooter) {

        super(world, shooter);
        this.heldStack = stack;
    }

    @Override
    protected ItemStack getArrowStack () {

        return this.heldStack != ItemStack.EMPTY ? this.heldStack : super.getArrowStack();
    }

    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound compound) {

        super.writeToNBT(compound);
        compound.setTag("HeldStack", this.heldStack.writeToNBT(new NBTTagCompound()));
        return compound;
    }

    @Override
    public void readFromNBT (NBTTagCompound compound) {

        super.readFromNBT(compound);
        this.heldStack = new ItemStack(compound.getCompoundTag("HeldStack"));
    }

    @Override
    public boolean equals (Object object) {

        return object instanceof EntityModArrow && super.equals(object);
    }

    @Override
    public int hashCode () {

        return this.heldStack.hashCode() + super.hashCode();
    }
}