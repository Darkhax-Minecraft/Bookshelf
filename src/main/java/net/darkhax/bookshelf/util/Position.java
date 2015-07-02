package net.darkhax.bookshelf.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;

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
     * @param nbt: A NBTTagCompound containing the required information to create a
     *            Position instance.
     */
    public Position(NBTTagCompound nbt) {
    
        this(nbt.getInteger("positionX"), nbt.getInteger("positionY"), nbt.getInteger("positionZ"));
    }
    
    /**
     * Creates an instance of a Position using an entity. Positions are grabbed from the
     * entity object provided.
     * 
     * @param entity: Instance of any minecraft entity.
     */
    public Position(Entity entity) {
    
        this((int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }
    
    /**
     * Creates an instance of a Position using three integer coordinates. Should be used if
     * you don't care about precision.
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
     * @return Position: A new Position instance, which has been translated north by
     *         the specified distance.
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
    
        return String.format("X:% Y:% Z:%", this.positionX, this.positionY, this.positionZ);
    }
}