package tacticulous.tira.testing;

import java.util.ArrayList;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;
import tacticulous.tira.algorithms.GameUsage;
import tacticulous.tira.algorithms.PathFind;
import tacticulous.tira.datastructure.MinHeap;
import tacticulous.tira.datastructure.Node;
import tacticulous.tira.datastructure.TacList;

/**
 *
 * @author O
 */
public class Performance {

    public static long speedRangeMultipleRandomsFixedSize(boolean mine, int size, int howManyTimes) {
        long startTime = System.currentTimeMillis();
        Die die = new DieRoller(5);
        for (int i = 0; i < howManyTimes; i++) {
            Die tempDie = new DieRoller(size);
            BattleMap map = new BattleMap(size, die.roll());
            if (mine) {
                PathFind.dijkstraWithHeap(map, tempDie.roll() - 1, tempDie.roll() - 1, size);
            } else {
                AltPathFind.dijkstraWithPriorityQueue(map, tempDie.roll() - 1, tempDie.roll() - 1, size);
            }
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    public static long speedRangeMultipleRandoms(boolean mine, int maxSize, int howManyTimes) {
        long startTime = System.currentTimeMillis();
        Die die = new DieRoller(maxSize - 2);
        for (int i = 0; i < howManyTimes; i++) {
            int size = die.roll() + 2;
            Die tempDie = new DieRoller(size);
            BattleMap map = new BattleMap(size, die.roll());
            if (mine) {
                PathFind.dijkstraWithHeap(map, tempDie.roll() - 1, tempDie.roll() - 1, maxSize);
            } else {
                AltPathFind.dijkstraWithPriorityQueue(map, tempDie.roll() - 1, tempDie.roll() - 1, maxSize);
            }
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    public static void compareFixed(int maxSize, int howManyTimes) {
        System.out.println("Map size:" + maxSize + ", repeats:" + howManyTimes);
        System.out.println("Heap:" + Performance.speedRangeMultipleRandomsFixedSize(true, maxSize, howManyTimes) + "ms");
        System.out.println("PriorityQueue:" + Performance.speedRangeMultipleRandomsFixedSize(false, maxSize, howManyTimes) + "ms");
    }

    public static void compare(int maxSize, int howManyTimes) {
        System.out.println("Max size:" + maxSize + ", repeats:" + howManyTimes);
        System.out.println("Heap:" + Performance.speedRangeMultipleRandoms(true, maxSize, howManyTimes) + "ms");
        System.out.println("PriorityQueue:" + Performance.speedRangeMultipleRandoms(false, maxSize, howManyTimes) + "ms");
    }

    public static long compareLists(int size, int remove, boolean mine, int repeats) {
        long sum = 0;
        for (int j = 0; j < repeats; j++) {
            long startTime = System.currentTimeMillis();
            Die die = new DieRoller(1000000);
            if (mine) {
                TacList<Integer> list = new TacList(2);
                for (int i = 0; i < size; i++) {
                    list.add(die.roll() - 500000);
                }
                for (int i = 0; i < remove; i++) {
                    list.remove(list.get(0));
                }
            } else {
                ArrayList<Integer> list = new ArrayList(2);
                for (int i = 0; i < size; i++) {
                    list.add(die.roll() - 500000);
                }
                for (int i = 0; i < remove; i++) {
                    list.remove(list.get(0));
                }
            }
            long stopTime = System.currentTimeMillis();
            sum = sum + (stopTime - startTime);
            System.gc();
        }
        return sum / repeats;
    }

    public static long compareListsSizeRight(int size, int remove, boolean mine, int repeats) {
        long sum = 0;
        {
            long startTime = System.currentTimeMillis();
            Die die = new DieRoller(1000000);
            if (mine) {
                TacList<Integer> list = new TacList(size);
                for (int i = 0; i < size; i++) {
                    list.add(die.roll() - 500000);
                }
                for (int i = 0; i < remove; i++) {
                    list.remove(list.get(0));
                }
            } else {
                ArrayList<Integer> list = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    list.add(die.roll() - 500000);
                }
                for (int i = 0; i < remove; i++) {
                    list.remove(list.get(0));
                }
            }
            long stopTime = System.currentTimeMillis();
            sum = sum + (stopTime - startTime);
            System.gc();
        }
        return sum / repeats;
    }

    public static void heapComparisons() {
        compare(5, 100000);
        compareFixed(10, 10000);
        compare(10, 10000);
        compareFixed(100, 1000);
        compare(100, 1000);
        compareFixed(500, 1);
    }

    public static void listComparisons() {

        int repeats = 20;
        System.out.println("TacList, wrong size, add 1000000 no remove: "
                + compareLists(1000000, 0, true, 1) + "ms");
        System.out.println("ArrayList, wrong size, add 1000000 no remove: "
                + compareLists(1000000, 0, true, 1) + "ms");
        System.out.println("TacList, wrong size, add 100000 remove 90000: "
                + compareLists(100000, 90000, true, 1) + "ms");
        System.out.println("ArrayList, wrong size, add 100000 remove 90000: "
                + compareLists(100000, 90000, true, 1) + "ms");
        System.out.println("TacList, right size, add 1000000 no remove: "
                + compareListsSizeRight(1000000, 0, true, 1) + "ms");
        System.out.println("ArrayList, right size, add 1000000 no remove: "
                + compareListsSizeRight(1000000, 0, true, 1) + "ms");
        System.out.println("TacList, right size, add 100000 remove 90000: "
                + compareListsSizeRight(100000, 90000, true, 1) + "ms");
        System.out.println("ArrayList, right size, add 100000 remove 90000: "
                + compareListsSizeRight(100000, 90000, true, 1) + "ms");
        System.out.println("");
        System.out.println("TacList, wrong size, add 10000 remove 1000: "
                + compareLists(10000, 1000, true, repeats) + "ms average");
        System.out.println("TacList, wrong size, add 100000 remove 1000: "
                + compareLists(100000, 1000, true, repeats) + "ms average");
        System.out.println("TacList, wrong size, add 1000000 remove 1000: "
                + compareLists(1000000, 1000, true, repeats) + "ms average");
        System.gc();
        System.out.println("TacList, wrong size, add 10000000 remove 1000: "
                + compareLists(10000000, 1000, true, repeats/5) + "ms average");
        System.gc();
        System.out.println("ArrayList, wrong size, add 10000 remove 1000: "
                + compareLists(10000, 1000, false, repeats) + "ms average");
        System.out.println("ArrayList, wrong size, add 100000 remove 1000: "
                + compareLists(100000, 1000, false, repeats) + "ms average");
        System.out.println("ArrayList, wrong size, add 1000000 remove 1000: "
                + compareLists(1000000, 1000, false, repeats) + "ms average");
        System.gc();
        System.out.println("ArrayList, wrong size, add 10000000 remove 1000: "
                + compareLists(10000000, 1000, false, repeats/5) + "ms average");
        compareLists(1000000, 10, false, 1);
        heapTesting();
    }

    public static void heapTesting() {

        MinHeap heap0 = new MinHeap(30);
        MinHeap heap = new MinHeap(100);
        MinHeap heap2 = new MinHeap(300);
        MinHeap heap3 = new MinHeap(1000);
        MinHeap heap4 = new MinHeap(3000);

        MinHeap heap12 = new MinHeap(200);
        MinHeap heap22 = new MinHeap(500);
        MinHeap heap24 = new MinHeap(700);

        System.out.println("Heap insert 900: " + heapInsert(heap0, 900) + "ms");
        System.out.println("Heap insert 10000: " + heapInsert(heap, 10000) + "ms");
        System.out.println("Heap insert 40000: " + heapInsert(heap12, 40000) + "ms");
        System.out.println("Heap insert 90000: " + heapInsert(heap2, 90000) + "ms");
        System.out.println("Heap insert 250000: " + heapInsert(heap22, 250000) + "ms");
        System.out.println("Heap insert 490000: " + heapInsert(heap24, 490000) + "ms");
        System.out.println("Heap insert 1000000: " + heapInsert(heap3, 1000000) + "ms");
        System.out.println("Heap insert 9000000: " + heapInsert(heap4, 9000000) + "ms");
        System.out.println("");
        System.out.println("Heap delMin 900: " + heapDelMin(heap0, 900) + "ms");
        System.out.println("Heap delMin 10000: " + heapDelMin(heap, 10000) + "ms");
        System.out.println("Heap delMin 40000: " + heapDelMin(heap12, 40000) + "ms");
        System.out.println("Heap delMin 90000: " + heapDelMin(heap2, 90000) + "ms");
        System.out.println("Heap delMin 250000: " + heapDelMin(heap22, 250000) + "ms");
        System.out.println("Heap delMin 490000: " + heapDelMin(heap24, 490000) + "ms");
        System.out.println("Heap delMin 1000000: " + heapDelMin(heap3, 1000000) + "ms");
        System.out.println("Heap delMin 9000000: " + heapDelMin(heap4, 9000000) + "ms");
    }

    private static long heapInsert(MinHeap heap, int howMany) {
        Die die = new DieRoller(100000000);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < howMany; i++) {
            heap.insert(new Node(die.roll(), 0, 0, 0));
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    private static long heapDelMin(MinHeap heap, int howMany) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < howMany; i++) {
            heap.delMin();
        }
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
