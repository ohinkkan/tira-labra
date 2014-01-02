
package tacticulous.game.domain;


import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.FixedDie;

/**
 *
 * @author O
 */
public class BattleMapTest {

    BattleMap map;
    Tile tile;
    Unit unit;
    Die die;

    @Test(expected = IllegalArgumentException.class)
    public void legalSizeForMap() {
        map = new BattleMap(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void legalRandomTerrainForMap() {
        die = new FixedDie(5);
        map = new BattleMap(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void legalFixedTerrainForMap() {
        int[][] intMap = new int[][]{
            {3, 4, 1, 5, 2},
            {1, 3, 4, 7, 8},
            {4, 7, 8, 9, 8},
            {1, 2, 1, 4, 6},
            {3, 4, 3, 1}
        };
        die = new FixedDie(5);
        map = new BattleMap(intMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void legalFixedTerrainForMap2() {
        int[][] intMap = new int[][]{
            {3, 4, 1, 5, 2},
            {1, 3, 4, 7, 8},
            {4, 7, 8, 9, 8},
            {1, 2, 1, 4, 0},
            {3, 4, 3, 1, 4}
        };
        die = new FixedDie(5);
        map = new BattleMap(intMap);
    }

    @Test
    public void unitPlacingWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertEquals(unit, map.getTile(0, 0).getUnit());
    }

    @Test
    public void legitCheckWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, 1);
        assertTrue(map.legit(0, 5, 9) && !map.legit(-1) && !map.legit(10));

    }
}
