package de.fyreum.dreships.sign.unused.astar.algorithm;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
