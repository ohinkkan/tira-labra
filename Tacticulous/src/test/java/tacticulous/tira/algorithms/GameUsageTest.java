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
        for (int i = 0; i < 1000; i++) {
            BattleMap map = new BattleMap(5, die, die.roll());
            Unit unit = new Unit(die.roll(), 1, 1, 1, "A");
            map.getTile(die.roll(), die.roll()).setUnit(unit);
            GameUsage.speedRange(unit, map);
        }
    }
}