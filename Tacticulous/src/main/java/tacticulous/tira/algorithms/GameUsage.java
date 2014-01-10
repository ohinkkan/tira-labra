/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

import tacticulous.tira.datastructure.Node;
//import java.util.ArrayList;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;
import tacticulous.tira.datastructure.TacList;

/**
 * Contains the primary methods used by non-Tira parts of the program.
 *
 * @author O
 */
public abstract class GameUsage {

    /**
     * Uses Dijkstra's algorithm to calculate the distances to other nodes
     * through shortests paths from starting node. Paths themselves are not
     * stored. Converts Node array into int array for use by the game.
     *
     * @param unit unit's tile is the algorithm's start node
     * @param map weights are pulled from here
     * @return Weights of shortests paths from start tile.
     *
     * @see
     * tacticulous.tira.algorithms.PathFind#dijkstraWithHeap(tacticulous.game.domain.BattleMap,
     * int, int, int)
     *
     * dijkstraWithHeap
     */
    public static int[][] speedRange(Unit unit, BattleMap map) {
        Node[][] moveCosts = PathFind.dijkstraWithHeap(map, unit.getX(), unit.getY(), unit.getSpeed());
        int[][] costs = new int[map.size()][map.size()];
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.size(); j++) {
                costs[i][j] = moveCosts[i][j].getDistance();
            }
        }
        return costs;
    }

    public static TacList<Node> getTilesToMoveTo(Unit unit, BattleMap map) {
        Node[][] moveCosts = PathFind.dijkstraWithHeap(map, unit.getX(), unit.getY(), unit.getSpeed());
        int fromX = Math.max(0, unit.getX() - unit.getSpeed());
        int fromY = Math.max(0, unit.getY() - unit.getSpeed());
        int toX = Math.min(moveCosts.length, unit.getX() + unit.getSpeed() + 1);
        int toY = Math.min(moveCosts.length, unit.getY() + unit.getSpeed() + 1);
        TacList<Node> tiles = new TacList((unit.getSpeed() * 2 + 1) * (unit.getSpeed() * 2 + 1));
        for (int i = fromX; i < toX; i++) {
            for (int j = fromY; j < toY; j++) {
                if (moveCosts[i][j].getDistance() <= unit.getSpeed() && moveCosts[i][j].getDistance() > 0) {
                    tiles.add(moveCosts[i][j]);
                }
            }
        }
        return tiles;
    }
}
