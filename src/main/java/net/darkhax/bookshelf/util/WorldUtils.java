/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public final class WorldUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private WorldUtils () {

        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets the display name of a world.
     *
     * @param world The world to get the name of.
     * @return The name of the world.
     */
    public static String getWorldName (World world) {

        String result = "Unknown";

        // TODO add more fallback options
        if (world.provider != null) {

            result = world.provider.getDimensionType().getName();
        }

        return result;
    }

    /**
     * Gets the amount of loaded chunks.
     *
     * @param world The world to get the chunk count of.
     * @return The amount of chunks. -1 means it was unable to get the amount.
     */
    public static int getLoadedChunks (WorldServer world) {

        return world.getChunkProvider() != null ? world.getChunkProvider().getLoadedChunkCount() : -1;
    }

    /**
     * Gets the dimension id of a world.
     *
     * @param world The world to get the id of.
     * @return The id of the world. 0 (surface) is used if none is found.
     */
    public static int getDimId (WorldServer world) {

        return world.provider != null ? world.provider.getDimension() : 0;
    }

    /**
     * Checks if two block positions are in the same chunk in a given world.
     *
     * @param first The first position.
     * @param second The second position.
     * @return Whether or not the two positions are in the same chunk.
     */
    public static boolean areSameChunk (BlockPos first, BlockPos second) {

        return new ChunkPos(first).equals(new ChunkPos(second));
    }

    /**
     * Checks if two block positions are in the same chunk in a given world.
     *
     * @param world The world to check within.
     * @param first The first position.
     * @param second The second position.
     * @return Whether or not the two positions are in the same chunk.
     */
    @Deprecated
    public static boolean areSameChunk (World world, BlockPos first, BlockPos second) {

        return areSameChunk(first, second);
    }

    /**
     * Checks if the dimension id of a world matches the provided dimension id.
     *
     * @param world The world to check.
     * @param id The dimension id you are looking for.
     * @return Whether or not they are the same.
     */
    public static boolean isDimension (World world, int id) {

        return getDimensionId(world) == id;
    }

    /**
     * Checks if the dimension type of a world matches the provided dimension type.
     *
     * @param world The world to check.
     * @param type The dimension type you are looking for.
     * @return Whether or not they are the same.
     */
    public static boolean isDimension (World world, DimensionType type) {

        return getDimensionType(world) == type;
    }

    /**
     * Gets the dimension id of a world.
     *
     * @param world The world you are looking into.
     * @return The id of the world.
     */
    public static int getDimensionId (World world) {

        return getDimensionType(world).getId();
    }

    /**
     * Gets the dimension type of a world.
     *
     * @param world The world you are looking into.
     * @return The type of the world.
     */
    public static DimensionType getDimensionType (World world) {

        return world.provider.getDimensionType();
    }

    /**
     * Attempts to set the biome of an entire chunk. Please note that this will also cause
     * conecting chunks to do an update, and will cause the targeted chunk to recieve a render
     * update.
     *
     * @param world The world to set the biome in.
     * @param pos The block position to target. This will target the chunk the psotion is in.
     * @param biome The biome to set the chunk to.
     */
    public static void setBiomes (World world, BlockPos pos, Biome biome) {

        try {

            final Chunk chunk = world.getChunkFromBlockCoords(pos);
            final byte[] biomes = chunk.getBiomeArray();
            Arrays.fill(biomes, (byte) Biome.getIdForBiome(biome));
            chunk.markDirty();

            updateNearbyChunks(world, chunk, true, true);
        }

        catch (final Exception e) {

            Constants.LOG.warn(e, "Unable to set biome for Pos: {}, Biome: {}", pos.toString(), biome.getRegistryName());
        }
    }

    /**
     * Marks a chunk for an update. This will set it dirty, and potentially do a render update.
     *
     * @param world The world to update chunks in.
     * @param chunk The chunk to update.
     * @param render Whether or not you want a render update.
     */
    public static void markChunkForUpdate (World world, Chunk chunk, boolean render) {

        chunk.markDirty();

        if (render) {

            final BlockPos initial = chunk.getPos().getBlock(1, 1, 1);
            world.markBlockRangeForRenderUpdate(initial, initial);
        }
    }

    /**
     * Updates all chunks near the provided chunk. This is a 3x3 area centered on the chunk.
     *
     * @param world The world to update chunks in.
     * @param chunk The chunk to update.
     * @param includeSelf Whether or not the base chunk should be updated.
     * @param render Whether or not you want a render update.
     */
    public static void updateNearbyChunks (World world, Chunk chunk, boolean includeSelf, boolean render) {

        for (final Chunk other : getNearbyChunks(world, chunk)) {

            if (other != chunk || includeSelf) {

                markChunkForUpdate(world, other, render);
            }
        }
    }

    /**
     * Gets a list of 9 chunks in a 3x3 area, centered around the passed chunk.
     *
     * @param world The world to get chunks from.
     * @param chunk The initial chunk.
     * @return A list of 9 chunks that are near the passed chunk, as well as the initial chunk.
     */
    public static List<Chunk> getNearbyChunks (World world, Chunk chunk) {

        return getNearbyChunks(world, chunk.getPos());
    }

    /**
     * Gets a list of 9 chunks in a 3x3 area, centered around the passed chunk position.
     *
     * @param world The world to get chunks from.
     * @param chunk The chunk position.
     * @return A list of 9 chunks that are near the passed chunk pos, as well as the chunk at
     *         the passed position.
     */
    public static List<Chunk> getNearbyChunks (World world, ChunkPos chunk) {

        final List<Chunk> chunks = new ArrayList<>();

        for (int offX = -1; offX < 2; offX++) {

            for (int offY = -1; offY < 2; offY++) {

                chunks.add(world.getChunkFromChunkCoords(chunk.x + offX, chunk.z + offY));
            }
        }

        return chunks;
    }

    /**
     * Gets a random position within a chunk. This will load the chunk if it is not already
     * loaded.
     *
     * @param world The world to get a position within.
     * @param x The chunk X position.
     * @param z The chunk Y position.
     * @return A random position within the chunk.
     */
    public static BlockPos getRandomChunkPosition (World world, int x, int z) {

        final Chunk chunk = world.getChunkFromChunkCoords(x, z);
        final int posX = x * 16 + world.rand.nextInt(16);
        final int posZ = z * 16 + world.rand.nextInt(16);
        final int height = MathHelper.roundUp(chunk.getHeight(new BlockPos(posX, 0, posZ)) + 1, 16);
        final int posY = world.rand.nextInt(height > 0 ? height : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(posX, posY, posZ);
    }

    /**
     * Gets the WorldType for a WorldProvider. This is a wrapper for an access transformer.
     *
     * @param provider The world provider to pull info from.
     * @return The WorldType of the provider.
     */
    public static WorldType getWorldType (WorldProvider provider) {

        return provider.terrainType;
    }

    /**
     * Sets the WorldType for a WorldProvider. This is a wrapper for an access transformer.
     *
     * @param provider The provider to set the type of.
     * @param type The type to set the world to.
     */
    public static void setWorldType (WorldProvider provider, WorldType type) {

        provider.terrainType = type;
    }

    /**
     * Gets the generator settings for a world. This is a wrapper for an access transformer.
     *
     * @param provider The provider to set the generator settings of.
     * @return The settings to apply.
     */
    public static String getWorldSettings (WorldProvider provider) {

        return provider.generatorSettings;
    }

    public static void setWorldSettings (WorldProvider provider, String settings) {

        provider.generatorSettings = settings;
    }

    /**
     * Attempts to spawn mobs in a chunk.
     *
     * @param world The world to spawn the mobs in.
     * @param pos The position of the chunk to spawn in.
     * @param mobType The type of mob to try and spawn.
     * @param spawnHook A hook for running additional code on mob spawn.
     */
    public static void attemptChunkSpawn (WorldServer world, BlockPos pos, EnumCreatureType mobType, @Nullable Consumer<EntityLiving> spawnHook) {

        final ChunkPos chunkPos = new ChunkPos(pos);
        final BlockPos blockpos = getRandomChunkPosition(world, chunkPos.x, chunkPos.z);
        final int randX = blockpos.getX();
        final int randY = blockpos.getY();
        final int randZ = blockpos.getZ();

        // Checks if block is not a solid cube.
        if (!world.getBlockState(blockpos).isNormalCube()) {

            final int offsetX = randX + MathsUtils.nextIntInclusive(-6, 6);
            final int spawnY = randY + MathsUtils.nextIntInclusive(-1, 1);
            final int offsetY = randZ + MathsUtils.nextIntInclusive(-6, 6);

            final BlockPos currentPos = new BlockPos(offsetX, spawnY, offsetY);

            final float spawnX = offsetX + 0.5F;
            final float spawnZ = offsetY + 0.5F;

            // Get a random spawn list entry for the current chunk.
            final SpawnListEntry spawnListEntry = world.getSpawnListEntryForTypeAt(mobType, currentPos);

            if (spawnListEntry == null) {

                return;
            }

            // Checks if the mob entry is valid for the current position.
            if (world.canCreatureTypeSpawnHere(mobType, spawnListEntry, currentPos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(spawnListEntry.entityClass), world, currentPos)) {

                // Get a random amount of mobs to spawn by using the pack size numbers.
                final int amountToSpawn = MathsUtils.nextIntInclusive(spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount);

                for (int attempt = 0; attempt < amountToSpawn; attempt++) {

                    EntityLiving spawnedMob;

                    try {

                        spawnedMob = spawnListEntry.newInstance(world);
                    }

                    catch (final Exception exception) {

                        Constants.LOG.catching(exception);
                        return;
                    }

                    if (spawnedMob != null) {

                        spawnedMob.setLocationAndAngles(spawnX, spawnY, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);

                        // Run forge's entity spawn check event
                        final Result canSpawn = ForgeEventFactory.canEntitySpawn(spawnedMob, world, spawnX, spawnY, spawnZ, false);

                        if (canSpawn == Result.ALLOW || canSpawn == Result.DEFAULT && spawnedMob.getCanSpawnHere() && spawnedMob.isNotColliding()) {

                            if (!ForgeEventFactory.doSpecialSpawn(spawnedMob, world, spawnX, spawnY, spawnZ)) {

                                spawnedMob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(spawnedMob)), null);
                            }

                            if (spawnedMob.isNotColliding()) {

                                world.spawnEntity(spawnedMob);

                                if (spawnHook != null) {

                                    spawnHook.accept(spawnedMob);
                                }
                            }

                            else {

                                spawnedMob.setDead();
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isHorizontal (EnumFacing facing) {

        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }
}