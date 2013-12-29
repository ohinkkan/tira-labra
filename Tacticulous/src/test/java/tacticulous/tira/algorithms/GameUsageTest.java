/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.algorithms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;
import tacticulous.game.utility.FixedDie;

/**
 *
 * @author O
 */
public class GameUsageTest {

    @Test
    public void speedRangeReturnWorks() {
        Die die = new FixedDie(5);
        BattleMap map = new BattleMap(5, die, 0);
        Unit unit = new Unit(3, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertNotNull(GameUsage.speedRange(unit, map));
    }

    @Test
    public void testMultipleRandoms() {
        Die die = new DieRoller(5);
        for (int i = 0; i < 100000; i++) {
            BattleMap map = new BattleMap(5, die, die.roll());
            Unit unit = new Unit(die.roll(), 1, 1, 1, "A");
            map.getTile(die.roll(), die.roll()).setUnit(unit);
            GameUsage.speedRange(unit, map);
        }
    }

    @Test
    public void returnIsCorrect() {
        Die die = new FixedDie(5);
        Unit unit = new Unit(3, 1, 1, 1, "A");

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
        BattleMap map = new BattleMap(intMap, die);
        map.getTile(2, 2).setUnit(unit);
        int[][] result = GameUsage.speedRange(unit, map);
        assertArrayEquals(expected, result);
    }
}
