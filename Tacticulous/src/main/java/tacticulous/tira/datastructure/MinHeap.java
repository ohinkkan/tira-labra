package tacticulous.tira.datastructure;


/**
 * Minimum heap data structure. Stores nodes, using distance as key.
 *
 * @author O
 */
public class MinHeap {

    private int heapSize;
    private Node[] heap;

    /**
     * Checks if heap is empty.
     *
     * @return true if heapSize = 0
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     *
     * @return
     */
    public int size() {
        return heapSize;
    }

    /**
     * Creates a new heap just large enough to contain all nodes from a map
     *
     * @param mapSize
     */
    public MinHeap(int mapSize) {
        heap = new Node[mapSize * mapSize + 1];
        heapSize = 0;
    }

    /**
     * Returns the parent of a node
     *
     * @param i
     * @return
     */
    private int parent(int i) {
        return i / 2;
    }

    /**
     * Returns the left child of a node
     *
     * @param i
     * @return
     */
    private int left(int i) {
        return 2 * i;
    }

    /**
     * Returns the right child of a node
     *
     * @param i
     * @return
     */
    private int right(int i) {
        return 2 * i + 1;
    }

    /**
     * Makes sure the heap condition is maintained at index i
     *
     * @param i
     */
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

    /**
     * swaps the locations of two nodes; support method, does not heapify!
     *
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        heap[i].setHeapIndex(j);
        heap[j].setHeapIndex(i);
        Node aid = heap[i];
        heap[i] = heap[j];
        heap[j] = aid;
    }

    /**
     * returns the minimum distance node and removes it from heap
     *
     * @return
     */
    public Node delMin() {
        Node min = heap[1];
        heap[1] = heap[heapSize];
        heap[1].setHeapIndex(1);
        heapSize--;
        heapify(1);
        return min;
    }

    /**
     * inserts node into the heap. does not check if there is room in the table!
     *
     * @param node
     */
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

    /**
     * updates the location of a node if its distance has decreased.
     * does not work if distance has increased!
     *
     * @param u
     */
    public void update(Node u) {
        int i = u.getHeapIndex();
        while (i > 1 && heap[parent(i)].getDistance() > heap[i].getDistance()) {
            swap(i, parent(i));
            i = parent(i);
        }
    }
}
