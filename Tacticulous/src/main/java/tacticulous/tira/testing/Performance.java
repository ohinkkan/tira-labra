package tacticulous.tira.testing;

import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;
import tacticulous.tira.algorithms.GameUsage;
import tacticulous.tira.algorithms.PathFind;

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
        System.out.println("Map size:" + maxSize + "Repeats:" + howManyTimes);
        System.out.println("Heap:" + Performance.speedRangeMultipleRandomsFixedSize(true, maxSize, howManyTimes) + "ms");
        System.out.println("PriorityQueue:" + Performance.speedRangeMultipleRandomsFixedSize(false, maxSize, howManyTimes) + "ms");
    }

    public static void compare(int maxSize, int howManyTimes) {
        System.out.println("Max size:" + maxSize + "Repeats:" + howManyTimes);
        System.out.println("Heap:" + Performance.speedRangeMultipleRandoms(true, maxSize, howManyTimes) + "ms");
        System.out.println("PriorityQueue:" + Performance.speedRangeMultipleRandoms(false, maxSize, howManyTimes) + "ms");
    }

    public static void comparisons() {
        compare(5,100000);
        compareFixed(10, 10000);
        compare(10, 10000);
        compareFixed(100, 1000);
        compare(100, 1000);
        compareFixed(1000, 1);
    }
}
