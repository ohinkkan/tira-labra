package tacticulous.tira.algorithms;

/**
 * Minimum heap.
 * 
 * @author O
 */
public class MinHeap {

    private int heapSize;
    private Node[] heap;

    public boolean isEmpty() {
        return heapSize == 0;
    }
    
    public MinHeap(int mapSize) {
        heap = new Node[mapSize * mapSize + 1];
        heapSize = 0;
    }

    private int parent(int i) {
        return i / 2;
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return 2 * i + 1;
    }

    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest;
        if (r <= this.heapSize) {
            if (heap[l].getDistance() < heap[r].getDistance()) {
                smallest = l;
            } else {
                smallest = r;
            }
            if (heap[i].getDistance() > heap[smallest].getDistance()) {
                swap(i, smallest);
                this.heapify(smallest);
            }
        } else if (l == this.heapSize) {
            if (heap[i].getDistance() > heap[l].getDistance()) {
                swap(i, l);
            }
        }
    }

    private void swap(int i, int j) {
        heap[i].setHeapIndex(j);
        heap[j].setHeapIndex(i);
        Node aid = heap[i];
        heap[i] = heap[j];
        heap[j] = aid;
    }

    public Node delMin() {
        Node min = heap[1];
        heap[1] = heap[heapSize];
        heap[1].setHeapIndex(1);
        heapSize--;
        heapify(1);
        return min;
    }

    public void insert(Node node) {
        heapSize++;
        int i = heapSize;
        while (i > 1 && heap[parent(i)].getDistance() > node.getDistance()) {
            heap[i] = heap[parent(i)];
            heap[i].setHeapIndex(i);
            i = parent(i);
        }
        heap[i] = node;
        node.setHeapIndex(i);
    }

    public void update(Node u) {
        int i = u.getHeapIndex();
        while (i > 1 && heap[parent(i)].getDistance() > heap[i].getDistance()) {
            swap(i, parent(i));
            i = parent(i);
        }
    }
}
