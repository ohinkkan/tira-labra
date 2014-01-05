/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

/**
 * Utility class, used by PathFind.
 * HeapIndex is used by MinHeap.update(Node)
 *
 * @author O
 *
 */
public class Node implements Comparable<Node>{

    private int distance;
    private int moveCost;
    private int x;
    private int y;
    private int heapIndex = 0;

    public int getHeapIndex() {
        return heapIndex;
    }

    public void setHeapIndex(int heapIndex) {
        this.heapIndex = heapIndex;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node(int distance, int moveCost, int x, int y) {
        this.distance = distance;
        this.moveCost = moveCost;
        this.x = x;
        this.y = y;
    }


    public int getDistance() {
        return distance;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Node t) {
       return this.distance-t.getDistance();
    }


}
