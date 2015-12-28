package net.darkhax.bookshelf.common;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.buff.Buff;
import net.darkhax.bookshelf.common.network.packet.PacketAddPlayerProperties;
import net.darkhax.bookshelf.common.network.packet.PacketRemovePlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.IExtendedEntityProperties;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.buff.BuffEffect;
import net.darkhax.bookshelf.buff.BuffHelper;
import net.darkhax.bookshelf.common.network.packet.PacketSyncPlayerProperties;

public class EntityProperties implements IExtendedEntityProperties {

    public static final String PROP_NAME = "BookshelfData";
    public final EntityLivingBase entity;
    private List<BuffEffect> buffs = new ArrayList<BuffEffect>();

    private EntityProperties(EntityLivingBase entity) {

        this.entity = entity;
    }

    @Override
    public void saveNBTData (NBTTagCompound compound) {

        NBTTagCompound entityData = new NBTTagCompound();
        entityData.setTag("BookshelfBuff", BuffHelper.writeNBT(buffs));
        compound.setTag(PROP_NAME, entityData);
    }

    @Override
    public void loadNBTData (NBTTagCompound compound) {

        NBTTagCompound playerData = compound.getCompoundTag(PROP_NAME);
        this.buffs = BuffHelper.readNBT(playerData.getTagList("BookshelfBuff", 10));
    }

    @Override
    public void init (Entity entity, World world) {

    }

    /**
     * Synchronizes the EntityProperties data.
     */
    public void sync () {

        Bookshelf.network.sendToAll(new PacketSyncPlayerProperties(this));
    }

    /**
     * Retrieves the EntityProperties instance from an Entity.
     *
     * @param entity: The Entity to retrieve from.
     * @return EntityProperties: The EntityProperties instance for that entity.
     */
    public static EntityProperties getProperties (EntityLivingBase entity) {

        return (EntityProperties) entity.getExtendedProperties(PROP_NAME);
    }

    /**
     * Sets the EntityProperties instance for an entity.
     *
     * @param entity: The entity to set to.
     * @return EntityProperties: The new EntityProperties instance.
     */
    public static EntityProperties setProperties (EntityLivingBase entity) {

        entity.registerExtendedProperties(PROP_NAME, new EntityProperties(entity));
        return getProperties(entity);
    }

    /**
     * A check to determine whether or not an Entity has an instance of EntityProperties.
     *
     * @param entity: The Entity to check.
     * @return boolean: Whether or not the entity has an instance of EntityProperties.
     */
    public static boolean hasProperties (EntityLivingBase entity) {

        return getProperties(entity) != null;
    }

    /**
     * Retrieves a list of buffs from the EntityProperties instance.
     *
     * @return NBTTagList: A list of buffs in NBT.
     */
    public List<BuffEffect> getBuffs () {

        return buffs;
    }

    /**
     * Sets a list of Buffs to this EntityProperties instance.
     *
     * @param buffs: The list of buffs to set.
     */
    public EntityProperties setBuffs (List<BuffEffect> buffs) {

        this.buffs = buffs;
        return this;
    }

    public void remove(BuffEffect buff, boolean isRemote) {

        if(this.buffs != null){
            BuffEffect ef = null;
            for (BuffEffect buffE : buffs) {

                if (buffE.getBuff().equals(buff.getBuff())) {
                    ef = buffE;
                    break;
                }
            }

            if(ef != null) {

                buffs.remove(ef);
                if(!isRemote)
                    Bookshelf.network.sendToAll(new PacketRemovePlayerProperties(entity, buff));
            }
        }

    }

    public void add(BuffEffect buff, boolean isRemote)
    {

        if(buff != null){

            buffs.add(buff);

            if(!isRemote)
                Bookshelf.network.sendToAll(new PacketAddPlayerProperties(entity, buff));
        }
    }
}