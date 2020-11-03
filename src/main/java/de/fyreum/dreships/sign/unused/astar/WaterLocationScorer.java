package de.fyreum.dreships.sign.unused.astar;

import de.fyreum.dreships.sign.unused.astar.algorithm.Scorer;

public class WaterLocationScorer implements Scorer<WaterLocationNode> {
    @Override
    public double computeCost(WaterLocationNode from, WaterLocationNode to) {
        return Double.compare(from.getStartDistance() + from.getEndDistance(), to.getStartDistance() + to.getEndDistance());
    }
}
