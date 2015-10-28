package net.darkhax.bookshelf.lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public final class Position implements Comparable<Position>, Serializable {
    
    private final int x;
    private final int y;
    private final int z;
    
    /**
     * Constructs a Position from a MovingObjectPosition.
     * 
     * @param pos: A MovingObjectPosition containing block coordinates.
     */
    public Position(MovingObjectPosition pos) {
        
        this(pos.blockX, pos.blockY, pos.blockZ);
    }
    
    /**
     * Constructs a Position from an Entity.
     * 
     * @param entity: An Entity containing position coordinates.
     */
    public Position(Entity entity) {
        
        this((int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }
    
    /**
     * Constructs a Position from a NBTTagCompound.
     * 
     * @param tag: An NBTTagCompound which contains coordinate data.
     */
    public Position(NBTTagCompound tag) {
        
        this(tag.getInteger("positionX"), tag.getInteger("positionY"), tag.getInteger("positionZ"));
    }
    
    /**
     * Constructs a Position from an InputStream.
     * 
     * @param inputStream: A stream of data to read data from. The next three integers in this
     *            stream are used for the construction of the Position.
     * @throws IOException: An IOException that can be thrown for any of the various
     *             IOException causes.
     */
    public Position(DataInputStream inputStream) throws IOException {
        
        this(inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
    }
    
    /**
     * Constructs a Position from a ByteBuf. Useful for reading from a network.
     * 
     * @param buf: The ByteBuf to read the data from.
     */
    public Position(ByteBuf buf) {
        
        this(buf.readInt(), buf.readInt(), buf.readInt());
    }
    
    /**
     * Constructs a Position from basic integers.
     * 
     * @param x: The X coordinate for this Position.
     * @param y: The Y coordinate for this Position.
     * @param z: The Z coordinate for this Position.
     */
    public Position(int x, int y, int z) {
        
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Provides access to the X coordinate for this Position.
     * 
     * @return int: The X coordinate.
     */
    public int getX () {
        
        return x;
    }
    
    /**
     * Provides access to the Y coordinate for this Position.
     * 
     * @return int: The Y coordinate.
     */
    public int getY () {
        
        return y;
    }
    
    /**
     * Provides access to the Z coordinate for this Position.
     * 
     * @return int: The Z coordinate.
     */
    public int getZ () {
        
        return z;
    }
    
    /**
     * Writes the coordinates of this position to an outgoing data stream. Each coordinate is
     * written in order of X, Y and Z.
     * 
     * @param dataStream: The stream of data to write the coordinates to.
     * @throws IOException: An IOException that can be thrown for any of the various
     *             IOException causes.
     */
    public void write (DataOutputStream dataStream) throws IOException {
        
        dataStream.writeInt(x);
        dataStream.writeInt(y);
        dataStream.writeInt(z);
    }
    
    /**
     * Write the coordinates of this position to an NBTTagCompound.
     * 
     * @param tag: The NBTTagCompound to write the coordinates to.
     */
    public NBTTagCompound write (NBTTagCompound tag) {
        
        tag.setInteger("positionX", x);
        tag.setInteger("positionY", y);
        tag.setInteger("positionZ", z);
        return tag;
    }
    
    /**
     * Writes the coordinates to a ByteBuf. Useful for sending a Position through a packet.
     * 
     * @param buf: The ByteBuf to write this data to.
     */
    public void write (ByteBuf buf) {
        
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    
    /**
     * Creates a new Position which is moved one block in the specified direction.
     * 
     * @param direction: The direction to transition towards. 0:down 1:up 2:north 3:south
     *            4:east 5:west
     * @return Position: A new Position which represents the same position moved one block in
     *         the specified direction.
     */
    public Position offset (int direction) {
        
        return offset(direction, 1);
    }
    
    /**
     * Creates a new Position which is moved a specified amount of blocks, in the specified
     * direction.
     * 
     * @param direction: The direction to transition towards. 0:down 1:up 2:north 3:south
     *            4:east 5:west
     * @param amount: The amount of blocks to move.
     * @return Position: A new Position which represents the same position, moved a specified
     *         amount of blocks in the specified direction.
     */
    public Position offset (int direction, int amount) {
        
        switch (direction) {
            
            case 0:
                return translateDown(amount);
            case 1:
                return translateUp(amount);
            case 2:
                return translateNorth(amount);
            case 3:
                return translateSouth(amount);
            case 4:
                return translateWest(amount);
            case 5:
                return translateEast(amount);
            default:
                return translateDown(amount);
        }
    }
    
    /**
     * Creates a new Position which has been translated by the specified amounts along the X, Y
     * and Z axis.
     * 
     * @param x: The distance to move on the X axis.
     * @param y: The distance to move on the Y axis.
     * @param z: The distance to move on the Z axis.
     * @return Position: A new Position which represents a translation of the previous
     *         Position, translated along all axis using the specified distances.
     */
    public Position translate (int x, int y, int z) {
        
        return new Position(this.x + x, this.y + y, this.z + z);
    }
    
    /**
     * Creates a new Position which has been translated upwards by one block.
     * 
     * @return Position: A new Position that has been translated upwards by one block.
     */
    public Position translateUp () {
        
        return new Position(x, y + 1, z);
    }
    
    /**
     * Creates a new Position which has been translated upwards by a specified amount of
     * blocks.
     * 
     * @param distance: The distance to translate the position upwards.
     * @return Position: A new Position that has been translated upwards by the specified
     *         amount of blocks.
     */
    public Position translateUp (int distance) {
        
        return new Position(x, y + distance, z);
    }
    
    /**
     * Creates a new Position which has been translated downwards by one block.
     * 
     * @return Position: A new Position that has been translated downwards by one block.
     */
    public Position translateDown () {
        
        return new Position(x, y - 1, z);
    }
    
    /**
     * Creates a new Position which has been translated downwards by a specified amount of
     * blocks.
     * 
     * @param distance: The distance to translate the position downwards.
     * @return Position: A new Position that has been translated downwards by the specified
     *         amount of blocks.
     */
    public Position translateDown (int distance) {
        
        return new Position(x, y - distance, z);
    }
    
    /**
     * Creates a new Position which has been translated downwards by one block.
     * 
     * @return Position: A new Position that has been translated downwards by one block.
     */
    public Position translateNorth () {
        
        return new Position(x, y, z - 1);
    }
    
    /**
     * Creates a new Position which has been translated north by a specified amount of blocks.
     * 
     * @param distance: The distance to translate the position north.
     * @return Position: A new Position that has been translated north by the specified amount
     *         of blocks.
     */
    public Position translateNorth (int distance) {
        
        return new Position(x, y, z - distance);
    }
    
    /**
     * Creates a new Position which has been translated south by one block.
     * 
     * @return Position: A new Position that has been translated south by one block.
     */
    public Position translateSouth () {
        
        return new Position(x, y, z + 1);
    }
    
    /**
     * Creates a new Position which has been translated south by a specified amount of blocks.
     * 
     * @param distance: The distance to translate the position south.
     * @return Position: A new Position that has been translated south. by the specified amount
     *         of blocks.
     */
    public Position translateSouth (int distance) {
        
        return new Position(x, y, z + distance);
    }
    
    /**
     * Creates a new Position which has been translated west by one block.
     * 
     * @return Position: A new Position that has been translated west by one block.
     */
    public Position translateWest () {
        
        return new Position(x - 1, y, z);
    }
    
    /**
     * Creates a new Position which has been translated west by a specified amount of blocks.
     * 
     * @param distance: The distance to translate the position west.
     * @return Position: A new Position that has been translated west by the specified amount
     *         of blocks.
     */
    public Position translateWest (int distance) {
        
        return new Position(x - distance, y, z);
    }
    
    /**
     * Creates a new Position which has been translated east by one block.
     * 
     * @return Position: A new Position that has been translated east by one block.
     */
    public Position translateEast () {
        
        return new Position(x + 1, y, z);
    }
    
    /**
     * Creates a new Position which has been translated east by a specified amount of blocks.
     * 
     * @param distance: The distance to translate the position east.
     * @return Position: A new Position that has been translated east by the specified amount
     *         of blocks.
     */
    public Position translateEast (int distance) {
        
        return new Position(x + distance, y, z);
    }
    
    /**
     * Calculates the distance between this position and a second position.
     * 
     * @param pos: A second position within the world.
     * @return double: The distance between this position and the provided positon.
     */
    public double getDistance (Position pos) {
        
        return getDistance(pos.x, pos.y, pos.z);
    }
    
    /**
     * Calculates the distance between this position and a specified set of coordinates.
     * 
     * @param x: The second X coordinate.
     * @param y: The second Y coordinate.
     * @param z: The second Z coordinate.
     * @return double: The distance between this position and the provided set of coordinates.
     */
    public double getDistance (int x, int y, int z) {
        
        int distanceX = this.x - x;
        int distanceY = this.y - y;
        int distanceZ = this.z - z;
        
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
    }
    
    /**
     * Checks whether or not there is a block at this Position.
     * 
     * @param world: An instance of the World to check within.
     * @return boolean: true if there is a block, false if there is not.
     */
    public boolean isBlockAtPosition (World world) {
        
        return getBlockAtPosition(world) != null;
    }
    
    /**
     * Retrieves a Block from this Position.
     * 
     * @param world: An instance of the world to grab the Block from.
     * @return Block: The Block that is found at this Position.
     */
    public Block getBlockAtPosition (World world) {
        
        return world.getBlock(x, y, z);
    }
    
    /**
     * Sets a new Block to this Position.
     * 
     * @param world: An instance of the world to place the Block in.
     * @param block: The Block that you wish tho place at this Position.
     */
    public void setBlockAtPosition (World world, Block block) {
        
        world.setBlock(x, y, z, block);
    }
    
    /**
     * Sets the Block at this position to Air.
     * 
     * @param world: An instance of the world to replace the Block in.
     */
    public void setBlockAtPositionToAir (World world) {
        
        world.setBlockToAir(x, y, z);
    }
    
    /**
     * Retrieves the meta value of a Block at this Position.
     * 
     * @param world: An instance of the world to grab the meta from.
     * @return int: An integer which represents the meta damage of the Block at this position.
     */
    public int getMetaAtPosition (World world) {
        
        return world.getBlockMetadata(x, y, z);
    }
    
    /**
     * Sets the meta value of a Block at this Position.
     * 
     * @param world: An instance of the world to set the meta within.
     * @param meta: The desired meta value for the Block at this Position.
     */
    public void setMetaAtPosition (World world, int meta) {
        
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
    
    /**
     * Sends an EntityLivingBase to this position within the world. Useful for teleporting.
     * 
     * @param entity: The EntityLivingBase you wish to send to this position.
     */
    public void sendEntityToPosition (EntityLivingBase entity) {
        
        entity.setPositionAndUpdate(x, y, z);
    }
    
    @Override
    public Object clone () {
        
        return new Position(x, y, z);
    }
    
    @Override
    public boolean equals (Object compared) {
        
        if (!(compared instanceof Position))
            return false;
            
        Position p = (Position) compared;
        return x == p.x && y == p.y && z == p.z;
    }
    
    @Override
    public int hashCode () {
        
        return (y & 0xff) | ((x & 0x7fff) << 8) | ((z & 0x7fff) << 24) | ((x < 0) ? 0x0080000000 : 0) | ((z < 0) ? 0x0000008000 : 0);
    }
    
    @Override
    public int compareTo (Position pos) {
        
        return (y == pos.y) ? (z == pos.z) ? x - pos.x : z - pos.z : y - pos.y;
    }
    
    @Override
    public String toString () {
        
        return "X: " + x + " Y:" + y + " Z:" + z;
    }
}