/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

import java.util.PriorityQueue;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;

/**
 *
 * @author O
 */
public abstract class PathFind {

    /**
     *
     * Uses Dijkstra's algorithm to calculate the distances 
     * to other nodes through shortests paths
     * from starting node. Paths themselves are not stored. 
     *
     * @param map weights(moveCosts) for nodes are pulled from here
     * @param startX
     * @param startY
     * @param range UNUSED. Will eventually be used for performance
     * optimization.
     *
     * @see
     * tacticulous.tira.algorithms.GameUsage#speedRange(tacticulous.game.domain.Unit,
     * tacticulous.game.domain.BattleMap)
     *
     * speedRange
     * @return Node array with distances of shortests paths from start
     * tile.
     */
    public static Node[][] dijkstraWithHeap(BattleMap map, int startX, int startY, int range) {
        Node[][] moveCosts = initialiseSingleSource(map.size(), startX, startY, map);
        PriorityQueue<Node> heapPlaceholder = new PriorityQueue();
        for (int i = 0; i < moveCosts.length; i++) {
            for (int j = 0; j < moveCosts.length; j++) {
                heapPlaceholder.add(moveCosts[i][j]);
            }
        }
        while (!heapPlaceholder.isEmpty()) {
            Node u = heapPlaceholder.poll();
            adjacents(u, moveCosts, heapPlaceholder);
        }
        return moveCosts;
    }

    /**
     * Checks which nodes adjacent to node u are valid (= within map borders)
     * and relaxes them.
     * 
     * @param u
     * @param map
     * @param notDone nodes(tiles) which have not been examined yet.
     */
    private static void adjacents(Node u, Node[][] map, PriorityQueue<Node> notDone) {
        Node v;
        if (legit(map, u.getX() - 1, u.getY())) {
            v = map[u.getX() - 1][u.getY()];
            relax(u, v, v.getMoveCost(), notDone);
        }
        if (legit(map, u.getX() + 1, u.getY())) {
            v = map[u.getX() + 1][u.getY()];
            relax(u, v, v.getMoveCost(), notDone);
        }
        if (legit(map, u.getX(), u.getY() - 1)) {
            v = map[u.getX()][u.getY() - 1];
            relax(u, v, v.getMoveCost(), notDone);
        }
        if (legit(map, u.getX(), u.getY() + 1)) {
            v = map[u.getX()][u.getY() + 1];
            relax(u, v, v.getMoveCost(), notDone);
        }
    }

    /**
     * Checks if given coordinates are within array.
     * 
     * @param map
     * @param x
     * @param y
     * @return True if within.
     */
    private static boolean legit(Node[][] map, int x, int y) {
        if (x >= 0 && y >= 0 && x < map.length && y < map[0].length) {
            return true;
        }
        return false;
    }

    /**
     * Standard relax used by shortest path algorithms, except that edge weight
     * is target node's weight(moveCost).
     * 
     * @param u from-node
     * @param v to-node
     * @param w weight of edge, that is, moveCost of to-node v.
     * @param notDone nodes(tiles) which have not been examined yet.
     */
    private static void relax(Node u, Node v, int w, PriorityQueue<Node> notDone) {
        if (v.getDistance() > u.getDistance() + w) {
            v.setDistance(u.getDistance() + w);
            notDone.remove(v);
            notDone.add(v);
        }
    }

    /**
     * Initializes and constructs the node map used by dijkstraWithHeap.
     * 
     * @param size
     * @param startX
     * @param startY
     * @param map battlemap
     * @return Node array, all distances are 1000 except for starting node, 
     * weights(moveCosts) are pulled from battlemap.
     */
    private static Node[][] initialiseSingleSource(int size, int startX, int startY, BattleMap map) {
        Node[][] initialised = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                initialised[i][j] = new Node(1000, map.getTile(i, j).getMoveCost(), i, j);
            }
        }
        initialised[startX][startY].setDistance(0);
        return initialised;
    }
}
