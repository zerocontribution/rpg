package io.zerocontribution.winter.ai.pathfinding;

public class ManhattanHeuristic implements AStarHeuristic {

    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        return Math.abs(tx - x) + Math.abs(ty - y);
    }

}
