package de.fyreum.dreships.sign.unused;

import lombok.NonNull;
import org.bukkit.util.NumberConversions;

import java.util.*;

/*
WARN: This Location class can only handle coordinates from -32.768 to 32.767.
      Use the IntegerLocation to handle bigger coordinates.

Info: This class is not comparable to a normal Location.
      It's only use is to optimize the Memory usage of the WaterDetection, by breaking down the location into the functions used.
*/
public class ShortLocation implements WaterLocation {

    private final short x;
    private final short z;
    private final boolean water;
    private final Set<WaterLocation> neighbours = new HashSet<>();

    /*
    This class equals 33 bit.

    |
    |
    V

    short = 16 bit
    boolean = 1 bit
    2*16 + 1 = 33 bit

    1 Gigabyte = 8.000.000.000 bit
    8.000.000.000 / 33 = 242.424.242,4242424242424242
    1 Gigabyte = 242.424.242 SimpleLocations

    World Saragandes (erethon.de) = 17.000 * 10.000 = 170.000.000 blocks.
    170.000.000 / 242.424.242 = 0.7012500012271875
    Water locations for Saragandes = 0.70 Gigabyte.
     */
    public ShortLocation(short x, short z, boolean water) {
        this.x = x;
        this.z = z;
        this.water = water;
    }

    public ShortLocation(Map<String, Object> map) {
        this.x = (short) map.get("x");
        this.z = (short) map.get("z");
        this.water = (boolean) map.get("water");
        this.neighbours.addAll((List<WaterLocation>) map.get("neighbours"));
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
