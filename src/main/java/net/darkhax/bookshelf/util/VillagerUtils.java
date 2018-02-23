/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class VillagerUtils {

    // Farmer
    public static final VillagerProfession PROFESSION_FARMER = getProfession("minecraft:farmer");

    public static final int CAREER_ID_FARMER = 1;
    public static final int CAREER_ID_FISHERMAN = 2;
    public static final int CAREER_ID_SHEPHERD = 3;
    public static final int CAREER_ID_FLETCHER = 4;

    public static final VillagerCareer CAREER_FLETCHER = PROFESSION_FARMER.getCareer(CAREER_ID_FLETCHER);
    public static final VillagerCareer CAREER_SHEPHERD = PROFESSION_FARMER.getCareer(CAREER_ID_SHEPHERD);
    public static final VillagerCareer CAREER_FISHERMAN = PROFESSION_FARMER.getCareer(CAREER_ID_FISHERMAN);
    public static final VillagerCareer CAREER_FARMER = PROFESSION_FARMER.getCareer(CAREER_ID_FARMER);

    // Librarian
    public static final VillagerProfession PROFESSION_LIBRARIAN = getProfession("minecraft:librarian");

    public static final int CAREER_ID_LIBRARIAN = 1;
    public static final int CAREER_ID_CARTOGRAPHER = 2;

    public static final VillagerCareer CAREER_LIBRARIAN = PROFESSION_LIBRARIAN.getCareer(CAREER_ID_LIBRARIAN);
    public static final VillagerCareer CAREER_CARTOGRAPHER = PROFESSION_LIBRARIAN.getCareer(CAREER_ID_CARTOGRAPHER);

    // Priest
    public static final VillagerProfession PROFESSION_PRIEST = getProfession("minecraft:priest");

    public static final int CAREER_ID_CLERIC = 1;

    public static final VillagerCareer CAREER_CLERIC = PROFESSION_PRIEST.getCareer(CAREER_ID_CLERIC);

    // Blacksmith
    public static final VillagerProfession PROFESSION_SMITH = getProfession("minecraft:smith");

    public static final int CAREER_ID_ARMOR_SMITH = 1;
    public static final int CAREER_ID_WEAPON_SMITH = 2;
    public static final int CAREER_ID_TOOL_SMITH = 3;

    public static final VillagerCareer CAREER_TOOL_SMITH = PROFESSION_SMITH.getCareer(CAREER_ID_TOOL_SMITH);
    public static final VillagerCareer CAREER_ARMOR_SMITH = PROFESSION_SMITH.getCareer(CAREER_ID_ARMOR_SMITH);
    public static final VillagerCareer CAREER_WEAPON_SMITH = PROFESSION_SMITH.getCareer(CAREER_ID_WEAPON_SMITH);

    // Butcher
    public static final VillagerProfession PROFESSION_BUTCHER = getProfession("minecraft:butcher");

    public static final int CAREER_ID_BUTCHER = 1;
    public static final int CAREER_ID_LEATHER_WORKER = 2;

    public static final VillagerCareer CAREER_BUTCHER = PROFESSION_BUTCHER.getCareer(CAREER_ID_BUTCHER);
    public static final VillagerCareer CAREER_LEATHER_WORKER = PROFESSION_BUTCHER.getCareer(CAREER_ID_LEATHER_WORKER);

    // Nitwit
    public static final VillagerProfession PROFESSION_NITWIT = getProfession("minecraft:nitwit");

    public static final int CAREER_ID_NITWIT = 1;

    public static final VillagerCareer CAREER_NITWIT = PROFESSION_NITWIT.getCareer(CAREER_ID_NITWIT);

    /**
     * Adds a trade to a villager career.
     *
     * @param career The career to add the trade to.
     * @param level The level of the trade. Players start at level one, and trading unlocks
     *        more. If you make this less than 1 lex will show up behind you and stab you.
     * @param trades The trades to add to the villager.
     */
    public static void addVillagerTrade (VillagerCareer career, int level, ITradeList... trades) {

        career.addTrade(level, trades);
    }

    /**
     * Gets a villager from a string id. This method exists because ObjectHolder didn't work in
     * a dev env for me.
     *
     * @param id The id of the villager profession type.
     * @return The villager profession. This can be null!
     */
    @Nullable
    public static VillagerProfession getProfession (String id) {

        return ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(id));
    }

    /**
     * Checks if a profession id exists.
     *
     * @param professionId The profession id to search for.
     * @return Whether or not the profession exists.
     */
    public static boolean doesProfessionExist (String professionId) {

        return ForgeRegistries.VILLAGER_PROFESSIONS.containsKey(new ResourceLocation(professionId));
    }

    /**
     * Checks if a career belongs to a profession.
     *
     * @param profession The possible owner.
     * @param career The career to check ownership of.
     * @return Whether or not the profession owns the passed career.
     */
    public static boolean hasCareer (VillagerProfession profession, VillagerCareer career) {

        return getCareers(profession).contains(career);
    }

    /**
     * Gets the list of careers for a profession. This list should be treated as read only!
     *
     * @param profession The profession to get careers from.
     * @return A list of villager careers for the profession.
     * @deprecated This method is deprecated in preparation of a potential Forge PR.
     */
    @Deprecated
    public static List<VillagerCareer> getCareers (VillagerProfession profession) {

        return ReflectionHelper.getPrivateValue(VillagerProfession.class, profession, "careers");
    }

    /**
     * Gets the profession a career belongs to.
     *
     * @param career The career to get the profession from.
     * @return The profession that the career belongs to.
     * @deprecated This method is deprecated in preparation of a potential Forge PR.
     */
    @Deprecated
    public static VillagerProfession getProfession (VillagerCareer career) {

        return ReflectionHelper.getPrivateValue(VillagerCareer.class, career, "profession");
    }

    /**
     * Gets the id of the profession a career belongs to.
     *
     * @param career The career to get the profession id for.
     * @return The id of the profession the career belongs to.
     * @deprecated This method is deprecated in preparation of a potential Forge PR.
     */
    @Deprecated
    public static int getProfessionId (VillagerCareer career) {

        return ReflectionHelper.getPrivateValue(VillagerCareer.class, career, "id");
    }

    /**
     * Gets the raw list of trades for a profession. This should be treated as read only!
     *
     * @param career The career to get trades from.
     * @return The raw list of trades. Index of the list determines the level. Levels start at
     *         1 but are offset by 1 like arrays are.
     * @deprecated This method is deprecated in preparation of a potential Forge PR.
     */
    @Deprecated
    public static List<List<ITradeList>> getRawTradeList (VillagerCareer career) {

        return ReflectionHelper.getPrivateValue(VillagerCareer.class, career, "trades");
    }
}
