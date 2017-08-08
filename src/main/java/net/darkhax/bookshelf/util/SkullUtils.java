/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class SkullUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private SkullUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Create a skull from an instance of EntityPlayer.
     *
     * @param player The EntityPlayer to use the skin from.
     * @return ItemStack An ItemStack containing a skull that represents the passed player.
     */
    public static ItemStack createSkull (EntityPlayer player) {

        return createSkull(player.getDisplayNameString(), player.getUniqueID());
    }

    /**
     * Creates a skull using a players UUID.
     *
     * @param uuid The UUID of the player to base the skull on.
     * @return ItemStack An ItemStack containing a skull which represents the owner of the
     *         passed UUID.
     */
    public static ItemStack createSkull (String name, UUID uuid) {

        final ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
        StackUtils.prepareStackTag(stack);
        final NBTTagCompound ownerTag = new NBTTagCompound();
        ownerTag.setString("Name", name);
        ownerTag.setString("Id", uuid.toString());
        stack.getTagCompound().setTag("SkullOwner", ownerTag);
        return stack;
    }

    /**
     * Creates a skull from the list of publicly provided MHF accounts.
     *
     * @param account The MHFAccount to create the skull from.
     * @return ItemStack An ItemStack containing a skull which represents the MHF account.
     */
    public static ItemStack createSkull (MHFAccount account) {

        return createSkull(account.getMHFName());
    }

    /**
     * Creates a skull from the list of provided players.
     *
     * @param player The Player account to create the skull from
     * @return ItemStack An ItemStack containing a skull which represents the known Player.
     */
    public static ItemStack createSkull (Player player) {

        return createSkull(player.lastKnownName, UUID.fromString(player.playerId));
    }

    /**
     * Creates a skull that represents a player. This method can use plain text usernames, or
     * player UUID. It is recommended to use the UUID over the username, unless you are 100%
     * certain that the username will never change.
     *
     * @param owner The owner of the skull being created. Can be a username of a UUID.
     * @return ItemStack An ItemStack containing a skull which represents the passed owner
     *         name.
     */
    public static ItemStack createSkull (String owner) {

        final ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
        StackUtils.prepareStackTag(stack);
        stack.getTagCompound().setString("SkullOwner", owner);
        return stack;
    }

    /**
     * Creates a vanilla Wither Skeleton Skull.
     *
     * @return ItemStack An ItemStack containing a vanilla wither skeleton skull.
     */
    public static ItemStack getWitherSkeletonSkull () {

        return new ItemStack(Items.SKULL, 1, 1);
    }

    /**
     * Creates a vanilla Zombie Skull.
     *
     * @return ItemStack An ItemStack containing a vanilla zombie skull.
     */
    public static ItemStack getZombieSkull () {

        return new ItemStack(Items.SKULL, 1, 2);
    }

    /**
     * Creates a vanilla Creeper Skull.
     *
     * @return ItemStack An ItemStack containing a vanilla creeper skull.
     */
    public static ItemStack getCreeperSkull () {

        return new ItemStack(Items.SKULL, 1, 4);
    }

    /**
     * Creates a vanilla Steve Skull.
     *
     * @return ItemStack An ItemStack containing a vanilla steve skull.
     */
    public static ItemStack getSteveSkull () {

        return new ItemStack(Items.SKULL, 1, 3);
    }

    /**
     * Creates a vanilla Skeleton Skull.
     *
     * @return ItemStack An ItemStack containing a vanilla skeleton skull.
     */
    public static ItemStack getSkeletonSkull () {

        return new ItemStack(Items.SKULL, 1, 0);
    }

    /**
     * Creates an array of ItemStacks containing MHF Skulls.
     *
     * @return ItemStack[] An array of ItemStack containing every skull from the MHFAccount
     *         enum.
     */
    public static ItemStack[] getMHFSkulls () {

        int counter = 0;
        final ItemStack[] MHFSkulls = new ItemStack[MHFAccount.values().length];

        for (final MHFAccount account : MHFAccount.values()) {

            MHFSkulls[counter] = createSkull(account);
            counter++;
        }

        return MHFSkulls;
    }

    /**
     * Creates an array of ItemStacks containing the skulls of players from the Player enum.
     *
     * @return ItemStack[] An array of ItemStacks containing the skulls of known players.
     */
    public static ItemStack[] getPlayerSkulls () {

        int counter = 0;
        final ItemStack[] playerSkulls = new ItemStack[Player.values().length];

        for (final Player player : Player.values()) {

            playerSkulls[counter] = createSkull(player);
            counter++;
        }

        return playerSkulls;
    }

    public static enum MHFAccount {

        ALEX("Alex", "6ab43178-89fd-4905-97f6-0f67d9d76fd9"),
        BLAZE("Blaze", "4c38ed11-596a-4fd4-ab1d-26f386c1cbac"),
        CAVE_SPIDER("CaveSpider", "cab28771-f0cd-4fe7-b129-02c69eba79a5"),
        CHICKEN("Chicken", "92deafa9-4307-42d9-b003-88601598d6c0"),
        COW("Cow", "f159b274-c22e-4340-b7c1-52abde147713"),
        CREEPER("Creeper", "057b1c47-1321-4863-a6fe-8887f9ec265f"),
        ENDERMAN("Enderman", "40ffb372-12f6-4678-b3f2-2176bf56dd4b"),
        GHAST("Ghast", "063085a6-797f-4785-be1a-21cd7580f752"),
        GOLEM("Golem", "757f90b2-2344-4b8d-8dac-824232e2cece"),
        HEROBRINE("Herobrine", "9586e5ab-157a-4658-ad80-b07552a9ca63"),
        LAVASLIME("LavaSlime", "0972bdd1-4b86-49fb-9ecc-a353f8491a51"),
        MOOSHROOM("MushroomCow", "a46817d6-73c5-4f3f-b712-af6b3ff47b96"),
        OCELOT("Ocelot", "1bee9df5-4f71-42a2-bf52-d97970d3fea3"),
        PIG("Pig", "8b57078b-f1bd-45df-83c4-d88d16768fbe"),
        PIG_ZOMBIE("PigZombie", "18a2bb50-334a-4084-9184-2c380251a24b"),
        SHEEP("Sheep", "dfaad551-4e7e-45a1-a6f7-c6fc5ec823ac"),
        SKELETON("Skeleton", "a3f427a8-18c5-49c5-a4fb-64c6e0e1e0a8"),
        SLIME("Slime", "870aba93-40e8-48b3-89c5-32ece00d6630"),
        SPIDER("Spider", "5ad55f34-41b6-4bd2-9c32-18983c635936"),
        SQUID("Squid", "72e64683-e313-4c36-a408-c66b64e94af5"),
        STEVE("Steve", "c06f8906-4c8a-4911-9c29-ea1dbd1aab82"),
        VILLAGER("Villager", "bd482739-767c-45dc-a1f8-c33c40530952"),
        WITHER_SKELETON("WSkeleton", "7ed571a5-9fb8-416c-8b9d-fb2f446ab5b2"),
        ZOMBIE("Zombie", "daca2c3d-719b-41f5-b624-e4039e6c04bd"),
        CACTUS("Cactus", "1d9048db-e836-4b9a-a108-55014922f1ae"),
        CAKE("Cake", "afb489c4-9fc8-48a4-98f2-dd7bea414c9a"),
        CHEST("Chest", "73d4e068-3a6d-4c8b-8f85-3323546955c4"),
        COCONUT_BROWN("CoconutB", "62efa973-f626-4092-aede-57ffbe84ff2b"),
        COCONUT_GREEN("CoconutG", "74556fea-28ed-4458-8db2-9a8220da0c12"),
        MELON("Melon", "1c7d9784-47ea-4bf3-bc23-acf260b436e6"),
        LOG("OakLog", "e224e5ec-e299-4005-ae22-3b0f77a57714"),
        PRESENT_GREEN("Present1", "156b251b-12e0-4829-a130-a61b53ba7720"),
        PRESENT_RED("Present2", "f1eb7cad-e2c0-4e9e-8aad-1eae21d5fd95"),
        PUMPKIN("Pumpkin", "f44d355b-b6ae-4ba8-8e62-ae6441854785"),
        TNT1("TNT", "d43af93c-c330-4a3d-bab8-ee74234a011a"),
        TNT2("TNT2", "55e73380-a973-4a52-9bb5-1efa5256125c"),
        ARROW_UP("ArrowUp", "fef039ef-e6cd-4987-9c84-26a3e6134277"),
        ARROW_DOWN("ArrowDown", "68f59b9b-5b0b-4b05-a9f2-e1d1405aa348"),
        ARROW_LEFT("ArrowLeft", "a68f0b64-8d14-4000-a95f-4b9ba14f8df9"),
        ARROW_RIGHT("ArrowRight", "50c8510b-5ea0-4d60-be9a-7d542d6cd156"),
        EXCLAMATION("Exclamation", "d3c47f6f-ae3a-45c1-ad7c-e2c762b03ae6"),
        QUESTION("Question", "606e2ff0-ed77-4842-9d6c-e1d3321c7838");

        /**
         * The base of the username, without the MHF prefix.
         */
        private final String username;

        /**
         * The UUID tied to the account.
         */
        private final String playerId;

        /**
         * An enumeration of all accounts provided by Mojang under the MHF format.
         *
         * @param username The username tied to the account.
         * @param uuid The uuid tied to the account.
         */
        MHFAccount (String username, String uuid) {

            this.username = username;
            this.playerId = uuid;
        }

        /**
         * Provides the base name for this skull. This is the base name, and not the full
         * username. Use getMHFName to get an actual username that can be used.
         *
         * @return String The skull name, without the MHF_ prefix.
         */
        public String getBaseName () {

            return this.username;
        }

        /**
         * Provides the username in the MHF format. The MHF format is a format used by Mojang
         * to provide a series of additional player names which can reliably be used for things
         * like skulls.
         *
         * @return String The basic username, with the MHF_ prefix.
         */
        public String getMHFName () {

            return "MHF_" + this.username;
        }
    }

    public static enum Player {

        ILEXICONN("iLexiconn", "40e85e42-21f6-46b6-b5b3-6aeb07f3e3fd"),
        GEFORCE("Geforce", "e42a08be-4aa9-42c3-b8d9-29bde3aa533d"),
        DARKHAX("Darkhax", "d183e5a2-a087-462a-963e-c3d7295f9ec5"),
        LCLC98("lclc98", "f0f76db6-0461-4151-8ba7-392d65d62ea3"),
        PROFMOBIUS("Profmobius", "0535d36f-aee6-4398-9355-a942b4a7f66b"),
        COOLSQUID("CoolSquid", "03a42a75-223a-4307-99c1-b69162ad6a6f"),
        SUBARAKI("Subaraki", "69eb9fdf-d2ee-48e9-9a95-2b483c5de40f"),
        FREYJA("freyjadono", "db731416-ab08-4fd9-83f7-ffd1e7fad09f"),
        CHICKENBONES("ChickenBones", "93417060-56f9-4cd5-8fd6-b73cb39ef2a3"),
        CYANIDEX("CyanideX", "54972f65-b920-4a60-b539-dc443c16cd29"),
        VIKESTEP("VikeStep", "6ef1716c-d032-4d6a-bb96-ee78caca9737"),
        MEGALOLOFU("megaloloful", "51ee67f8-57fa-4d88-81b1-e91b73048e60"),
        BLORPH("Blorph", "6c25e5b2-e8d4-4b64-b5c1-fb7f84cce66c"),
        MR_TOOTS("Mr_Toots", "61daa8b8-0ac0-4efa-a17e-1860e7c7db00"),
        VICTORIOUS("victorious3", "b027a4f4-d480-426c-84a3-a9cb029f4b72"),
        SHADOWCLAIMER("ShadowClaimer", "52ff0245-4926-4d8f-92e6-332645a9c392"),
        TEDYHERE("TedyHere", "99598e23-9e10-4bf6-af80-fdcda4b80f27"),
        JARED11108("Jaredlll08", "3bf32666-f9ba-4060-af02-53bdb0df38fc"),
        SANANDREASMC("sanandreasMC", "044d980d-5c2a-4030-95cf-cbfde69ea3cb");

        /**
         * The last known username tied to the account.
         */
        public final String lastKnownName;

        /**
         * The UUID tied to the account.
         */
        private final String playerId;

        /**
         * An enumeration of a few player names. This list includes contributors, and friends.
         *
         * @param username The last known username tied to the account.
         * @param uuid The UUID tied to the account.
         */
        Player (String username, String uuid) {

            this.lastKnownName = username;
            this.playerId = uuid;
        }

        /**
         * Gets the last name that was known for the profile.
         *
         * @return The last known name for the profile.
         */
        public String getLastKnownName () {

            return this.lastKnownName;
        }

        /**
         * Gets the UUID for the profile.
         *
         * @return The UUID for the profile.
         */
        public String getId () {

            return this.playerId;
        }
    }
}
