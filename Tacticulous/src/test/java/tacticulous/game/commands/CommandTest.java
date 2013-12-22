/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.commands;

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
public class CommandTest {

    BattleMap map;
    Tile tile;
    Unit unit;
    Die die;

    @Test
    public void legalMoveWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        Command.move(map, 0, 0, 5, 5);
        assertEquals(null, map.getTile(0, 0).getUnit());
        assertEquals(unit, map.getTile(5, 5).getUnit());
    }

    @Test
    public void illegalMoveDoesNotWork() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        Command.move(map, 0, 0, 11, 5);
        assertEquals(unit, map.getTile(0, 0).getUnit());
        assertEquals(null, map.getTile(5, 5).getUnit());
    }

    @Test
    public void CannotMoveIfTargetTileOccupied() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        assertEquals(false, Command.move(map, 0, 0, 1, 1));
    }

    @Test
    public void illegalAttackDoesNotWork() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertEquals(false, Command.attack(map, 0, 0, 1, 11));
    }

    @Test
    public void mustHaveTarget() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertEquals(false, Command.attack(map, 0, 0, 1, 1));
    }

    @Test
    public void hitRemovesHealth() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "A");
        Command.attackRoll(unit, unit2, 1);
        assertEquals(0, unit2.getHitPoints());
    }

    @Test
    public void missDoesNotRemoveHealth() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        Command.attackRoll(unit, unit2, -1);
        assertEquals(1, unit2.getHitPoints());
    }

    @Test
    public void unitDiesAtZeroHitpoints() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        assertEquals(1, Command.attackRoll(unit, unit2, 1));
    }

    @Test
    public void lethalAttackRemovesTarget() {
        die = new FixedDie(5);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        Command.attack(map, 0, 0, 1, 1);
        assertEquals(null, map.getTile(1, 1).getUnit());
    }

    @Test
    public void missingAttackDoesNotRemoveTarget() {
        die = new FixedDie(-2);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        Command.attack(map, 0, 0, 1, 1);
        assertEquals(unit2, map.getTile(1, 1).getUnit());
    }

    @Test
    public void nonLethalAttackDoesNotRemoveTarget() {
        die = new FixedDie(2);
        map = new BattleMap(10, die, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 5, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        Command.attack(map, 0, 0, 1, 1);
        assertEquals(unit2, map.getTile(1, 1).getUnit());
    }
}