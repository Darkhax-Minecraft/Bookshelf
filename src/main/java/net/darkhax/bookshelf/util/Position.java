package net.darkhax.bookshelf.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class Position {
    
    public static int positionX;
    public static int positionY;
    public static int positionZ;
    
    public Position(MovingObjectPosition mop) {
    
        this(mop.blockX, mop.blockY, mop.blockZ);
    }
    
    /**
     * Creates an instance of Position using a NBTTagCompound.
     * 
     * @param nbt: A NBTTagCompound containing the required information to create a Position
     *            instance.
     */
    public Position(NBTTagCompound nbt) {
    
        this(nbt.getInteger("positionX"), nbt.getInteger("positionY"), nbt.getInteger("positionZ"));
    }
    
    /**
     * Creates an instance of a Position using an entity. Positions are grabbed from the entity
     * object provided.
     * 
     * @param entity: Instance of any minecraft entity.
     */
    public Position(Entity entity) {
    
        this((int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }
    
    /**
     * Creates an instance of a Position using three integer coordinates. Should be used if you
     * don't care about precision.
     * 
     * @param x: The X position to store.
     * @param y: The Y position to store.
     * @param z: The Z position to store.
     */
    public Position(int x, int y, int z) {
    
        this.positionX = x;
        this.positionY = y;
        this.positionZ = z;
    }
    
    /**
     * Moves the position of this instance to the North by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     * @return Position: A new Position instance, which has been translated north by the
     *         specified distance.
     */
    public void translateNorth (int distance) {
    
        this.translate(0, 0, -distance);
    }
    
    /**
     * Moves the position of this instance to the east by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     */
    public void translateEast (int distance) {
    
        this.translate(distance, 0, 0);
    }
    
    /**
     * Moves the position of this instance to the south by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     */
    public void translateSouth (int distance) {
    
        this.translate(0, 0, distance);
    }
    
    /**
     * Moves the position of this instance to the west by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     */
    public void translateWest (int distance) {
    
        this.translate(-distance, 0, 0);
    }
    
    /**
     * Moves the position of this instance to the up by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     */
    public void translateUp (int distance) {
    
        this.translate(0, distance, 0);
    }
    
    /**
     * Moves the position of this instance down by the specified distance.
     * 
     * @param distance: The distance to move the position by.
     */
    public void translateDown (int distance) {
    
        this.translate(0, -distance, 0);
    }
    
    /**
     * Provides a way to translate a Position by using an EnumFacing. Particularly useful when
     * you don't necessarily know the direction.
     * 
     * @param facing: The EnumFacing to translate by.
     * @param distance: The distance to translate in a particular direction.
     */
    public void translate (EnumFacing facing, int distance) {
    
        this.translate(facing.getFrontOffsetX() * distance, facing.getFrontOffsetY() * distance, facing.getFrontOffsetZ() * distance);
    }
    
    /**
     * Allows for the position to be moved to a new location based on specific directions
     * provided. Positive values move into positive, while negative values will move value
     * towards the negatives.
     * 
     * @param transX: The amount of blocks to move on the X axis.
     * @param transY: The amount of blocks to move on the Y axis.
     * @param transZ: The amount of blocks to move on the Z axis.
     */
    public void translate (int transX, int transY, int transZ) {
    
        this.positionX += transX;
        this.positionY += transY;
        this.positionZ += transZ;
    }
    
    /**
     * Stores the data from the Position instance to a provided NBTTagCompound.
     * 
     * @param dataTag: A NBTTagCompound used to write all position data to.
     * @return NBTTagCompound: A NBTTagCompound containing location data.
     */
    public NBTTagCompound writeToTag (NBTTagCompound dataTag) {
    
        dataTag.setInteger("positionX", this.positionX);
        dataTag.setInteger("positionY", this.positionY);
        dataTag.setInteger("positionZ", this.positionZ);
        return dataTag;
    }
    
    /**
     * Calculates the distance between this position and another position.
     * 
     * @param position: A Position instance to check the distance of.
     * @return double: The distance between this position and the provided one.
     */
    public double distanceToPosition (Position position) {
    
        return distanceTo(position.positionX, position.positionY, position.positionZ);
    }
    
    /**
     * Calculates the distance between this position and the position of the specified entity.
     * 
     * @param entity: An instance of an entity which is being used to calculate a distance.
     * @return double: The distance between the position and the Entity's position.
     */
    public double distanceToEntity (Entity entity) {
    
        return distanceTo((int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }
    
    /**
     * Calculates the distance between this position and a provided position.
     * 
     * @param x: The second X position.
     * @param y: The second Y position.
     * @param z: The second Z position.
     * @return double: The distance between the two locations.
     */
    public double distanceTo (int x, int y, int z) {
    
        int difX = this.positionX - x;
        int difY = this.positionY - y;
        int difZ = this.positionZ - z;
        
        return Math.sqrt(difX * difX + difY * difY + difZ * difZ);
    }
    
    /**
     * Sends an EntityPlayer to the position in the world.
     * 
     * @param player: The player to be sent to this position.
     */
    public void sendPlayerToPos (EntityPlayer player) {
    
        player.setPositionAndUpdate(positionX, positionY, positionZ);
    }
    
    /**
     * Retrieves the Block at this position within the world.
     * 
     * @param world: The world to grab the Block from.
     * @return Block: The Block which is found at this current position. May be null, or
     *         reference Air.
     */
    public Block getBlockAtPosition (World world) {
    
        return world.getBlock(this.positionX, this.positionY, this.positionZ);
    }
    
    /**
     * Retrieves the meta value of the Block at this position within the world.
     * 
     * @param world: The world to grab the meta value from.
     * @return int: The int representation of the meta at the current position. May be 0 if no
     *         block is found.
     */
    public int getMetaAtPosition (World world) {
    
        return world.getBlockMetadata(this.positionX, this.positionY, this.positionZ);
    }
    
    /**
     * Retrieves the TileEntity at the current position within the world.
     * 
     * @param world: The world to grab the TileEntity from.
     * @return TileEntity: The TileEntity at the current position. This may be null.
     */
    public TileEntity getTileEntityAtPosition (World world) {
    
        return world.getTileEntity(this.positionX, this.positionY, this.positionZ);
    }
    
    /**
     * Retrieves the BiomeGenBase atht he current position within the world.
     * 
     * @param world: The world to grab the BiomeGenBase from.
     * @return BiomeGenBase: The biome in place at the current position.
     */
    public BiomeGenBase getBiomeAtPosition (World world) {
    
        return world.getBiomeGenForCoords(this.positionX, this.positionZ);
    }
    
    /**
     * Sets the block at the current position to air.
     * 
     * @param world: The world to set the block within.
     */
    public void setBlockToAir (World world) {
    
        world.setBlockToAir(this.positionX, this.positionY, this.positionZ);
    }
    
    /**
     * Updates all neighbor positions that a change has occurred within this position.
     * 
     * @param world: The world to update positions within.
     */
    public void updateNeighborPositions (World world) {
    
        world.notifyBlocksOfNeighborChange(this.positionX, this.positionY, this.positionZ, this.getBlockAtPosition(world));
    }
    
    /**
     * Updates the metadata of the block at the current position.
     * 
     * @param world: The world to update the block in.
     * @param meta: The meta value to assign the block at this position.
     */
    public void updateMetaData (World world, int meta) {
    
        world.setBlockMetadataWithNotify(this.positionX, this.positionY, this.positionZ, meta, 2);
    }
    
    /**
     * Simple way to create a basic copy of the current Position.
     * 
     * @return Position: A basic clone of the current Position.
     */
    public Position copy () {
    
        return new Position(this.positionX, this.positionY, this.positionZ);
    }
    
    @Override
    public boolean equals (Object obj) {
    
        if (obj instanceof Position) {
            
            Position pos = (Position) obj;
            return this.positionX == pos.positionX && this.positionY == pos.positionY && this.positionZ == pos.positionZ;
        }
        
        return false;
    }
    
    @Override
    public String toString () {
    
        return "X: " + this.positionX + " Y: " + this.positionY + " Z: " + this.positionZ;
    }
}