package de.fyreum.dreships.sign.unused;

import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public interface WaterLocation extends Comparable<WaterLocation>, ConfigurationSerializable {

    boolean isWater();

    int getX();

    int getZ();

    int distance(@NonNull WaterLocation other);

    int distanceSquared(@NonNull WaterLocation other);

    Set<WaterLocation> getNeighbours();

    void addNeighbours(@NonNull WaterLocation... neighbours);

    String toString();

    @Override
    default @NonNull Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("x", getX());
        serialized.put("z", getZ());
        serialized.put("water", isWater());
        List<WaterLocation> serializedNeighbours = new ArrayList<>(getNeighbours());
        serialized.put("neighbours", serializedNeighbours);
        return serialized;
    }
}
