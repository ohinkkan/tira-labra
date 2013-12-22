/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;

/**
 *
 * @author O
 */
public abstract class GameUsage {

    /**
     * Uses Dijkstra's algorithm to calculate the distances 
     * to other nodes through shortests paths
     * from starting node. Paths themselves are not stored. 
     * Converts Node array into int array for use by the game.
     * 
     * @param unit unit's tile is the algorithm's start node
     * @param map weights are pulled from here
     * @return Weights of shortests paths from start tile. 
     * 
     * @see tacticulous.tira.algorithms.PathFind#dijkstraWithHeap(tacticulous.game.domain.BattleMap, int, int, int) 
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
}
