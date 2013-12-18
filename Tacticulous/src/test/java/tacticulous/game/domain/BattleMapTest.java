/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
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
        die = new FixedDie(5);
        map = new BattleMap(0, die);
    }

    @Test
    public void unitPlacingWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        map.getTile(0, 0).setUnit(unit);
        assertEquals(unit, map.getTile(0, 0).getUnit());
    }

    @Test
    public void legalMoveWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        map.getTile(0, 0).setUnit(unit);
        map.move(0, 0, 5, 5);
        assertEquals(null, map.getTile(0, 0).getUnit());
        assertEquals(unit, map.getTile(5, 5).getUnit());
    }

    @Test
    public void illegalMoveDoesNotWork() {
        die = new FixedDie(5);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        map.getTile(0, 0).setUnit(unit);
        map.move(0, 0, 11, 5);
        assertEquals(unit, map.getTile(0, 0).getUnit());
        assertEquals(null, map.getTile(5, 5).getUnit());
    }

    @Test
    public void lethalAttackRemovesTarget() {
        die = new FixedDie(5);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 1, 'B');
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        map.attack(0, 0, 1, 1);
        assertEquals(null, map.getTile(1, 1).getUnit());
    }

    @Test
    public void missingAttackDoesNotRemoveTarget() {
        die = new FixedDie(-2);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 1, 'B');
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        map.attack(0, 0, 1, 1);
        assertEquals(unit2, map.getTile(1, 1).getUnit());
    }

    @Test
    public void nonLethalAttackDoesNotRemoveTarget() {
        die = new FixedDie(2);
        map = new BattleMap(10, die);
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 5, 'B');
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        map.attack(0, 0, 1, 1);
        assertEquals(unit2, map.getTile(1, 1).getUnit());
    }
}