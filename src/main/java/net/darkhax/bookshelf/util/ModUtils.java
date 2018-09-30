/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class ModUtils {

    /**
     * This cache is used by {@link #getSortedEntries(IForgeRegistry)} to improve repeat
     * performance of the method. Calling {@link #getSortedEntries(IForgeRegistry)} before all
     * entries have been registered will lock out new ones from being found.
     */
    private static final Map<IForgeRegistry<?>, Multimap<String, ?>> REGISTRY_CACHE = new HashMap<>();

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private ModUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets the name of a mod that registered the passed object. Has support for a wide range
     * of registerable objects such as blocks, items, enchantments, potions, sounds, villagers,
     * biomes, and so on.
     *
     * @param registerable The registerable object. Accepts anything that extends
     *        IForgeRegistryEntry.Impl. Current list includes BiomeGenBase, Block, Enchantment,
     *        Item, Potion, PotionType, SoundEvent and VillagerProfession.
     * @return String The name of the mod that registered the object.
     */
    public static String getModName (IForgeRegistryEntry.Impl<?> registerable) {

        final String modID = registerable.getRegistryName().getNamespace();
        final ModContainer mod = getModContainer(modID);
        return mod != null ? mod.getName() : "minecraft".equals(modID) ? "Minecraft" : "Unknown";
    }

    /**
     * Gets the name of a mod that registered the entity. Due to Entity not using
     * IForgeRegistryEntry.Impl a special method is required.
     *
     * @param entity The entity to get the mod name for.
     * @return String The name of the mod that registered the entity.
     */
    public static String getModName (Entity entity) {

        if (entity == null) {
            return "Unknown";
        }

        final EntityRegistration reg = getRegistryInfo(entity);

        if (reg != null) {

            final ModContainer mod = reg.getContainer();

            if (mod != null) {
                return mod.getName();
            }

            return "Unknown";
        }

        return "Minecraft";
    }

    /**
     * Gets registry info for an entity.
     *
     * @param entity The entity to get registry info of.
     * @return The entities registry info. Can be null.
     */
    public static EntityRegistration getRegistryInfo (Entity entity) {

        return getRegistryInfo(entity.getClass());
    }

    /**
     * Gets registry info for an entity, from it's class.
     *
     * @param entity The class to look for.
     * @return The entities registry info. Can be null.
     */
    public static EntityRegistration getRegistryInfo (Class<? extends Entity> entity) {

        return EntityRegistry.instance().lookupModSpawn(entity, false);
    }

    /**
     * Gets a mod container by it's ID.
     *
     * @param modID The ID of the mod to grab.
     * @return The ModContainer using that ID.
     */
    public static ModContainer getModContainer (String modID) {

        return Loader.instance().getIndexedModList().get(modID);
    }

    /**
     * Gets the name of a mod from it's ID.
     *
     * @param modId The mod to look up.
     * @return The name of the mod.
     */
    public static String getModName (String modId) {

        final ModContainer mod = getModContainer(modId);
        return mod != null ? mod.getName() : modId;
    }

    /**
     * Searches through the array of CreativeTabs and finds the first tab with the same label
     * as the one passed.
     *
     * @param label: The label of the tab you are looking for.
     * @return CreativeTabs: A CreativeTabs with the same label as the one passed. If this is
     *         not found, you will get null.
     */
    @SideOnly(Side.CLIENT)
    public static CreativeTabs getTabFromLabel (String label) {

        for (final CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (tab.getTabLabel().equalsIgnoreCase(label)) {
                return tab;
            }
        }

        return null;
    }

    /**
     * Creates a ResourceLocation for a string, using the active mod container as the owner of
     * the ID.
     *
     * @param id The id for the specific entry.
     * @return A ResourceLocation for the entry.
     */
    public static ResourceLocation getIdForCurrentContainer (String id) {

        final int index = id.lastIndexOf(':');
        final String entryName = index == -1 ? id : id.substring(index + 1);
        final ModContainer mod = Loader.instance().activeModContainer();
        final String prefix = mod == null || mod instanceof InjectedModContainer && ((InjectedModContainer) mod).wrappedContainer instanceof FMLContainer ? "minecraft" : mod.getModId().toLowerCase();

        return new ResourceLocation(prefix, entryName);
    }

    /**
     * Creates a sorted version of a ForgeRegistry. This will only contain entries that were
     * present at the time of calling it. Entries are sorted by their owning modid.
     *
     * @param registry The registry to sort.
     * @return A map of all entries sorted by the owning mod id.
     */
    @SuppressWarnings("unchecked")
    public static <T extends IForgeRegistryEntry<T>> Multimap<String, T> getSortedEntries (IForgeRegistry<T> registry) {

        if (REGISTRY_CACHE.containsKey(registry)) {

            return (Multimap<String, T>) REGISTRY_CACHE.get(registry);
        }

        final Multimap<String, T> map = ArrayListMultimap.create();

        for (final T entry : registry.getValues()) {

            if (entry.getRegistryName() != null) {

                map.put(entry.getRegistryName().getNamespace(), entry);
            }
        }

        REGISTRY_CACHE.put(registry, map);
        return map;
    }

    /**
     * Gets a ResourceLocation where the domain/modid is pulled from the active mod id.
     *
     * @param name The name of the id to create.
     * @return The ResourceLocation with the active mod as the domain/id.
     */
    public static ResourceLocation getIdForActiveMod (String name) {

        if (!name.contains(":")) {

            final ModContainer container = Loader.instance().activeModContainer();

            if (container != null) {

                return new ResourceLocation(container.getModId(), name);
            }
        }

        return new ResourceLocation(name);
    }
}