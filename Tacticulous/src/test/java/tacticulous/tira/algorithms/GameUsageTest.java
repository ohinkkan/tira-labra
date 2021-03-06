package tacticulous.tira.algorithms;

import tacticulous.tira.datastructure.Node;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;
import tacticulous.tira.datastructure.TacList;

/**
 *
 * @author O
 */
public class GameUsageTest {

    @Test
    public void speedRangeReturnWorks() {
        BattleMap map = new BattleMap(5, 0);
        Unit unit = new Unit(3, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertNotNull(GameUsage.speedRange(unit, map));
    }

    @Test
    public void speedRangeMultipleRandoms() {
        Die die = new DieRoller(5);
        for (int i = 0; i < 100000; i++) {
            BattleMap map = new BattleMap(5, die.roll());
            Unit unit = new Unit(die.roll(), 1, 1, 1, "A");
            map.getTile(die.roll() - 1, die.roll() - 1).setUnit(unit);
            GameUsage.speedRange(unit, map);
        }
    }

    @Test
    public void speedRangeReturnIsCorrect() {
        Unit unit = new Unit(2, 1, 1, 1, "A");
        int[][] intMap = new int[][]{
            {3, 4, 1, 5, 2},
            {1, 3, 4, 7, 8},
            {4, 7, 8, 9, 8},
            {1, 2, 1, 4, 6},
            {3, 4, 3, 1, 4}
        };
        int[][] expected = new int[][]{
            {11, 9, 5, 10, 12},
            {8, 7, 4, 11, 19},
            {8, 7, 0, 9, 17},
            {4, 3, 1, 5, 11},
            {7, 7, 4, 5, 9}
        };
        BattleMap map = new BattleMap(intMap);
        map.getTile(2, 2).setUnit(unit);
        int[][] result = GameUsage.speedRange(unit, map);
        assertArrayEquals(expected, result);
    }

        @Test
    public void getTilesToMoveToIsCorrect() {
        int[][] intMap = new int[][]{
            {3, 4, 1, 5, 2},
            {1, 3, 4, 7, 8},
            {4, 7, 8, 9, 8},
            {1, 2, 1, 4, 6},
            {3, 4, 3, 1, 4}
        };
        BattleMap map = new BattleMap(intMap);
        Unit unit = new Unit(7, 1, 1, 1, "A");
        map.getTile(2, 2).setUnit(unit);
        TacList<Node> result = GameUsage.getTilesToMoveTo(unit, map);
        assertEquals(12, result.size());
        unit = new Unit(4, 1, 1, 1, "A");
        map.getTile(2, 2).setUnit(unit);
        result = GameUsage.getTilesToMoveTo(unit, map);
        assertEquals(5, result.size());
    }
}
