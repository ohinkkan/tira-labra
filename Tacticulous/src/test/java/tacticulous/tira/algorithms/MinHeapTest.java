/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author O
 */
public class MinHeapTest {

    private MinHeap heap;
    private Node node;

    @Test
    public void newHeapEmpty() {
        heap = new MinHeap(10);
        assertTrue(heap.isEmpty());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void heapNotTooBig() {
        heap = new MinHeap(3);
        node = new Node(1, 1, 1, 1);
        for (int i = 0; i < 10; i++) {
            heap.insert(node);
        }
    }

    @Test
    public void heapNotTooSmall() {
        heap = new MinHeap(3);
        node = new Node(1, 1, 1, 1);
        for (int i = 0; i < 9; i++) {
            heap.insert(node);
        }
    }

    @Test
    public void heapInsertAndDelWorksEasyAndEndsEmpty() {
        heap = new MinHeap(3);
        node = new Node(1, 1, 1, 1);
        Node node2 = new Node(2, 1, 1, 1);
        Node node3 = new Node(3, 1, 1, 1);
        heap.insert(node);
        heap.insert(node2);
        heap.insert(node3);
        assertTrue(!heap.isEmpty());
        assertEquals(1, heap.delMin().getDistance());
        assertEquals(2, heap.delMin().getDistance());
        assertEquals(3, heap.delMin().getDistance());
        assertTrue(heap.isEmpty());
    }

    @Test
    public void heapInsertAndDelWorksHarder() {
        heap = new MinHeap(3);
        node = new Node(5, 1, 1, 1);
        Node node2 = new Node(2, 1, 1, 1);
        Node node3 = new Node(7, 1, 1, 1);
        Node node4 = new Node(2, 1, 1, 1);
        Node node5 = new Node(1, 1, 1, 1);
        heap.insert(node);
        heap.insert(node2);
        heap.insert(node3);
        heap.insert(node4);
        heap.insert(node5);
        assertEquals(1, heap.delMin().getDistance());
        assertEquals(2, heap.delMin().getDistance());
        assertEquals(2, heap.delMin().getDistance());
        assertEquals(5, heap.delMin().getDistance());
        assertEquals(7, heap.delMin().getDistance());
    }

    @Test
    public void heapUpdateWorks() {
        heap = new MinHeap(3);
        node = new Node(5, 1, 1, 1);
        Node node2 = new Node(2, 1, 1, 1);
        Node node3 = new Node(7, 1, 1, 1);
        Node node4 = new Node(2, 1, 1, 1);
        Node node5 = new Node(1, 1, 1, 1);
        heap.insert(node);
        heap.insert(node2);
        heap.insert(node3);
        heap.insert(node4);
        heap.insert(node5);
        node3.setDistance(4);
        heap.update(node3);
        node.setDistance(3);
        heap.update(node);
        assertEquals(1, heap.delMin().getDistance());
        assertEquals(2, heap.delMin().getDistance());
        assertEquals(2, heap.delMin().getDistance());
        assertEquals(3, heap.delMin().getDistance());
        assertEquals(4, heap.delMin().getDistance());
    }

}
