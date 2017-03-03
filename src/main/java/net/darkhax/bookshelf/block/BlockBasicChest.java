package net.darkhax.bookshelf.block;

import net.darkhax.bookshelf.tileentity.TileEntityBasicChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockBasicChest extends BlockChest {

    /**
     * Whether or not this chest has redstone power.
     */
    private final boolean hasPower;

    /**
     * The location of the single chest texture.
     */
    private final ResourceLocation TEXTURE_SINGLE;

    /**
     * The location of the double chest texture.
     */
    private final ResourceLocation TEXTURE_DOUBLE;

    /**
     * Creates a new chest that will behave like the standard vanilla one. The texture for the
     * single and double variations are created using the domain and name parameters.
     *
     * @param domain The domain to use for assets.
     * @param name The base name for the texture to use.
     * @param type The basic chest type. Use EnumHelper to make a new one.
     *        EnumHelper.addEnum(Type.class, "MODNAME_TYPENAME", new Class[0]);
     */
    public BlockBasicChest (String domain, String name, Type type) {

        this(domain, name, false, type);
    }

    /**
     * Creates a new chest that will behave like the standard vanilla one, with the option of
     * redstone support. The texture for the single and double variations are created using the
     * domain and name parameters.
     *
     * @param domain The domain to use for assets.
     * @param name The base name for the texture to use.
     * @param hasPower Whether or not this chest has redstone power.
     * @param type type The basic chest type. Use EnumHelper to make a new one.
     *        EnumHelper.addEnum(Type.class, "MODNAME_TYPENAME", new Class[0]);
     */
    public BlockBasicChest (String domain, String name, boolean hasPower, Type type) {

        this(new ResourceLocation(domain, "textures/entity/chest/" + name + "_single.png"), new ResourceLocation(domain, "textures/entity/chest/" + name + "_double.png"), hasPower, type);
    }

    /**
     * Creates a new chest that will behave list a standard vanilla chest, with optional
     * support for redstone.
     *
     * @param singleTexture The texture for the single chest.
     * @param doubleTexture The texture for the double chest.
     * @param hasPower Whether or not this chest has redstone power.
     * @param type The basic chest type. Use EnumHelper to make a new one.
     *        EnumHelper.addEnum(Type.class, "MODNAME_TYPENAME", new Class[0]);
     */
    public BlockBasicChest (ResourceLocation singleTexture, ResourceLocation doubleTexture, boolean hasPower, Type type) {

        super(type);
        this.hasPower = hasPower;
        this.TEXTURE_SINGLE = singleTexture;
        this.TEXTURE_DOUBLE = doubleTexture;
    }

    @Override
    public boolean canProvidePower (IBlockState state) {

        return this.hasPower;
    }

    @Override
    public TileEntity createNewTileEntity (World worldIn, int meta) {

        return new TileEntityBasicChest(this.chestType);
    }

    /**
     * Gets the texture for a single chest.
     *
     * @return The texture for a single chest.
     */
    public ResourceLocation getSingleTexture () {

        return this.TEXTURE_SINGLE;
    }

    /**
     * Gets the texture for a double chest.
     *
     * @return The texture for a double chest.
     */
    public ResourceLocation getDoubleTexture () {

        return this.TEXTURE_DOUBLE;
    }

    /**
     * Whether or not this chest has redstone power.
     *
     * @return Does the chest have redstone power.
     */
    public boolean isHasPower () {

        return this.hasPower;
    }
}