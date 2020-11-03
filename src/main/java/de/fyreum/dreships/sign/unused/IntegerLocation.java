package de.fyreum.dreships.sign.unused;

import lombok.NonNull;
import org.bukkit.util.NumberConversions;

import java.util.*;

/*
Info: This class is not comparable to a normal Location.
      It's only use is to optimize the Memory usage of the WaterDetection, by breaking down the location into the functions used.

      Mainly this is used as a fallback class for the ShortLocation.
 */
public class IntegerLocation implements WaterLocation {

    private final int x;
    private final int z;
    private final boolean water;
    private final Set<WaterLocation> neighbours = new HashSet<>();

    /*
    This class equals 65 bit.

    |
    |
    V

    int = 32 bit
    boolean = 1 bit
    2*32 + 1 = 65 bit

    1 Gigabyte = 8.000.000.000 bit
    8.000.000.000 / 65 = 123.076.923,0769230769230769
    1 Gigabyte = 123.076.923 SimpleLocations

    World Saragandes (erethon.de) = 17.000 * 10.000 = 170.000.000 blocks.
    170.000.000 / 123.076.923 = 1,3812500008632813
    Water locations for Saragandes = 1,38 Gigabyte.
     */
    public IntegerLocation(int x, int z, boolean water) {
        this.x = x;
        this.z = z;
        this.water = water;
    }

    // Getter and setter

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public boolean isWater() {
        return water;
    }

    @Override
    public Set<WaterLocation> getNeighbours() {
        return this.neighbours;
    }

    @Override
    public void addNeighbours(@NonNull WaterLocation... neighbours) {
        this.neighbours.addAll(Arrays.asList(neighbours));
    }

    // Util

    @Override
    public int distance(@NonNull WaterLocation o) {
        return (int) Math.ceil(Math.sqrt(distanceSquared(o)));
    }

    @Override
    public int distanceSquared(@NonNull WaterLocation other) {
        return (int) Math.ceil(NumberConversions.square(x - other.getX()) + NumberConversions.square(z - other.getZ()));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WaterLocation)) {
            return false;
        }
        WaterLocation otherLoc = (WaterLocation) other;
        return this.getX() == otherLoc.getX() && this.getZ() == otherLoc.getZ() && this.isWater() == otherLoc.isWater();
    }

    @Override
    public int compareTo(@NonNull WaterLocation other) {
        return (getX() + getZ()) - (other.getX() + other.getZ());
    }

    @Override
    public String toString() {
        return "WaterLocation[" + this.x + ", " + this.z + ", Water=" + this.water + "]";
    }
}
