/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * An implementation of Entity which doesn't actually do anything. Useful when
 * you need to make references to an entity class for tricking registers.
 */
public class EntityFake extends Entity {

    public EntityFake (World worldIn) {

        super(worldIn);
    }

    @Override
    protected void entityInit () {

        // The entity is fake, it should not initialize.
    }

    @Override
    protected void readEntityFromNBT (NBTTagCompound compound) {

        // The entity is fake, it should not have any nbt.
    }

    @Override
    protected void writeEntityToNBT (NBTTagCompound compound) {

        // The entity is fake, it should not have any nbt.
    }
}