package tacticulous.tira.algorithms;

import tacticulous.tira.datastructure.Node;
import tacticulous.tira.datastructure.MinHeap;
import tacticulous.game.domain.BattleMap;

/**
 *
 * Provides simple pathfinding.
 *
 * @author O
 */
public abstract class PathFind {

    /**
     *
     * Uses Dijkstra's algorithm to calculate the distances to other nodes
     * through shortests paths from starting node. Paths themselves are not
     * stored.
     *
     * @param map weights(moveCosts) for nodes are pulled from here
     * @param startX
     * @param startY
     * @param range will only check tiles within this distance from start coordinates.
     *
     * @see
     * tacticulous.tira.algorithms.GameUsage#speedRange(tacticulous.game.domain.Unit,
     * tacticulous.game.domain.BattleMap)
     *
     * speedRange
     * @return Node array with distances of shortests paths from start tile.
     */
    public static Node[][] dijkstraWithHeap(BattleMap map, int startX, int startY, int range) {
        Node[][] moveCosts = initialiseSingleSource(map.size(), startX, startY, map);
        MinHeap heap = new MinHeap(map.size());
        int fromX = Math.max(0, startX - range);
        int fromY = Math.max(0, startY - range);
        int toX = Math.min(moveCosts.length, startX + range + 1);
        int toY = Math.min(moveCosts.length, startY + range + 1);
        for (int i = fromX; i < toX; i++) {
            for (int j = fromY; j < toY; j++) {
                heap.insert(moveCosts[i][j]);
            }
        }
        while (!heap.isEmpty()) {
            Node u = heap.delMin();
            adjacents(u, moveCosts, heap);
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
    private static void adjacents(Node u, Node[][] map, MinHeap notDone) {
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
    public static boolean legit(Node[][] map, int x, int y) {
        return x >= 0 && y >= 0 && x < map.length && y < map[0].length;
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
    private static void relax(Node u, Node v, int w, MinHeap notDone) {
        if (v.getDistance() > u.getDistance() + w) {
            v.setDistance(u.getDistance() + w);
            notDone.update(v);
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
    public static Node[][] initialiseSingleSource(int size, int startX, int startY, BattleMap map) {
        Node[][] initialised = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                initialised[i][j] = new Node(Integer.MAX_VALUE/2, map.getTile(i, j).getMoveCost(), i, j);
            }
        }
        initialised[startX][startY].setDistance(0);
        return initialised;
    }



}
