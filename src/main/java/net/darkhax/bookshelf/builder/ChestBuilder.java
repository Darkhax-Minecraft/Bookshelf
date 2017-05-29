package net.darkhax.bookshelf.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.darkhax.bookshelf.block.BlockBasicChest;
import net.darkhax.bookshelf.block.tileentity.TileEntityBasicChest;
import net.darkhax.bookshelf.client.render.tileentity.TileEntityBasicChestRenderer;
import net.darkhax.bookshelf.item.ItemBlockChest;
import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.GameUtils;
import net.darkhax.bookshelf.util.NumericUtils;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest.Type;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ChestBuilder {

    /**
     * The overlay for a single trap chest.
     */
    public static final ResourceLocation TRAP_OVERLAY_SINGLE = new ResourceLocation(Constants.MOD_ID, "textures/entity/chest/trap.png");

    /**
     * The overlay for a double trap chest.
     */
    public static final ResourceLocation TRAP_OVERLAY_DOUBLE = new ResourceLocation(Constants.MOD_ID, "textures/entity/chest/trap_double.png");

    /**
     * A map of builders, used to get a builder by the mod id.
     */
    public static final Map<String, ChestBuilder> BUILDERS = new HashMap<>();

    /**
     * A default chest type, which
     */
    public static final ChestType NONE = new ChestType("none", "none");

    /**
     * Checks if the tile entity has been registered. All mods use the same tile entity for
     * their chests.
     */
    private static boolean tileRegistered = false;

    /**
     * The normal chest type for the builder.
     */
    private final Type typeNormal;

    /**
     * The trap chest type for the builder.
     */
    private Type typeTrap;

    /**
     * The normal chest block.
     */
    private final Block chestNormal;

    /**
     * The trap chest block.
     */
    private Block chestTrap;

    /**
     * The mod id.
     */
    private final String modId;

    /**
     * Does the builder want to use traps?
     */
    private final boolean useTraps;

    /**
     * Not yet implemented
     */
    private final boolean isXmas;

    /**
     * A map of types keyed to their names.
     */
    private final Map<String, IChestType> types;

    /**
     * Creates a chest builder for you to use.
     *
     * @param modid The modid of the builder.
     * @param useTraps Whether or not you want trap blocks.
     */
    public ChestBuilder (String modid, boolean useTraps) {

        // Moved from Param
        final boolean useXmas = false;

        this.modId = modid;
        this.useTraps = useTraps;
        this.isXmas = useXmas && NumericUtils.isChristmasTime();
        this.types = new HashMap<>();

        this.typeNormal = EnumHelper.addEnum(Type.class, modid.toUpperCase(), new Class[0]);
        this.chestNormal = new BlockBasicChest(this, this.typeNormal);

        if (this.useTraps) {

            this.typeTrap = EnumHelper.addEnum(Type.class, modid.toUpperCase() + "_TRAP", new Class[0]);
            this.chestTrap = new BlockBasicChest(this, this.typeTrap);
        }

        BUILDERS.put(modid, this);
    }

    /**
     * Registers the contents with the game.
     *
     * @param registry The RegistryHelper to register stuff to. If you don't want to use the
     *        Bookshelf RegistryHelper you can do this yourself.
     */
    public void register (RegistryHelper registry) {

        registry.registerBlock(this.chestNormal, new ItemBlockChest(this.chestNormal, this), "chest");
        OreDictionary.registerOre(OreDictUtils.CHEST, new ItemStack(this.chestNormal, 1, OreDictionary.WILDCARD_VALUE));

        if (this.useTraps) {

            this.chestTrap = registry.registerBlock(this.chestTrap, new ItemBlockChest(this.chestTrap, this), "chest_trap");
            OreDictionary.registerOre(OreDictUtils.CHEST, new ItemStack(this.chestTrap, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre(OreDictUtils.CHEST_TRAPPED, new ItemStack(this.chestTrap, 1, OreDictionary.WILDCARD_VALUE));
        }

        this.registerTile();
    }

    /**
     * Registers the tile entity.
     */
    private void registerTile () {

        if (tileRegistered) {
            return;
        }

        GameRegistry.registerTileEntity(TileEntityBasicChest.class, "bookshelf_chest");

        if (GameUtils.isClient) {

            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicChest.class, new TileEntityBasicChestRenderer());
        }

        tileRegistered = true;
    }

    /**
     * Gets the normal chest block.
     *
     * @return The normal chest block.
     */
    public Block getNormalChest () {

        return this.chestNormal;
    }

    /**
     * Gets the trap chest block.
     *
     * @return The trap chest block.
     */
    public Block getTrapChest () {

        return this.chestTrap;
    }

    /**
     * Adds a bunch of chest types, using strings as names.
     *
     * @param names The names of the types to add.
     */
    public void addChestType (String... names) {

        for (final String name : names) {

            this.addChestType(name);
        }
    }

    /**
     * Adds a chest type, using a string as a name.
     *
     * @param name The name to use.
     * @return The chest type registered.
     */
    public IChestType addChestType (String name) {

        final IChestType type = this.addChestType(new ChestType(this.modId, name));

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {

            if (this.useTraps) {

                type.addRenderHook(RenderTrapChestOverlay.INSTANCE);
            }
        }

        return type;
    }

    /**
     * Adds a chest type.
     *
     * @param type The chest type to add.
     * @return The chest type that was added.
     */
    public IChestType addChestType (IChestType type) {

        this.types.put(type.getName(), type);
        return type;
    }

    /**
     * Gets a chest type based on name.
     *
     * @param name The name of the type to get.
     * @return The chest type that was found.
     */
    public IChestType getChestType (String name) {

        return this.types.get(name);
    }

    /**
     * Gets the normal chest type.
     *
     * @return The normal chest type.
     */
    public Type getNormalType () {

        return this.typeNormal;
    }

    /**
     * Gets the trap chest type.
     *
     * @return The trap chest type.
     */
    public Type getTrapType () {

        return this.typeTrap;
    }

    /**
     * Gets all registered types.
     *
     * @return A collection of all the registered types.
     */
    public Collection<IChestType> getTypes () {

        return this.types.values();
    }

    /**
     * Gets the modid.
     *
     * @return The modid.
     */
    public String getModid () {

        return this.modId;
    }

    /**
     * Checks if the builder uses traps.
     *
     * @return Whether or not the builder uses trap blocks.
     */
    public boolean useTraps () {

        return this.useTraps;
    }

    /**
     * A default implementation of IChestType.
     */
    public static class ChestType implements IChestType {

        /**
         * The modid.
         */
        private final String modid;

        /**
         * The type name.
         */
        private final String name;

        /**
         * The normal chest texture.
         */
        private final ResourceLocation textureNormal;

        /**
         * The double chest texture.
         */
        private final ResourceLocation textureDouble;

        /**
         * The normal item model.
         */
        private final ModelResourceLocation itemModelChest;

        /**
         * The trap item model.
         */
        private final ModelResourceLocation itemModelChestTrap;

        /**
         * A list of render hooks.
         */
        @SideOnly(Side.CLIENT)
        private List<IRenderHook> renderHooks;

        public ChestType (String modid, String name) {

            this.modid = modid;
            this.name = name;
            this.textureNormal = new ResourceLocation(modid, "textures/entity/chest/" + name + ".png");
            this.textureDouble = new ResourceLocation(modid, "textures/entity/chest/" + name + "_double.png");
            this.itemModelChest = new ModelResourceLocation(new ResourceLocation(modid, "chest_" + name), "inventory");
            this.itemModelChestTrap = new ModelResourceLocation(new ResourceLocation(modid, "chest_trap_" + name), "inventory");
        }

        @Override
        public String getName () {

            return this.name;
        }

        @Override
        public String getModId () {

            return this.modid;
        }

        @Override
        public ResourceLocation getNormalTexture () {

            return this.textureNormal;
        }

        @Override
        public ResourceLocation getDoubleTexture () {

            return this.textureDouble;
        }

        @Override
        public ModelResourceLocation getNormalItemModel () {

            return this.itemModelChest;
        }

        @Override
        public ModelResourceLocation getTrapItemModel () {

            return this.itemModelChestTrap;
        }

        @Override
        public List<IRenderHook> renderHooks () {

            if (this.renderHooks == null) {

                this.renderHooks = new ArrayList<>();
            }

            return this.renderHooks;
        }

        @Override
        public void addRenderHook (IRenderHook callback) {

            if (this.renderHooks == null) {

                this.renderHooks = new ArrayList<>();
            }

            this.renderHooks.add(callback);
        }
    }

    /**
     * An interface that is used to define a chest type.
     */
    public interface IChestType {

        /**
         * Gets the name of the chest type.
         *
         * @return The name of the chest type.
         */
        String getName ();

        /**
         * Gets the modid that added the type.
         *
         * @return The modid that added the type.
         */
        String getModId ();

        /**
         * Gets the normal chest texture.
         *
         * @return The normal chest texture.
         */
        ResourceLocation getNormalTexture ();

        /**
         * Gets the double chest texture.
         *
         * @return The double chest texture.
         */
        ResourceLocation getDoubleTexture ();

        /**
         * Gets the normal item model.
         *
         * @return The normal item model.
         */
        ModelResourceLocation getNormalItemModel ();

        /**
         * Gets the trap item model.
         *
         * @return The trap item model.
         */
        ModelResourceLocation getTrapItemModel ();

        /**
         * Gets all the render hooks.
         *
         * @return The render hooks.
         */
        @SideOnly(Side.CLIENT)
        List<IRenderHook> renderHooks ();

        /**
         * Adds a render hook.
         *
         * @param callback Adds a render hook.
         */
        @SideOnly(Side.CLIENT)
        void addRenderHook (IRenderHook callback);
    }

    /**
     * An implementation of the chest render hook, used to render the trap overlays.
     */
    @SideOnly(Side.CLIENT)
    public static class RenderTrapChestOverlay implements IRenderHook {

        public static final IRenderHook INSTANCE = new RenderTrapChestOverlay();

        @Override
        public void render (TileEntityBasicChest te, double x, double y, double z, float partialTicks, int destroyStage, TileEntityBasicChestRenderer renderer, ModelChest model, boolean isSingle) {

            if (te.getChestType() == te.getBuilder().typeTrap) {

                renderer.bindTexture(isSingle ? ChestBuilder.TRAP_OVERLAY_SINGLE : ChestBuilder.TRAP_OVERLAY_DOUBLE);

                final float scale = 1.002F;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.translate(!isSingle ? -0.002F : -0.001F, -0.001F, -0.001F);
                model.renderAll();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

        @Override
        public void setup (TileEntityBasicChest te, double x, double y, double z, float partialTicks, int destroyStage, TileEntityBasicChestRenderer renderer, ModelChest model, boolean isSingle) {

        }
    }

    /**
     * An interface which defines a render hook for chests.
     */
    @SideOnly(Side.CLIENT)
    public interface IRenderHook {

        /**
         * Called after the chest is rendered.
         *
         * @param te The tile entity.
         * @param x The x pos.
         * @param y The y pos.
         * @param z The z pos.
         * @param partialTicks The partial ticks.
         * @param destroyStage The destroy stage.
         * @param renderer The renderer.
         * @param model The model.
         * @param isSingle Whether or not the chest is a single chest.
         */
        public void render (TileEntityBasicChest te, double x, double y, double z, float partialTicks, int destroyStage, TileEntityBasicChestRenderer renderer, ModelChest model, boolean isSingle);

        /**
         * Called before the chest is rendered.
         *
         * @param te The tile entity.
         * @param x The x pos.
         * @param y The y pos.
         * @param z The z pos.
         * @param partialTicks The partial ticks.
         * @param destroyStage The destroy stage.
         * @param renderer The renderer.
         * @param model The model.
         * @param isSingle Whether or not the chest is a single chest.
         */
        public void setup (TileEntityBasicChest te, double x, double y, double z, float partialTicks, int destroyStage, TileEntityBasicChestRenderer renderer, ModelChest model, boolean isSingle);
    }
}
