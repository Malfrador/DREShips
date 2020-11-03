package de.fyreum.dreships.sign.unused.astar;

import de.fyreum.dreships.sign.unused.astar.algorithm.GraphNode;
import de.fyreum.dreships.sign.unused.WaterLocation;

public class WaterLocationNode implements GraphNode {

    private final String id;
    private final WaterLocation location;
    private final int startDistance;
    private final int endDistance;

    public WaterLocationNode(String id, WaterLocation location, int startDistance, int endDistance) {
        this.id = id;
        this.location = location;
        this.startDistance = startDistance;
        this.endDistance = endDistance;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public WaterLocation getLocation() {
        return location;
    }

    public int getStartDistance() {
        return startDistance;
    }

    public int getEndDistance() {
        return endDistance;
    }

    @Override
    public String toString() {
        return "WaterLocationNode{id= " + id + ", " + this.location.toString() + "}";
    }
}
