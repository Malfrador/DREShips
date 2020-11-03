package de.fyreum.dreships.sign.unused.astar;

import de.fyreum.dreships.sign.unused.astar.algorithm.Graph;
import de.fyreum.dreships.sign.unused.astar.algorithm.RouteFinder;
import de.fyreum.dreships.sign.unused.LocList;
import de.fyreum.dreships.sign.unused.WaterLocation;

import java.util.*;

public class WaterLocationRouteFinder {

    private final Graph<WaterLocationNode> graph;
    private final RouteFinder<WaterLocationNode> routeFinder;
    private final Set<WaterLocationNode> nodes;

    public WaterLocationRouteFinder(LocList<WaterLocation> waterLocations) {
        this.nodes = waterLocations.toNodes();
        graph = new Graph<>(nodes, getConnections());
        routeFinder = new RouteFinder<>(graph, new WaterLocationScorer(), new WaterLocationScorer());
    }

    public WaterLocationRouteFinder(Set<WaterLocationNode> nodes) {
        this.nodes = nodes;
        graph = new Graph<>(nodes, getConnections());
        routeFinder = new RouteFinder<>(graph, new WaterLocationScorer(), new WaterLocationScorer());
    }

    private Map<String, Set<String>> getConnections() {
        Map<String, Set<String>> connections = new HashMap<>();
        for (WaterLocationNode node : nodes) {
            Set<String> neighbours = new HashSet<>();
            for (WaterLocation neighbour : node.getLocation().getNeighbours()) {

                WaterLocationNode neighboursNode = nodes.stream()
                        .filter(n -> neighbour.equals(n.getLocation()))
                        .findFirst()
                        .orElse(null);

                if (neighboursNode == null) {
                    neighboursNode = new WaterLocationNode("" + nodes.size() + 1, neighbour, neighbour.getX(), neighbour.getX());
                    nodes.add(neighboursNode);
                }

                neighbours.add(neighboursNode.getId());
            }
            connections.put(node.getId(), neighbours);
        }
        return connections;
    }

    /*
     It's likely to use the other findRoute() method to avoid duplicated WaterLocationNode instances of the same WaterLocation.
     */
    public final List<WaterLocationNode> findRoute(WaterLocationNode from, WaterLocationNode to) {
        return routeFinder.findRoute(from, to);
    }

    /*
     This method is preferred to be used, because it searches for an existing Node instead of directly creating a new one.
     */
    public final List<WaterLocationNode> findRoute(WaterLocation from, WaterLocation to) {
        WaterLocationNode fromNode = nodes.stream()
                .filter(n -> from.equals(n.getLocation()))
                .findFirst()
                .orElse(null);

        if (fromNode == null) {
            fromNode = new WaterLocationNode("" + nodes.size() + 1, from, from.getX(), from.getZ());
        }

        WaterLocationNode toNode = nodes.stream()
                .filter(n -> to.equals(n.getLocation()))
                .findFirst()
                .orElse(null);

        if (toNode == null) {
            toNode = new WaterLocationNode("" + nodes.size() + 1, to, to.getX(), to.getZ());
        }
        return findRoute(fromNode, toNode);
    }

    // Getter

    public Graph<WaterLocationNode> getGraph() {
        return graph;
    }

    public RouteFinder<WaterLocationNode> getRouteFinder() {
        return routeFinder;
    }
}
