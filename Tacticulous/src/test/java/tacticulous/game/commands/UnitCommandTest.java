package tacticulous.game.commands;

import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.FixedDie;

/**
 *
 * @author O
 */
public class UnitCommandTest {

    BattleMap map;
    Tile tile;
    Unit unit;
    Die die;

    @Test
    public void legalMoveAndCheckWorks() {
        die = new FixedDie(5);
        map = new BattleMap(10, 0);
        unit = new Unit(10, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        int[][] costs = new int[10][10];
        assertTrue(UnitCommand.checkMove(map, unit, 5, 5, costs));
        UnitCommand.move(map, unit, map.getTile(5, 5));
        assertEquals(null, map.getTile(0, 0).getUnit());
        assertEquals(unit, map.getTile(5, 5).getUnit());
    }

    @Test
    public void outOfBoundsMoveDoesNotWork() {
        int[][] costs = new int[10][10];
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertTrue(!UnitCommand.checkMove(map, unit, 5, 11, costs));

    }

    @Test
    public void tooSlowMoveDoesNotWork() {
        int[][] costs = new int[10][10];
        costs[5][5] = 2;
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertTrue(!UnitCommand.checkMove(map, unit, 5, 5, costs));
    }

    @Test
    public void CannotMoveIfTargetTileOccupied() {
        int[][] costs = new int[10][10];
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        assertTrue(!UnitCommand.checkMove(map, unit, 1, 1, costs));
    }

    @Test
    public void legalAttackCheckWorks() {
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(0, 1).setUnit(unit2);
        assertTrue(UnitCommand.checkAttack(map, unit, 0, 1));
    }

    @Test
    public void targetMustBeWithinMapBoundaries() {
        die = new FixedDie(5);
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertTrue(!UnitCommand.checkAttack(map, unit, 1, 11));
    }

    @Test
    public void mustHaveTarget() {
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertTrue(!UnitCommand.checkAttack(map, unit, 1, 1));
    }

    @Test
    public void cannotAttackItself() {
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        assertTrue(!UnitCommand.checkAttack(map, unit, 0, 0));
    }

    @Test
    public void targetNotInRange() {
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "A");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(0, 2).setUnit(unit2);
        assertTrue(!UnitCommand.checkAttack(map, unit, 0, 2));
    }

    @Test
    public void hitRemovesHealth() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "A");
        UnitCommand.attackRoll(unit, unit2, 1);
        assertEquals(0, unit2.getHitPoints());
    }

    @Test
    public void missDoesNotRemoveHealth() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        UnitCommand.attackRoll(unit, unit2, -1);
        assertEquals(1, unit2.getHitPoints());
    }

    @Test
    public void unitDiesAtZeroHitpoints() {
        unit = new Unit(1, 1, 1, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 1, "B");
        assertEquals(1, UnitCommand.attackRoll(unit, unit2, 1));
    }

    @Test
    public void lethalAttackRemovesTarget() {
        die = new FixedDie(5);
        map = new BattleMap(10, 0);
        Player player = new Player("Test", null);
        unit = new Unit(1, 1, 1, 1, 1, "A", player);
        Unit unit2 = new Unit(1, 1, 1, 1, 1, "B", player);
        player.addUnit(unit);
        player.addUnit(unit2);
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        UnitCommand.attack(die, unit, map.getTile(1, 1));
        assertEquals(null, map.getTile(1, 1).getUnit());
        assertTrue(!player.getUnits().contains(unit2));
    }

    @Test
    public void missingAttackDoesNotRemoveTarget() {
        die = new FixedDie(-2);
        map = new BattleMap(10, 0);
        Player player = new Player("Test", null);
        unit = new Unit(1, 1, 1, 1, 1, "A", player);
        Unit unit2 = new Unit(1, 1, 1, 1, 1, "B", player);
        player.addUnit(unit);
        player.addUnit(unit2);
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        UnitCommand.attack(die, unit, map.getTile(1, 1));
        assertEquals(unit2, map.getTile(1, 1).getUnit());
        assertTrue(player.getUnits().contains(unit2));
    }

    @Test
    public void nonLethalAttackDoesNotRemoveTarget() {
        die = new FixedDie(2);
        map = new BattleMap(10, 0);
        Player player = new Player("Test", null);
        unit = new Unit(1, 1, 1, 1, 1, "A", player);
        Unit unit2 = new Unit(1, 1, 1, 1, 2, "B", player);
        player.addUnit(unit);
        player.addUnit(unit2);
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        UnitCommand.attack(die, unit, map.getTile(1, 1));
        assertTrue(player.getUnits().contains(unit2));
    }

    @Test
    public void chanceToHitWorks() {
        map = new BattleMap(10, 0);
        unit = new Unit(1, 1, 0, 1, "A");
        Unit unit2 = new Unit(1, 5, 1, 1, "B");
        map.getTile(0, 0).setUnit(unit);
        map.getTile(1, 1).setUnit(unit2);
        assertEquals(40, UnitCommand.chanceToHit(unit, unit2));

        unit2 = new Unit(1, 10, 1, 1, "B");
        map.getTile(1, 1).setUnit(unit2);
        assertEquals(0, UnitCommand.chanceToHit(unit, unit2));

        unit2 = new Unit(1, 0, 1, 1, "B");
        map.getTile(1, 1).setUnit(unit2);
        assertEquals(90, UnitCommand.chanceToHit(unit, unit2));

        unit2 = new Unit(1, 12, 1, 1, "B");
        map.getTile(1, 1).setUnit(unit2);
        assertEquals(0, UnitCommand.chanceToHit(unit, unit2));

        unit2 = new Unit(1, -2, 1, 1, "B");
        map.getTile(1, 5).setUnit(unit2);
        assertEquals(70, UnitCommand.chanceToHit(unit, unit2));

        unit2 = new Unit(1, -10, 1, 1, "B");
        map.getTile(1, 5).setUnit(unit2);
        assertEquals(100, UnitCommand.chanceToHit(unit, unit2));
    }
}
