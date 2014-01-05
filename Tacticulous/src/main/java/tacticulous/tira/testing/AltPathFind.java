/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tacticulous.tira.testing;

import java.util.PriorityQueue;
import tacticulous.game.domain.BattleMap;
import tacticulous.tira.algorithms.Node;
import tacticulous.tira.algorithms.PathFind;

/**
 *
 * @author O
 */
public class AltPathFind {
/**
     * For efficiency testing
     */
    public static Node[][] dijkstraWithPriorityQueue(BattleMap map, int startX, int startY, int range) {
        Node[][] moveCosts = PathFind.initialiseSingleSource(map.size(), startX, startY, map);
        PriorityQueue<Node> heapPlaceholder = new PriorityQueue();

        for (int i = 0; i < moveCosts.length; i++) {
            for (int j = 0; j < moveCosts.length; j++) {
                heapPlaceholder.add(moveCosts[i][j]);

            }
        }
        while (!heapPlaceholder.isEmpty()) {
            Node u = heapPlaceholder.poll();
            adjacentsWithPriorityQueue(u, moveCosts, heapPlaceholder);
        }

        return moveCosts;
    }

    /**
     * For efficiency testing
     */
    private static void adjacentsWithPriorityQueue(Node u, Node[][] map, PriorityQueue<Node> notDone) {
        Node v;
        if (PathFind.legit(map, u.getX() - 1, u.getY())) {
            v = map[u.getX() - 1][u.getY()];
            relaxWithPriorityQueue(u, v, v.getMoveCost(), notDone);
        }
        if (PathFind.legit(map, u.getX() + 1, u.getY())) {
            v = map[u.getX() + 1][u.getY()];
            relaxWithPriorityQueue(u, v, v.getMoveCost(), notDone);
        }
        if (PathFind.legit(map, u.getX(), u.getY() - 1)) {
            v = map[u.getX()][u.getY() - 1];
            relaxWithPriorityQueue(u, v, v.getMoveCost(), notDone);
        }
        if (PathFind.legit(map, u.getX(), u.getY() + 1)) {
            v = map[u.getX()][u.getY() + 1];
            relaxWithPriorityQueue(u, v, v.getMoveCost(), notDone);
        }
    }

    /**
     * For efficiency testing
     */
    private static void relaxWithPriorityQueue(Node u, Node v, int w, PriorityQueue<Node> notDone) {
        if (v.getDistance() > u.getDistance() + w) {
            v.setDistance(u.getDistance() + w);
            notDone.remove(v);
            notDone.add(v);

        }
    }
}
